package everymoveapp.prototype;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseAuthInvalidUserException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.ktx.Firebase;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class LoginActivity extends AppCompatActivity {
    private EditText loginUsername, loginPassword;
    private Button loginButton;
    private TextView signupRedirectText,forgotPassword;
    private static final String TAG="LoginActivity";

    FirebaseAuth database;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        loginUsername = findViewById(R.id.login_username);
        loginPassword = findViewById(R.id.login_password);
        loginButton = findViewById(R.id.login_button);
        signupRedirectText = findViewById(R.id.signupRedirectText);
        forgotPassword = findViewById(R.id.forgot_password);
        database = FirebaseAuth.getInstance();

        ImageView imageViewShowHidePwd = findViewById(R.id.show_hidePwd);
        imageViewShowHidePwd.setImageResource(R.drawable.ic_hide_pwd);
        imageViewShowHidePwd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(loginPassword.getTransformationMethod().equals(HideReturnsTransformationMethod.getInstance())){
                    loginPassword.setTransformationMethod(PasswordTransformationMethod.getInstance());

                    imageViewShowHidePwd.setImageResource(R.drawable.ic_hide_pwd);
                }
                else {
                     loginPassword.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                    imageViewShowHidePwd.setImageResource(R.drawable.ic_show_pwd);
                }
            }
        });
        forgotPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LoginActivity.this, ForgotPasswordActivity.class);
                startActivity(intent);
                finish();

            }
        });


        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ReportWrite("User is Logged In");

                String userUsername = loginUsername.getText().toString();
                String userPassword = loginPassword.getText().toString();
                if (TextUtils.isEmpty(userUsername)) {
                    loginUsername.setError("Username cannot be empty");
                    return;
                } else if (TextUtils.isEmpty(userPassword)){
                    loginPassword.setError("Password cannot be empty");
                    return;
                }
                else {
                    database.signInWithEmailAndPassword(userUsername,userPassword).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {

                            if (task.isSuccessful()){
                                Toast.makeText(LoginActivity.this, "Login Successful...", Toast.LENGTH_SHORT).show();
                                Intent intent = new Intent(LoginActivity.this, DashboardActivity.class);
                                startActivity(intent);
                                finish();


                            }

                            else {

                                try {
                                    throw task.getException();
                                } catch (FirebaseAuthInvalidUserException e){
                                    loginUsername.setError("User does not exist or is not longer valid. Please register again.");
                                    loginUsername.requestFocus();
                                } catch (FirebaseAuthInvalidCredentialsException e) {
                                    loginUsername.setError("Invalid credentials. Kindly check and re-enter");
                                    loginUsername.requestFocus();
                                } catch (Exception e){
                                    Log.e(TAG, e.getMessage());
                                    Toast.makeText(LoginActivity.this, "Something went wrong", Toast.LENGTH_SHORT).show();
                                }

                            }

                        }
                    });
                }
            }
        });
        signupRedirectText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(LoginActivity.this, SignupActivity.class);
                startActivity(intent);
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        FirebaseUser user = database.getCurrentUser();

        if (user != null) {
            Intent intent = new Intent(LoginActivity.this, DashboardActivity.class);
            startActivity(intent);
            this.finish();
        }
        else {
            Toast.makeText(this, "You can login Now!!", Toast.LENGTH_SHORT).show();
        }
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