package everymoveapp.prototype;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.auth.FirebaseAuthWeakPasswordException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.lang.ref.Reference;
import java.util.Calendar;
import java.util.regex.Pattern;

public class SignupActivity extends AppCompatActivity {

    private EditText signupFullName, signupEmail, signupDob ,signupHeight,signupWeight,signupPassword,signupConfirmPassword;
    private RadioGroup radioGroupSignupGender;
    private RadioButton radioButtonSignupGender;
    private DatePickerDialog picker;
    private static final String TAG = "RegisterActivity";

    TextView loginRedirectText;
    Button signupButton;
    FirebaseAuth database;

   DatabaseReference reference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sighnup);

        signupFullName = findViewById(R.id.SignupFullName);
        signupEmail = findViewById(R.id.SignupEmail);
        signupDob = findViewById(R.id.SignupDateOfBirth);
        signupHeight = findViewById(R.id.SignupHeight);
        signupWeight = findViewById(R.id.SignupWeight);
        signupPassword = findViewById(R.id.SignupPassword);
        signupConfirmPassword = findViewById(R.id.SignupC_Password);

        radioGroupSignupGender = findViewById(R.id.radio_group_gender);
        radioGroupSignupGender.clearCheck();


        signupButton = findViewById(R.id.sighnup_button);
        loginRedirectText = findViewById(R.id.loginRedirectText);


        signupButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                int SelectGenderId = radioGroupSignupGender.getCheckedRadioButtonId();
                radioButtonSignupGender = findViewById(SelectGenderId);


                String textFullName = signupFullName.getText().toString();
                String textEmail = signupEmail.getText().toString();
                String textDob = signupDob.getText().toString();
                String textHeight = signupHeight.getText().toString();
                String textWeight = signupWeight.getText().toString();
                String textPassword = signupPassword.getText().toString();
                String textConfirmPassword = signupConfirmPassword.getText().toString();
                String textGender;



                if(TextUtils.isEmpty((textFullName))){
                    Toast.makeText(SignupActivity.this,"Please Enter Your Full Name ",Toast.LENGTH_LONG).show();
                    signupFullName.setError("Full name is required");
                    signupFullName.requestFocus();
                }
                else if(TextUtils.isEmpty((textEmail))){
                    Toast.makeText(SignupActivity.this,"Please Enter Your Email ",Toast.LENGTH_LONG).show();
                    signupEmail.setError("Email is required");
                    signupEmail.requestFocus();
                }
                else if(!Patterns.EMAIL_ADDRESS.matcher(textEmail).matches()){
                    Toast.makeText(SignupActivity.this,"Please re-Enter Your Email ",Toast.LENGTH_LONG).show();
                    signupEmail.setError("valid Email is required");
                    signupEmail.requestFocus();
                }
                else if(TextUtils.isEmpty((textDob))){
                    Toast.makeText(SignupActivity.this,"Please Enter Your Date of Birth ",Toast.LENGTH_LONG).show();
                    signupDob.setError("Date of Birth is required");
                    signupDob.requestFocus();
                }
                else if(radioGroupSignupGender.getCheckedRadioButtonId() == -1){
                    Toast.makeText(SignupActivity.this,"Please select your gender ",Toast.LENGTH_LONG).show();
                    radioButtonSignupGender.setError("Height is required");
                    radioButtonSignupGender.requestFocus();
                }
                else if(TextUtils.isEmpty((textHeight))){
                    Toast.makeText(SignupActivity.this,"Please Enter Your Height ",Toast.LENGTH_LONG).show();
                    signupHeight.setError("Height is required");
                    signupHeight.requestFocus();
                }
                else if(TextUtils.isEmpty((textWeight))){
                    Toast.makeText(SignupActivity.this,"Please Enter Your Weight ",Toast.LENGTH_LONG).show();
                    signupWeight.setError("Weight is required");
                    signupWeight.requestFocus();
                }
                else if(TextUtils.isEmpty((textPassword))){
                    Toast.makeText(SignupActivity.this,"Please Enter Your Password ",Toast.LENGTH_LONG).show();
                    signupPassword.setError("password is required");
                    signupPassword.requestFocus();
                }
                else if(textPassword.length() < 6){
                    Toast.makeText(SignupActivity.this,"Password should be at least 6 digit",Toast.LENGTH_LONG).show();
                    signupPassword.setError("password is too weak");
                    signupPassword.requestFocus();
                }
                else if(TextUtils.isEmpty((textConfirmPassword))){
                    Toast.makeText(SignupActivity.this,"Please re-Enter Your Password ",Toast.LENGTH_LONG).show();
                    signupConfirmPassword.setError("password is required");
                    signupConfirmPassword.requestFocus();
                }
                else if(!textConfirmPassword.equals(textPassword)){
                    Toast.makeText(SignupActivity.this,"Please Enter same password ",Toast.LENGTH_LONG).show();
                    signupConfirmPassword.setError("password must be same ");
                    signupConfirmPassword.requestFocus();
                    signupPassword.clearComposingText();
                    signupConfirmPassword.clearComposingText();
                }

                else {
                    textGender = radioButtonSignupGender.getText().toString();
                    signupuser(textFullName,textEmail,textDob,textGender,textHeight,textWeight,textPassword);
                }
            }
        });

        loginRedirectText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(SignupActivity.this, LoginActivity.class);
                startActivity(intent);
            }
        });

        signupDob.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Calendar calendar = Calendar.getInstance();
                int day = calendar.get(Calendar.DAY_OF_MONTH);
                int month = calendar.get(Calendar.MONTH);
                int year = calendar.get(Calendar.YEAR);

                picker = new DatePickerDialog(SignupActivity.this, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                        signupDob.setText(dayOfMonth + "/" + (month + 1) + "/" + year);

                    }
                },year,month,day);
                picker.show();

            }
        });
    }

    private void signupuser(String textFullName, String textEmail, String textDob, String textGender, String textHeight, String textWeight, String textPassword) {

        FirebaseAuth auth = FirebaseAuth.getInstance();
        auth.createUserWithEmailAndPassword(textEmail,textPassword).addOnCompleteListener(SignupActivity.this,
                new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(task.isSuccessful()){
                            Toast.makeText(SignupActivity.this, "User registered successfully", Toast.LENGTH_LONG).show();
                            FirebaseUser firebaseUser = auth.getCurrentUser();

                            UserProfileChangeRequest profileChangeRequest = new UserProfileChangeRequest.Builder().setDisplayName(textFullName).build();
                            firebaseUser.updateProfile(profileChangeRequest);

                            ReadWriteUserDetails writeUserDetails = new ReadWriteUserDetails(textDob,textGender,textHeight,textWeight);
                            DatabaseReference referenceProfile = FirebaseDatabase.getInstance().getReference("Registered Users");

                            referenceProfile.child(firebaseUser.getUid()).setValue(writeUserDetails).addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {

                                    if(task.isSuccessful()){
                                        firebaseUser.sendEmailVerification();
                                        Toast.makeText(SignupActivity.this, "User SignedUp Successfully, Please verify Your Email", Toast.LENGTH_LONG).show();

                                        Intent intent = new Intent(SignupActivity.this, DashboardActivity.class);
                                        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                                        startActivity(intent);
                                        finish();
                                    }
                                    else {
                                        Toast.makeText(SignupActivity.this, "SignUp Failed, Please Try again", Toast.LENGTH_LONG).show();


                                    }

                                }
                            });

                        }
                        else {
                            try {
                                throw task.getException();
                            } catch (FirebaseAuthWeakPasswordException e){
                                signupPassword.setError("your password is too weak");
                                signupPassword.requestFocus();

                            } catch (FirebaseAuthInvalidCredentialsException e){
                                signupEmail.setError("your email is invalid");
                                signupEmail.requestFocus();
                            } catch (FirebaseAuthUserCollisionException e){
                                signupEmail.setError("your already sign up with this email");
                                signupEmail.requestFocus();
                            } catch (Exception e){
                                Log.e(TAG, e.getMessage());
                                Toast.makeText(SignupActivity.this, e.getMessage(), Toast.LENGTH_LONG).show();
                            }
                        }

                    }
                });
    }
}