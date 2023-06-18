package everymoveapp.prototype;


import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.Date;

public class strength_plan_activity extends AppCompatActivity {

    EditText Count1, Count2, Count3, Count4, Count5, Count6, Count7;
    CheckBox checkBox1, checkBox2, checkBox3, checkBox4, checkBox5, checkBox6, checkBox7;
    Button increment1, increment2, increment3, increment4, increment5, increment6, increment7;
    Button decrement1, decrement2, decrement3, decrement4, decrement5, decrement6, decrement7;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_strength_plan);
        Intent intent = getIntent();
        String Username= intent.getStringExtra("Username");

        AlertDialog.Builder builder = new AlertDialog.Builder(strength_plan_activity.this);


        // 1 =============================================================

        checkBox1 = findViewById(R.id.checkBox1);
        Count1 = (EditText) findViewById(R.id.countC1);

        increment1 = (Button) findViewById(R.id.increment1);
        decrement1 = (Button) findViewById(R.id.decrement1);

        workoutMod(checkBox1, Count1, increment1, decrement1,"20");

        // 2 =============================================================

        checkBox2 = findViewById(R.id.checkBox2);
        Count2 = (EditText) findViewById(R.id.countC2);

        increment2 = (Button) findViewById(R.id.increment2);
        decrement2 = (Button) findViewById(R.id.decrement2);

        workoutMod(checkBox2, Count2, increment2, decrement2, "20");

        // 3 =============================================================

        checkBox3 = findViewById(R.id.checkBox3);
        Count3 = (EditText) findViewById(R.id.countC3);

        increment3 = (Button) findViewById(R.id.increment3);
        decrement3 = (Button) findViewById(R.id.decrement3);

        workoutMod(checkBox3, Count3, increment3, decrement3, "60");

        // 4 =============================================================


        checkBox4 = findViewById(R.id.checkBox4);
        Count4 = (EditText) findViewById(R.id.countC4);

        increment4 = (Button) findViewById(R.id.increment4);
        decrement4 = (Button) findViewById(R.id.decrement4);

        workoutMod(checkBox4, Count4, increment4, decrement4, "10");

        // 5 =============================================================


        checkBox5 = findViewById(R.id.checkBox5);
        Count5 = (EditText) findViewById(R.id.countC5);

        increment5 = (Button) findViewById(R.id.increment5);
        decrement5 = (Button) findViewById(R.id.decrement5);

        workoutMod(checkBox5, Count5, increment5, decrement5, "10");


        // 5 =============================================================


        checkBox6 = findViewById(R.id.checkBox6);
        Count6 = (EditText) findViewById(R.id.countC6);

        increment6 = (Button) findViewById(R.id.increment6);
        decrement6 = (Button) findViewById(R.id.decrement6);

        workoutMod(checkBox6, Count6, increment6, decrement6, "10");


// Score counting =============================================================

        Button addButton = (Button) findViewById(R.id.buttonAdd_plan_A);



        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

