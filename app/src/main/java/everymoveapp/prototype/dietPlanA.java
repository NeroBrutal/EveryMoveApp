package everymoveapp.prototype;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;


public class dietPlanA extends AppCompatActivity {

    TextView txtPlanName, txtPlanCate, txtPlanDesc;

    String planID="";

    Button back, btnDelete, btnEdit;
    DatabaseReference dietdatabaseRefPlanA;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_diet_plan);
        Intent intent = getIntent();

        String Username = "Aaroophan";

        planID = intent.getStringExtra("Name");
        dietdatabaseRefPlanA = FirebaseDatabase.getInstance().getReference("Diet Plans/"+Username);
        txtPlanName = findViewById(R.id.txtDietName);
        txtPlanCate = findViewById(R.id.txtDietCate);
        txtPlanDesc = findViewById(R.id.txtDietDesc);
        Log.d("AAAAAAAAAAAAAA", "                                                                  Value "+planID);
    }


    @Override
    protected void onStart() {
        super.onStart();

        btnDelete = (Button) findViewById(R.id.btnDelete);
        btnEdit = (Button) findViewById(R.id.btnEdit);
        dietdatabaseRefPlanA.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                Log.d("AAAAAAAAAAAAAA", "                                                                  Value "+planID);

                DataSnapshot snapDietA = snapshot.child(planID);
                DietPlans dietPlanA =  snapDietA.getValue(DietPlans.class);
                try {
                    txtPlanName.setText(dietPlanA.getPlanName());
                }
                catch (Exception e) {
                    txtPlanName.setText("Default Plan Name");
                }
                try {
                    txtPlanCate.setText(dietPlanA.getPlanCategory());
                }
                catch (Exception e) {
                    txtPlanCate.setText("Default Plan Category");
                }
                try {
                    txtPlanDesc.setText(dietPlanA.getPlanDescription());
                }
                catch (Exception e) {
                    txtPlanDesc.setText("Default Plan Descriotion");
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        btnDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                startActivity(intent);
                DatabaseReference dietPlanRef = dietdatabaseRefPlanA.child(planID);
                dietPlanRef.removeValue();

                Toast.makeText(dietPlanA.this, "Deleted Succesfully", Toast.LENGTH_SHORT).show();
            }
        });

        btnEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                dietdatabaseRefPlanA.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        DataSnapshot snapDietA = snapshot.child(planID);
                        DietPlans dietPlanA =  snapDietA.getValue(DietPlans.class);
                        Intent intent = new Intent(getApplicationContext(), createDietPlan.class);
                        intent.putExtra("Name", dietPlanA.getPlanName());
                        intent.putExtra("Category", dietPlanA.getPlanCategory());
                        intent.putExtra("Description", dietPlanA.getPlanDescription());
                        startActivity(intent);

                        Toast.makeText(dietPlanA.this, "Going to Edit", Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
            }
        });
    }
}