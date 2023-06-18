package everymoveapp.prototype;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Calendar;

public class EditProfileActivity extends AppCompatActivity {

    private EditText Edit_name,Edit_Dob,Edit_height,Edit_weight;
    private RadioGroup EditRadio_groupGender;
    private RadioButton RadioButton;
    private Button Edit_save_button;
    private String FullName,Dob,Gender,Height,Weight;
    private FirebaseAuth authProfile;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile);

        Edit_name = findViewById(R.id.Edit_FullName);
        Edit_Dob = findViewById(R.id.Edit_DateOfBirth);
        Edit_height = findViewById(R.id.Edit_Height);
        Edit_weight = findViewById(R.id.Edit_Weight);
        Edit_save_button = findViewById(R.id.Edit_save_button);

        EditRadio_groupGender = findViewById(R.id.radio_group_gender);

        authProfile = FirebaseAuth.getInstance();
        FirebaseUser firebaseUser = authProfile.getCurrentUser();

        showProfile(firebaseUser);

    }

    private void showProfile(FirebaseUser firebaseUser) {

        String userIDofRegistered = firebaseUser.getUid();

        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Registered Users");
        reference.child(userIDofRegistered).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                ReadWriteUserDetails readUserDetails = snapshot.getValue(ReadWriteUserDetails.class);

                if(readUserDetails != null){
                    FullName = firebaseUser.getDisplayName();
                    Dob = readUserDetails.Dob;
                    Gender = readUserDetails.Gender;
                    Height = readUserDetails.Height;
                    Weight = readUserDetails.Weight;

                    Edit_name.setText(FullName);
                    Edit_Dob.setText(Dob);
                    Edit_height.setText(Height);
                    Edit_weight.setText(Weight);

                    if(Gender.equals("Male")){
                        RadioButton = findViewById(R.id.Edit_radio_Male);
                    }
                    else {
                        RadioButton = findViewById(R.id.Edit_radio_Female);
                    }
                    RadioButton.setChecked(true);
                }
                else {
                    Toast.makeText(EditProfileActivity.this, "Something went wrong..", Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(EditProfileActivity.this, "Something went wrong..", Toast.LENGTH_LONG).show();

            }
        });

        Edit_Dob.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String textSADob[] = Dob.split("/");

                int day = Integer.parseInt(textSADob[0]);
                int month = Integer.parseInt(textSADob[1]) - 1;
                int year = Integer.parseInt(textSADob[2]);

                DatePickerDialog picker;

                picker = new DatePickerDialog(EditProfileActivity.this, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                        Edit_Dob.setText(dayOfMonth + "/" + (month + 1) + "/" + year);

                    }
                },year,month,day);
                picker.show();

            }
        });

        Edit_save_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                UpdateProfile(firebaseUser);
            }
        });
    }

    private void UpdateProfile(FirebaseUser firebaseUser) {

        int selectGenderID = EditRadio_groupGender.getCheckedRadioButtonId();
        RadioButton = findViewById(selectGenderID);

        FullName = Edit_name.getText().toString();
        Dob = Edit_Dob.getText().toString();
        Height = Edit_height.getText().toString();
        Weight = Edit_weight.getText().toString();



        if(TextUtils.isEmpty((FullName))){
            Toast.makeText(EditProfileActivity.this,"Please Enter Your Full Name ",Toast.LENGTH_LONG).show();
            Edit_name.setError("Full name is required");
            Edit_name.requestFocus();
        }

        else if(TextUtils.isEmpty((Dob))){
            Toast.makeText(EditProfileActivity.this,"Please Enter Your Date of Birth ",Toast.LENGTH_LONG).show();
            Edit_Dob.setError("Date of Birth is required");
            Edit_Dob.requestFocus();
        }
        else if(TextUtils.isEmpty(RadioButton.getText())){
            Toast.makeText(EditProfileActivity.this,"Please select your gender ",Toast.LENGTH_LONG).show();
            RadioButton.setError("Gender is required");
            RadioButton.requestFocus();
        }
        else if(TextUtils.isEmpty((Height))){
            Toast.makeText(EditProfileActivity.this,"Please Enter Your Height ",Toast.LENGTH_LONG).show();
            Edit_height.setError("Height is required");
            Edit_height.requestFocus();
        }
        else if(TextUtils.isEmpty((Weight))){
            Toast.makeText(EditProfileActivity.this,"Please Enter Your Weight ",Toast.LENGTH_LONG).show();
            Edit_weight.setError("Weight is required");
            Edit_weight.requestFocus();
        }
        else {
            Gender = RadioButton.getText().toString();
            FullName = Edit_name.getText().toString();
            Dob = Edit_Dob.getText().toString();
            Height = Edit_height.getText().toString();
            Weight = Edit_weight.getText().toString();


            ReadWriteUserDetails writeUserDetails = new ReadWriteUserDetails(Dob,Gender,Height,Weight);

            DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Registered Users");

            String userID = firebaseUser.getUid();


            reference.child(userID).setValue(writeUserDetails).addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    if(task.isSuccessful()){
                        UserProfileChangeRequest profileUpdates = new UserProfileChangeRequest.Builder().
                                setDisplayName(FullName).build();
                        firebaseUser.updateProfile(profileUpdates);

                        Toast.makeText(EditProfileActivity.this, "Update Successful...", Toast.LENGTH_SHORT).show();

                        Intent intent = new Intent(EditProfileActivity.this,Profile_Edit_Activity.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK |
                                Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(intent);
                        finish();

                    }
                }
            });
        }
    }
}