//                int total = ((int) (((((Integer.parseInt(Count1.getText().toString())/10.0)*100.0)+((Integer.parseInt(Count2.getText().toString())/30.0)*100.0)+((Integer.parseInt(Count3.getText().toString())/5.0)*100.0)+((Integer.parseInt(Count4.getText().toString())/5.0)*100.0)+((Integer.parseInt(Count5.getText().toString())/10.0)*100.0)+((Integer.parseInt(Count6.getText().toString())/500.0)*100.0)+((Integer.parseInt(Count7.getText().toString())/2.0)*100.0))/7)*0.05));

                double score1 = (Integer.parseInt(Count1.getText().toString())/20.0)*100.0;
                double score2 = (Integer.parseInt(Count2.getText().toString())/20.0)*100.0;
                double score3 = (Integer.parseInt(Count3.getText().toString())/60.0)*100.0;
                double score4 = (Integer.parseInt(Count4.getText().toString())/10.0)*100.0;
                double score5 = (Integer.parseInt(Count5.getText().toString())/10.0)*100.0;
                double score6 = (Integer.parseInt(Count5.getText().toString())/10.0)*100.0;

                int totalScore = (int) (((score1 + score2 + score3 + score4 + score5 + score6) / 600) * 5);

                if (totalScore == 0) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(strength_plan_activity.this);
                    builder.setTitle("Error");
                    builder.setMessage("Invalid Score... You must be get atleast 1...!");
                    builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            // Perform the desired action when the OK button is clicked
                            // This can be left empty if you just want to dismiss the dialog

                        }
                    });
                    builder.show();
                }else{
                    if (totalScore > 5){
                        totalScore = 5;
                    }
                    builder.setTitle("Scores");
                    builder.setMessage("Your Total Score is "+ totalScore);

                    // Set a positive button and its click listener
                    builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            // Perform the desired action when the OK button is clicked
                            // This can be left empty if you just want to dismiss the dialog
                            Intent i = new Intent(getApplicationContext(), AmharMainActivity.class);
                            startActivity(i);
                        }
                    });
                    builder.show();
                    addToDb(Username, totalScore);
                }

            }
        });



    }

    // FUNCTIONS =============================================================

    //    Add to Database (Aaroophan)=========================================================

    public void addToDb(String username,double totalScore){
        // Aarooopans code...!

        Date date = new Date();
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
        String Date = formatter.format(date);
        ;
        String[] DateParts = Date.split("-");
        int DateDay = Integer.parseInt(DateParts[2]);
        int DateMonth = Integer.parseInt(DateParts[1]);
        int DateYear = Integer.parseInt(DateParts[0]);

        String CurrentDate = DateYear + "-" + DateMonth + "-" + DateDay;

        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef3 = database.getReference("/HealthScore/" + username);
        myRef3.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                if (!snapshot.exists()) {
                    FirebaseDatabase database = FirebaseDatabase.getInstance();
                    DatabaseReference myRef = database.getReference("/HealthScore");
                    myRef.child(username).setValue("");
                }
            }

            @Override
            public void onCancelled(DatabaseError error) {
                Log.w("AAAAAAAAAAAAAA", "Failed to read value.", error.toException());
            }
        });

        DatabaseReference myRef2 = database.getReference("/HealthScore/" + username);

        myRef2.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    myRef2.child(CurrentDate).setValue(totalScore);
                    Log.d("AAAAAAAAAAAAAA", "INSERT VALUE");
                }
            }

            @Override
            public void onCancelled(DatabaseError error) {
                Log.w("AAAAAAAAAAAAAA", "Failed to read value.", error.toException());
            }
        });
    }

    // check minus value function =============================================================
    public void checkInvalidValue(int pCountInt, EditText Count){
        if (pCountInt < 0){
            AlertDialog.Builder builder = new AlertDialog.Builder(strength_plan_activity.this);
            builder.setTitle("Error");
            builder.setMessage("Invalid workout count!!!");
            builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    // Perform the desired action when the OK button is clicked
                    // This can be left empty if you just want to dismiss the dialog
                    Count.setText("0");
                }
            });
            builder.show();

        }
    }


    public void workoutMod(CheckBox checkBox, EditText count, Button increment, Button decrement,String target){

        count.setText("0");

        increment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int pCountInt = Integer.parseInt(count.getText().toString().trim());
                pCountInt++;
                String pCountStr = Integer.toString(pCountInt);
                count.setText(pCountStr);
                checkBox.setChecked(false);
            }
        });

        decrement.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int pCountInt = Integer.parseInt(count.getText().toString().trim());
                pCountInt--;
                String pCountStr = Integer.toString(pCountInt);
                count.setText(pCountStr);
                checkBox.setChecked(false);
                checkInvalidValue(pCountInt, count);
            }
        });
        checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    // Checkbox is checked
                    // Perform your logic here when the checkbox is clicked
                    count.setText(target);
                } else {
                    // Checkbox is not checked
                    // Perform your logic here when the checkbox is not clicked
//                    count.setText("0");
                }
            }
        });
    }
}