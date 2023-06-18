package everymoveapp.prototype;


import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class DietPlanB extends AppCompatActivity {

    TextView txtPlanB;

    String planID;


    Button back;
    DatabaseReference dietdatabaseRefPlanB;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_diet_plan_b);
        planID = "-NV3h2X65iq1bOb5KLnQ";
        dietdatabaseRefPlanB = FirebaseDatabase.getInstance().getReference("Diet Plans");
        txtPlanB = findViewById(R.id.txtDietPlanB);




    }


    @Override
    protected void onStart() {
        super.onStart();
        dietdatabaseRefPlanB.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                DataSnapshot snapshotDietB = snapshot.child(planID);
                DietPlans dietPlanB = snapshotDietB.getValue(DietPlans.class);
                txtPlanB.setText(dietPlanB.getPlanDescription());
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}