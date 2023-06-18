package everymoveapp.prototype;


import androidx.appcompat.app.AppCompatActivity;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class AmharMainActivity extends AppCompatActivity {
    private Button inputBtn;
    private TextView message,UserName;
    private String keyO;
    private String valueO;
    private String searchUser;
    public Dialog loadingDialog;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_amhar_main);
        Intent intent = getIntent();
        String Username= intent.getStringExtra("Username");
        UserName = findViewById(R.id.UserName);
        UserName.setText("Welcome " + Username);



        searchUser = "testID3"; // Select a userID to navigate to their workout plan

        FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
        DatabaseReference databaseReference = firebaseDatabase.getReference("Personal Plans").child(searchUser);

        //set the variables of buttons
        inputBtn = (Button) findViewById(R.id.buttonInput);
        message = (TextView) findViewById(R.id.textMessage);



//        // Loading dialog view
//        loadingDialog = createLoadingDialog();
//        loadingDialog.show();
//        loadingDialog.dismiss();


        loadDialog(10);
        databaseReference.addValueEventListener(new ValueEventListener() {

            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // This method is called when the data is changed or fetched for the first time
                // Retrieve the data from the snapshot


                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    // Access individual child nodes
                    String key = snapshot.getKey();
                    String value = snapshot.getValue(String.class);

                    // Do something with the data
//                    Toast.makeText(MainActivity.this, key +" : "+ value, Toast.LENGTH_LONG).show();
                    if (key.equals("planName")) {
                        keyO = key;
                        valueO = value;

                        // Use the value associated with the key
                        message.setText("Your Workout Plan is : " + value);
                        loadingDialog.dismiss();
//                        Toast.makeText(MainActivity.this, key +" : "+ value, Toast.LENGTH_LONG).show();
                        break; // Exit the loop if the key is found

                    }
                    Log.d("FirebaseData", "Value: " + value);
                }
            }

            @Override
            public void onCancelled(DatabaseError error) {
                // This method is called if the retrieval is canceled or fails
                Toast.makeText(AmharMainActivity.this, "Failed to retrieve data: " + error.getMessage(), Toast.LENGTH_LONG).show();
                Log.e("FirebaseData", "Failed to retrieve data: " + error.getMessage());
            }
        });

//        // Perform the search query
//        databaseReference.orderByChild("Beginners Plan").equalTo("Beginners Plan").addListenerForSingleValueEvent(new ValueEventListener() {
//            @Override
//            public void onDataChange(DataSnapshot dataSnapshot) {
//                // Data is retrieved successfully
//                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
//                    // Process each retrieved data item
//                    String data = snapshot.getValue(String.class);
//                    // Perform actions with the retrieved data
//                    // ...
//                    Toast.makeText(MainActivity.this, data, Toast.LENGTH_LONG).show();
//
//                }
//            }
//
//            @Override
//            public void onCancelled(DatabaseError databaseError) {
//                // Error occurred while retrieving data
//                // Handle the error
//                Toast.makeText(MainActivity.this, "Error in Database", Toast.LENGTH_LONG).show();
//            }
//        });

        // Input Button function
        inputBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Loading dialog view
                loadingDialog = createLoadingDialog();
                loadingDialog.show();

                if(keyO.equals("planName")){
                    if(valueO.trim().equals("Balanced")){ // Balanced

                        Intent i = new Intent(AmharMainActivity.this, balanced_plan_activity.class);
                        Toast.makeText(getApplicationContext(), keyO +" : "+ valueO, Toast.LENGTH_LONG).show();
                        startActivity(i);
                        loadingDialog.dismiss();

                    }else if(valueO.trim().equals("Aerobics")){ // Aerobics

                        Intent i = new Intent(AmharMainActivity.this, aerobics_plan_activity.class);
                        Toast.makeText(getApplicationContext(), keyO +" : "+ valueO, Toast.LENGTH_LONG).show();
                        startActivity(i);
                        loadingDialog.dismiss();

                    }else if(valueO.trim().equals("Strength")){ // Strength

                        Intent i = new Intent(AmharMainActivity.this, strength_plan_activity.class);
                        Toast.makeText(getApplicationContext(), keyO +" : "+ valueO, Toast.LENGTH_LONG).show();
                        startActivity(i);
                        loadingDialog.dismiss();

                    }else if(valueO.trim().equals("core workouts")){ // core workouts

                        Intent i = new Intent(AmharMainActivity.this, core_workout_plan_activity.class);
                        Toast.makeText(getApplicationContext(), keyO +" : "+ valueO, Toast.LENGTH_LONG).show();
                        startActivity(i);
                        loadingDialog.dismiss();

                    }
                }

            }
        });

    }


    //================================FUNCTIONS==========================================

    private Dialog createLoadingDialog() {
        Dialog dialog = new Dialog(this);
        dialog.setContentView(R.layout.dialog_loading);
        dialog.setCancelable(false);
        dialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        return dialog;
    }

    private void loadDialog(int delay){
        // Loading dialog view
        loadingDialog = createLoadingDialog();
        loadingDialog.show();
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                // Hide the loading dialog
                loadingDialog.dismiss();

                // Perform any other task after the loading is complete
                // For example, display a Toast message -> Loading Complete
//                Toast.makeText(MainActivity.this, "Database is connected", Toast.LENGTH_SHORT).show();
            }
        }, delay*1000); // Simulating a 2-second delay
    }
}