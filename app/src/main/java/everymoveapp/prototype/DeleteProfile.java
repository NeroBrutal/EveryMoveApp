package everymoveapp.prototype;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.EmailAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

public class DeleteProfile extends AppCompatActivity {

    private FirebaseAuth auth;
    private FirebaseUser firebaseUser;
    private EditText OldPassword;
    private TextView AuthenticateText;
    private Button AuthenticateBtn,DeleteBtn;
    private String userPwd;
    private static final String TAG = "DeleteProfileActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_delete_profile);

        OldPassword = findViewById(R.id.Edit_Password);
        AuthenticateBtn = findViewById(R.id.Authenticate_Button);
        DeleteBtn = findViewById(R.id.Edit_save_button);
        AuthenticateText = findViewById(R.id.AuthenticateText);

        DeleteBtn.setEnabled(false);


        auth = FirebaseAuth.getInstance();
        firebaseUser = auth.getCurrentUser();

        if(firebaseUser.equals("")){
            Toast.makeText(this, "Something went wrong...User Details are not available..", Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(DeleteProfile.this,Profile_Edit_Activity.class);
            startActivity(intent);
            finish();
        }else {
            reAuthenticate(firebaseUser);
        }
    }

    private void reAuthenticate(FirebaseUser firebaseUser) {
        AuthenticateBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                userPwd = OldPassword.getText().toString();

                if(TextUtils.isEmpty(userPwd)){
                    Toast.makeText(DeleteProfile.this, "Password is needed", Toast.LENGTH_SHORT).show();
                    OldPassword.setError("Please Enter the Password...");
                    OldPassword.requestFocus();
                }
                else {

                    AuthCredential credential = EmailAuthProvider.getCredential(firebaseUser.getEmail(),userPwd);

                    firebaseUser.reauthenticate(credential).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if(task.isSuccessful()){
                                OldPassword.setEnabled(false);
                                AuthenticateBtn.setEnabled(false);
                                DeleteBtn.setEnabled(true);

                                AuthenticateText.setText("You are Authenticated.  You can delete the profile...");
                                Toast.makeText(DeleteProfile.this, "be careful the action is irreversible", Toast.LENGTH_SHORT).show();


                                DeleteBtn.setBackgroundTintList(ContextCompat.getColorStateList(DeleteProfile.this,R.color.color_one));

                                DeleteBtn.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {

                                        ShowAlertDialog();
                                    }
                                });

                            }

                        }
                    });
                }

            }
        });
    }

    private void ShowAlertDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(DeleteProfile.this);
        builder.setTitle("Delete User and Related Data");
        builder.setMessage("Do you really want to Delete Profile and related Data ? ...This action irreversible.");

        builder.setPositiveButton("Continue", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                deleteUser(firebaseUser);

            }
        });

        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Intent intent = new Intent(DeleteProfile.this,Profile_Edit_Activity.class);
                startActivity(intent);
                finish();;
            }
        });

        AlertDialog alertDialog = builder.create();

        alertDialog.setOnShowListener(new DialogInterface.OnShowListener() {
            @Override
            public void onShow(DialogInterface dialog) {
                alertDialog.getButton(AlertDialog.BUTTON_POSITIVE).setTextColor(getResources().getColor(R.color.color_one));
            }
        });

        alertDialog.show();
    }

    private void deleteUser(FirebaseUser firebaseUser) {
        firebaseUser.delete().addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if(task.isSuccessful()){
                    deleteUserData();
                    auth.signOut();
                    Toast.makeText(DeleteProfile.this, "User has been deleted...", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(DeleteProfile.this, IntroActivity.class);
                    startActivity(intent);
                    finish();
                }
                else {
                    try {
                        throw task.getException();
                    }catch (Exception e){
                        Toast.makeText(DeleteProfile.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });
    }

    private void deleteUserData() {
        FirebaseStorage firebaseStorage = FirebaseStorage.getInstance();
        StorageReference reference = firebaseStorage.getReferenceFromUrl(firebaseUser.getPhotoUrl().toString());
        reference.delete().addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void unused) {
                Log.d(TAG, "OnSuccess: Photo Deleted.");
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.d(TAG, e.getMessage());
                Toast.makeText(DeleteProfile.this, e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });

        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("Registered Users");
        databaseReference.child(firebaseUser.getUid()).removeValue().addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void unused) {
                Log.d(TAG, "OnSuccess: User Data Deleted.");
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.d(TAG, e.getMessage());
                Toast.makeText(DeleteProfile.this, e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}