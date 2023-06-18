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

public class ChangeProflePassword extends AppCompatActivity {

    private EditText Old_Password,Edit_New_Password,Edit_New_PasswordConfirm;
    private TextView AuthenticateText;
    private String userOldPwd,userNewPwd,userNewPwdCfm;
    private FirebaseAuth auth;
    private Button Authenticate_Button,Edit_save_button;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_profle_password);

        Old_Password = findViewById(R.id.Old_Password);
        Edit_New_Password = findViewById(R.id.Edit_New_Password);
        Edit_New_PasswordConfirm = findViewById(R.id.Edit_New_PasswordConfirm);
        AuthenticateText = findViewById(R.id.AuthenticateText);
        Authenticate_Button = findViewById(R.id.Authenticate_Button);
        Edit_save_button = findViewById(R.id.Edit_save_button);

        Edit_New_Password.setEnabled(false);
        Edit_New_PasswordConfirm.setEnabled(false);
        Edit_save_button.setEnabled(false);


        auth = FirebaseAuth.getInstance();

        FirebaseUser firebaseUser = auth.getCurrentUser();


        if(firebaseUser.equals("")){
            Toast.makeText(this, "Something went wrong. User Details not Available", Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(ChangeProflePassword.this,Manage_Profile_Activity.class);
            startActivity(intent);
            finish();
        }
        else {
            reAuthenticate(firebaseUser);
        }

    }

    private void reAuthenticate(FirebaseUser firebaseUser) {
        Authenticate_Button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                userOldPwd = Old_Password.getText().toString();

                if(TextUtils.isEmpty(userOldPwd)){
                    Toast.makeText(ChangeProflePassword.this, "Password is needed", Toast.LENGTH_SHORT).show();
                    Old_Password.setError("Please Enter the Password...");
                    Old_Password.requestFocus();
                }
                else {

                    AuthCredential credential = EmailAuthProvider.getCredential(firebaseUser.getEmail(),userOldPwd);

                    firebaseUser.reauthenticate(credential).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if(task.isSuccessful()){
                                Old_Password.setEnabled(false);
                                Edit_New_Password.setEnabled(true);
                                Edit_New_PasswordConfirm.setEnabled(true);
                                Authenticate_Button.setEnabled(false);
                                Edit_save_button.setEnabled(true);

                                AuthenticateText.setText("You are Authenticated.  Please Enter the new Password..");


                                Edit_save_button.setBackgroundTintList(ContextCompat.getColorStateList(ChangeProflePassword.this,R.color.color_one));

                                Edit_save_button.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        ChangePwd(firebaseUser);
                                    }
                                });

                            }

                        }
                    });
                }

            }
        });
    }

    private void ChangePwd(FirebaseUser firebaseUser) {
        userNewPwd = Edit_New_Password.getText().toString();
        userNewPwdCfm = Edit_New_PasswordConfirm.getText().toString();

        if(TextUtils.isEmpty(userNewPwd)){
            Toast.makeText(this, "New Password is empty", Toast.LENGTH_SHORT).show();
            Edit_New_Password.setError("Password is required");
            Edit_New_Password.requestFocus();
        }
        else if(TextUtils.isEmpty(userNewPwdCfm)){
            Toast.makeText(this, "Please confirm your New Password ", Toast.LENGTH_SHORT).show();
            Edit_New_PasswordConfirm.setError("Password is required");
            Edit_New_PasswordConfirm.requestFocus();
        }
        else if(!userNewPwd.matches(userNewPwdCfm)){
            Toast.makeText(this, "Password did not match", Toast.LENGTH_SHORT).show();
            Edit_New_PasswordConfirm.setError("Please enter same password");
            Edit_New_PasswordConfirm.requestFocus();
        } else if (userOldPwd.matches(userNewPwd)) {
            Toast.makeText(this, "Password Cant be as old password", Toast.LENGTH_SHORT).show();
            Edit_New_Password.setError("Please enter another password");
            Edit_New_Password.requestFocus();
        } else {
            firebaseUser.updatePassword(userNewPwd).addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    if(task.isSuccessful()){
                        Toast.makeText(ChangeProflePassword.this, "Password changed Successfully", Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(ChangeProflePassword.this,Manage_Profile_Activity.class);
                        startActivity(intent);
                        finish();
                    }
                    else {
                        try {
                            throw task.getException();
                        }catch (Exception e){
                            Toast.makeText(ChangeProflePassword.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    }

                }
            });
        }
    }
}