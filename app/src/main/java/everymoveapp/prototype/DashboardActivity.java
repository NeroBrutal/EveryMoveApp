package everymoveapp.prototype;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class DashboardActivity extends AppCompatActivity {

    private CardView profileManage,settings,Progress,nutritionCard,workoutCard,mealCard;
    private FirebaseAuth auth;
    private String userID,Username;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);

        profileManage = findViewById(R.id.profileCard);
        settings = findViewById(R.id.settingCard);
        Progress = findViewById(R.id.ProgressCard);
        nutritionCard = findViewById(R.id.nutritionCard);
        workoutCard = findViewById(R.id.workoutCard);
        mealCard = findViewById(R.id.mealCard);


        auth = FirebaseAuth.getInstance();
        FirebaseUser firebaseUser = auth.getCurrentUser();
        userID = firebaseUser.getUid();

        DatabaseReference RegUsers = FirebaseDatabase.getInstance().getReference("Registered Users");
        RegUsers.child(userID).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                ReadWriteUserDetails readDetails = snapshot.getValue(ReadWriteUserDetails.class);
                if (readDetails != null){
                    Username = firebaseUser.getDisplayName();

                }

                else {

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


        profileManage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(DashboardActivity.this, Manage_Profile_Activity.class);
                startActivity(intent);
            }
        });
        settings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(DashboardActivity.this, Profile_Edit_Activity.class);
                startActivity(intent);
            }
        });

        Progress.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(DashboardActivity.this, ProgressOrSaved.class);
                intent.putExtra("Username",Username);
                startActivity(intent);

            }
        });
        nutritionCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(DashboardActivity.this, MainActivity.class);
                startActivity(intent);
            }
        });

        workoutCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(DashboardActivity.this, RaeedMainActivity.class);
                intent.putExtra("UserID",userID);
                startActivity(intent);
            }
        });

        mealCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(DashboardActivity.this, AmharMainActivity.class);
                intent.putExtra("UserID",userID);
                intent.putExtra("Username",Username);
                startActivity(intent);
            }
        });


    }
}