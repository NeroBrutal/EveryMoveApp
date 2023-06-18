package everymoveapp.prototype;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class Profile_Edit_Activity extends AppCompatActivity {

    private Button deleteBtn,EmailChange,EditProfile,ProfilePicUpdate,ChangePassword,LogOut;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile_edit);

        EditProfile = findViewById(R.id.Edit_text_Profile);
        EmailChange = findViewById(R.id.Edit_text_Change_Email);
        deleteBtn = findViewById(R.id.Delete_button);
        ProfilePicUpdate = findViewById(R.id.Edit_pic_Profile);
        ChangePassword = findViewById(R.id.ChangePassword);
        LogOut = findViewById(R.id.LogOut);


        EditProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Profile_Edit_Activity.this, EditProfileActivity.class);
                startActivity(intent);
                finish();
            }
        });

        ProfilePicUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Profile_Edit_Activity.this, SetUpProfilepicActivity.class);
                startActivity(intent);
                finish();

            }
        });

        EmailChange.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Profile_Edit_Activity.this, UpdateEmailActivity.class);
                startActivity(intent);
                finish();
            }
        });

        deleteBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Profile_Edit_Activity.this, DeleteProfile.class);
                startActivity(intent);
                finish();
            }
        });

        ChangePassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Profile_Edit_Activity.this, ChangeProflePassword.class);
                startActivity(intent);
                finish();
            }
        });

        LogOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ReportWrite("User is Logged Out");
                FirebaseAuth auth;
                auth = FirebaseAuth.getInstance();
                auth.signOut();
                Intent intent = new Intent(Profile_Edit_Activity.this, IntroActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
                finish();
            }
        });




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
        DatabaseReference myRef = database.getReference("/Report/LoginFunction");

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
