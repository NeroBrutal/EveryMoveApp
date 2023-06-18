package everymoveapp.prototype;


import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.Date;

public class PlanA extends AppCompatActivity {



    FirebaseAuth firebaseAuth;
    TextView txtPlanA;

    String db_ID, User_ID, planName;


    Button back,addPlanA;

    DatabaseReference databaseRefPlanA, databaseRefCurrentUser, databaseRefPersonalPlans;

    FirebaseUser currentUser;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_plan);
        back= findViewById(R.id.bckBtn);
        db_ID = "-NX9_x1Fd9CIs7lZ2zTP";
        databaseRefPlanA = FirebaseDatabase.getInstance().getReference("Workouts");
        databaseRefCurrentUser=FirebaseDatabase.getInstance().getReference("Users");
        txtPlanA = findViewById(R.id.txtPlanD);
        FirebaseApp.initializeApp(this);
        firebaseAuth = FirebaseAuth.getInstance();
        currentUser= FirebaseAuth.getInstance().getCurrentUser();

        Intent intent = getIntent();
        User_ID=intent.getStringExtra("UserID");
        // get uid from dashboard  through intent function and assign here
        planName = "Balanced";

        addPlanA = findViewById(R.id.btnPersonalPlanA);
        databaseRefPersonalPlans = FirebaseDatabase.getInstance().getReference("Personal Plans");






        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent= new Intent(getApplicationContext(), RaeedMainActivity.class);
                startActivity(intent);
            }
        });

        addPlanA.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               /* if (currentUser != null) {
                    Log.d("user verification","user is not null!!");
                    String uid = currentUser.getUid();
                    DatabaseReference currentUserRef = databaseRefCurrentUser.child(uid);
                    DatabaseReference planRef = currentUserRef.child("workouts");
                    planRef.setValue("Plan A").addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful()) {
                                // Value set successfully
                                Toast.makeText(getApplicationContext(), "Workout plan set to Plan A", Toast.LENGTH_SHORT).show();
                            } else {
                                // Failed to set value
                                Toast.makeText(getApplicationContext(), "Failed to set workout plan", Toast.LENGTH_SHORT).show();
                            }

                        }
                    });




                } else {

                }


               */

                PersonalPlans planA = new PersonalPlans(User_ID,planName);
                databaseRefPersonalPlans.child(User_ID).setValue(planA);
                Toast.makeText(getApplicationContext(),"Added to personal list", Toast.LENGTH_SHORT).show();
                Date date = new Date();
                SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
                String Date = format.format(date);

                DatabaseReference logsRef= FirebaseDatabase.getInstance().getReference("Logs_PersonalPlans");
                logsRef.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        if(snapshot.exists()){
                            logsRef.child(Date).setValue(User_ID + "   added given plan to their list: " + planName);                        }


                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });





            }
        });

    }




    @Override
    protected void onStart() {
        super.onStart();
        databaseRefPlanA.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                DataSnapshot snapA=  snapshot.child(db_ID);
                Workouts workoutA = snapA.getValue(Workouts.class);
                txtPlanA.setText(workoutA.getPlanName().toString());
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }
}