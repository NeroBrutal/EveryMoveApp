package everymoveapp.prototype;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

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

public class PlanC extends AppCompatActivity {


    TextView txtPlanC;

    String db_ID, User_ID, planName;


    Button backC,addPlanC;

    DatabaseReference databaseRefPlanC,databaseRefPersonalPlans;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_plan_c);
        db_ID = "-NX9aeUL-AFfHX0VHTfc";
        Intent intent = getIntent();

        User_ID=intent.getStringExtra("UserID");
        // get uid from dashboard  through intent function and assign here
        planName = "Strength";
        databaseRefPlanC = FirebaseDatabase.getInstance().getReference("Workouts");
        databaseRefPersonalPlans = FirebaseDatabase.getInstance().getReference("Personal Plans");

        txtPlanC= findViewById(R.id.txtPlanD);


        addPlanC = findViewById(R.id.btnAddPersonalPlanC);

        addPlanC.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PersonalPlans planC = new PersonalPlans(User_ID,planName);
                databaseRefPersonalPlans.child(User_ID).setValue(planC);
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
        databaseRefPlanC.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                DataSnapshot snapA=  snapshot.child(db_ID);
                Workouts workoutA = snapA.getValue(Workouts.class);
                txtPlanC.setText(workoutA.getPlanName().toString());
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}