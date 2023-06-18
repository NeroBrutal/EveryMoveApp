package everymoveapp.prototype;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class createDietPlan extends AppCompatActivity {

    String PlanName,PlanCategory, PlanDescription;
    Button addDpBtn;
    EditText Pname,Pcat,Pdesc;

    ListView listViewDiets;
    List<DietPlans>dietPlansList;


    DatabaseReference dietdatabaseRefPlanA, dietdatabaseRefPlanB;
    DatabaseReference databaseRefDietPlans;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_diet_plan);
        addDpBtn =findViewById(R.id.DPaddBtn);
        Pname= findViewById(R.id.editTextPname);
        Pcat = findViewById(R.id.editTextPcat);
        Pdesc= findViewById(R.id.editTextPdesc);

        Intent intent = getIntent();

        String Username = "Aaroophan";

        listViewDiets = findViewById(R.id.listViewDiets);
        databaseRefDietPlans= FirebaseDatabase.getInstance().getReference("Diet Plans");

        Log.d("ÄAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA","                     VALUE                      "+databaseRefDietPlans);

        dietPlansList= new ArrayList<>();


        String Name = intent.getStringExtra("Name");
        String Category = intent.getStringExtra("Category");
        String Description = intent.getStringExtra("Description");

        Pname.setText(Name);
        Pcat.setText(Category);
        Pdesc.setText(Description);



        addDpBtn.setOnClickListener(new View.OnClickListener() {
            @Override


            public void onClick(View v) {
                addDietPlan(Username);
            }
        });
    }
    private void addDietPlan(String Username){

        dietdatabaseRefPlanA = FirebaseDatabase.getInstance().getReference("Diet Plans/"+Username);

        dietdatabaseRefPlanA.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(!snapshot.exists()){
                    dietdatabaseRefPlanA = FirebaseDatabase.getInstance().getReference("Diet Plans");
                    databaseRefDietPlans.child(Username).setValue("");
                }
                else{
                    Log.d("ÄAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA","                     VALUE                      Exists");
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        dietdatabaseRefPlanB = FirebaseDatabase.getInstance().getReference("Diet Plans/"+Username);
        Log.d("ÄAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA","                     VALUE                      "+dietdatabaseRefPlanA);

        dietdatabaseRefPlanB.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.exists()){
                    ReportWrite("Diet Plan is created");
                    PlanName = Pname.getText().toString();
                    PlanCategory= Pcat.getText().toString();
                    PlanDescription = Pdesc.getText().toString();
                    String PlanID = PlanName;
                    DietPlans dietPlans = new DietPlans(PlanCategory,PlanDescription,PlanName,PlanName);
                    dietdatabaseRefPlanB.child(PlanID).setValue(dietPlans);
                    Toast.makeText(createDietPlan.this, "Added Succesfully", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


    }

    @Override
    protected void onStart() {
        super.onStart();

        /*databaseRefDietPlans.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                dietPlansList.clear();
                for(DataSnapshot dietSnapshot : snapshot.getChildren()){
                    DietPlans dietPlans = dietSnapshot.getValue(DietPlans.class);
                    dietPlansList.add(dietPlans);
                }
                DietList adapter = new DietList(createDietPlan.this, dietPlansList);
                listViewDiets.setAdapter(adapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });*/
    }

    public void ReportWrite(String Entry){

        Calendar calendar = Calendar.getInstance();
        int hour = calendar.get(Calendar.HOUR_OF_DAY);
        int minute = calendar.get(Calendar.MINUTE);
        int second = calendar.get(Calendar.SECOND);

        Date date = new Date();
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
        String Date = formatter.format(date);;
        String[] DateParts = Date.split("-");
        int DateDay = Integer.parseInt(DateParts[2]);
        int DateMonth = Integer.parseInt(DateParts[1]);
        int DateYear = Integer.parseInt(DateParts[0]);

        String Time = DateYear+"-"+DateMonth+"-"+DateDay+"-"+hour+"-"+minute+"-"+second;
        Log.d("AAAAAAAAAAAAAA", Time);

        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference("/Report/DietPlans");

        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    myRef.child(Time).setValue(Entry);
                }
            }
            @Override
            public void onCancelled(DatabaseError error) {
                Log.w("AAAAAAAAAAAAAA", "Failed to read value.", error.toException());
            }
        });
    }
}