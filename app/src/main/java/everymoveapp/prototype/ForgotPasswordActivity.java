package everymoveapp.prototype;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;

public class ForgotPasswordActivity extends AppCompatActivity {

    private Button Reset_Button;
    private EditText text_mail;

    private FirebaseAuth authProfile;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_password);

        text_mail = findViewById(R.id.text_mail);
        Reset_Button = findViewById(R.id.Reset_Button);

        Reset_Button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String Email = text_mail.getText().toString();

                if(TextUtils.isEmpty(Email)){
                    Toast.makeText(ForgotPasswordActivity.this, "Email can't be empty", Toast.LENGTH_SHORT).show();
                    text_mail.setText("Email can't be empty");
                }
                else if(!Patterns.EMAIL_ADDRESS.matcher(Email).matches()){
                    Toast.makeText(ForgotPasswordActivity.this, "Enter a valid Email address", Toast.LENGTH_SHORT).show();
                    text_mail.setText("Email is not valid");
                }
                else {
                    resetpassword(Email);
                }
            }
        });
    }

    private void resetpassword(String Email) {
        authProfile = FirebaseAuth.getInstance();
        authProfile.sendPasswordResetEmail(Email).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if(task.isSuccessful()){
                    Toast.makeText(ForgotPasswordActivity.this, "Check your inbox for reset the password", Toast.LENGTH_SHORT).show();

                    Intent intent = new Intent(ForgotPasswordActivity.this, LoginActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(intent);
                    finish();
                }
                else {
                    Toast.makeText(ForgotPasswordActivity.this, "Something went wrong", Toast.LENGTH_SHORT).show();
                }

            }
        });
    }
}