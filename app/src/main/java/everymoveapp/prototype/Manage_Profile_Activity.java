package everymoveapp.prototype;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;


public class Manage_Profile_Activity extends AppCompatActivity {

    private FirebaseAuth AuthProfile;
    private EditText FullName_text,Email_text,Dob_text,Gender_text,Height_text,Weight_text;
    private TextView Welcome_text;
    private ImageView image_view_profile_Dp;

    private String fullName,Email,Dob,Gender,Height,Weight;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manage_profile);

        Welcome_text = findViewById(R.id.textview_show_Welcome);
        FullName_text = findViewById(R.id.text_view_Full_Name);
        Email_text = findViewById(R.id.text_view_Email);
        Dob_text = findViewById(R.id.text_view_Dob);
        Gender_text = findViewById(R.id.text_view_Gender);
        Height_text = findViewById(R.id.text_view_Height);
        Weight_text = findViewById(R.id.text_view_Weight);
        image_view_profile_Dp = findViewById(R.id.image_view_profile_Dp);

        AuthProfile = FirebaseAuth .getInstance();
        FirebaseUser firebaseUser = AuthProfile.getCurrentUser();

        image_view_profile_Dp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Manage_Profile_Activity.this, SetUpProfilepicActivity.class);
                startActivity(intent);
            }
        });



        if(firebaseUser == null){
            Toast.makeText(Manage_Profile_Activity.this, "Something Wrong", Toast.LENGTH_LONG).show();
        }
        else {
            showUserData(firebaseUser);
        }

    }

    private void showUserData(FirebaseUser firebaseUser) {

        String UserID = firebaseUser.getUid();
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("Registered Users");
        databaseReference.child(UserID).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                ReadWriteUserDetails readUserDetails = snapshot.getValue(ReadWriteUserDetails.class);

                if(readUserDetails != null){
                    fullName = firebaseUser.getDisplayName();
                    Email = firebaseUser.getEmail();
                    Dob = readUserDetails.Dob;
                    Gender = readUserDetails.Gender;
                    Height = readUserDetails.Height;
                    Weight = readUserDetails.Weight;

                    Welcome_text.setText("Welcome, " + fullName +"!");
                    FullName_text.setText("Full Name : " + fullName);
                    Email_text.setText("Email : " +Email);
                    Dob_text.setText("Date of Birth : " +Dob);
                    Gender_text.setText("Gender : " +Gender);
                    Height_text.setText("Height : " +Height + " cm");
                    Weight_text.setText("Weight : " +Weight + " Kg");

                    Uri uri = firebaseUser.getPhotoUrl();

                    Picasso.with(Manage_Profile_Activity.this).load(uri).into(image_view_profile_Dp);
                }
                else {
                    Toast.makeText(Manage_Profile_Activity.this, "Something went Wrong...", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(Manage_Profile_Activity.this, "Something Wrong", Toast.LENGTH_LONG).show();

            }
        });
    }


}
