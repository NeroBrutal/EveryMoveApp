package everymoveapp.prototype;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.EmailAuthCredential;
import com.google.firebase.auth.EmailAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class UpdateEmailActivity extends AppCompatActivity {

    private FirebaseAuth authProfile;
    private FirebaseUser firebaseUser;
    private EditText Old_email,Password,New_email;
    private TextView AuthenticateText;
    private Button Authenticate,Save;
    private String userOldEmail,userNewEmail,userPassword;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_email);

        Old_email = findViewById(R.id.Edit_Email);
        Password = findViewById(R.id.Edit_Password);
        New_email = findViewById(R.id.Edit_New_Email);
        Authenticate = findViewById(R.id.Authenticate_Button);
        Save = findViewById(R.id.Edit_save_button);

        Save.setEnabled(false);
        New_email.setEnabled(false);

        authProfile = FirebaseAuth.getInstance();
        firebaseUser = authProfile.getCurrentUser();

        userOldEmail = firebaseUser.getEmail();

        if(firebaseUser.equals("")){
            Toast.makeText(this, "Something went wrong", Toast.LENGTH_LONG).show();
        }
        else{
            reAuthenticate(firebaseUser);
        }


    }

    private void reAuthenticate(FirebaseUser firebaseUser) {
        Authenticate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                userPassword = Password.getText().toString();

                if(TextUtils.isEmpty(userPassword)){
                    Toast.makeText(UpdateEmailActivity.this, "Password Can't be Empty...", Toast.LENGTH_SHORT).show();
                    Password.setError("Please Enter Your Password foe authentication");
                    Password.requestFocus();
                }
                else{
                    AuthCredential credential = EmailAuthProvider.getCredential(userOldEmail,userPassword);

                    firebaseUser.reauthenticate(credential).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if(task.isSuccessful()){
                                Toast.makeText(UpdateEmailActivity.this, "Password has been verified", Toast.LENGTH_SHORT).show();

                                New_email.setEnabled(true);
                                Password.setEnabled(false);
                                Save.setEnabled(true);
                                Authenticate.setEnabled(false);

                                AuthenticateText = findViewById(R.id.AuthenticateText);

                                AuthenticateText.setText("You are authenticated You can change the email ");

                                Save.setBackgroundTintList(ContextCompat.getColorStateList(UpdateEmailActivity.this,R.color.color_one));

                                Save.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        userNewEmail = New_email.getText().toString();

                                        if(TextUtils.isEmpty(userNewEmail)){
                                            Toast.makeText(UpdateEmailActivity.this, "New Email is required...", Toast.LENGTH_SHORT).show();
                                            New_email.setError("Please Enter the new mail...");
                                            New_email.requestFocus();
                                        }
                                        else if (!Patterns.EMAIL_ADDRESS.matcher(userNewEmail).matches()){
                                            Toast.makeText(UpdateEmailActivity.this, "Please Enter valid  Email..", Toast.LENGTH_SHORT).show();
                                            New_email.setError("Please provide valid mail...");
                                            New_email.requestFocus();
                                        } else if (userOldEmail.matches(userNewEmail)) {
                                            Toast.makeText(UpdateEmailActivity.this, "Email cant be same", Toast.LENGTH_SHORT).show();
                                            New_email.setError("Please Enter new mail...");
                                            New_email.requestFocus();
                                        }
                                        else {
                                            updateEmail(firebaseUser);
                                        }
                                    }
                                });
                            }
                            else {
                                try {
                                    throw task.getException();
                                }catch (Exception e){
                                    Toast.makeText(UpdateEmailActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                                }
                            }
                        }
                    });
                }

            }
        });
    }

    private void updateEmail(FirebaseUser firebaseUser) {
        firebaseUser.updateEmail(userNewEmail).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if(task.isSuccessful()){
                    firebaseUser.sendEmailVerification();

                    Toast.makeText(UpdateEmailActivity.this, "Email has been updated..", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(UpdateEmailActivity.this,Manage_Profile_Activity.class);
                    startActivity(intent);
                    finish();
                }
                else {
                    try {
                        throw task.getException();
                    }catch (Exception e){
                        Toast.makeText(UpdateEmailActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });
    }
}