package everymoveapp.prototype;


import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
//import com.example.ppa_p_asv_1.RetrieveDataForProgress;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.eazegraph.lib.charts.BarChart;
import org.eazegraph.lib.models.BarModel;

public class SharePost extends AppCompatActivity {

    TextView txtCaption, txtDate;

    Button btnSave;
    AlertDialog.Builder Builder;

    String[] DayForDB;
    String[] Message;

    int ValueFromDB, L=0;

    String[] Dates;
    int BL=0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_share_post);

        BarChart barChart = (BarChart) findViewById(R.id.barchart);

        btnSave = (Button) findViewById(R.id.btn_Save);
        Builder = new AlertDialog.Builder(this);

        txtDate = (TextView) findViewById(R.id.tvDate);

        txtCaption = (TextView) findViewById(R.id.txtCaption);

        Intent intent = getIntent();

        DayForDB = new String[7];
        DayForDB[0] = "Day0";
        DayForDB[1] = "Day1";
        DayForDB[2] = "Day2";
        DayForDB[3] = "Day3";
        DayForDB[4] = "Day4";
        DayForDB[5] = "Day5";
        DayForDB[6] = "Day6";

        String User = intent.getStringExtra("Name");
        String Caption = intent.getStringExtra("Caption");

        for(L=0; L<7; L++) {
            ReadFromDB(User, intent.getStringExtra(DayForDB[L]), barChart, L);
        }

        txtDate.setText(intent.getStringExtra(DayForDB[6]));
        txtCaption.setText(Caption);

        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Builder.setTitle("Save")
                        .setMessage("Are you sure want to save?")
                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {

                                UpdatePostSavedPostsDB(User, intent.getStringExtra(DayForDB[6]));

                                Log.d("AAAAAAAAAAAAAA", "Value JUMBALAKKA JUMBALAKKA");

                                Toast.makeText(SharePost.this, "Sharing", Toast.LENGTH_SHORT).show();
                                Intent intentFromShare = new Intent(getApplicationContext(), SavedPosts.class);
                                startActivity(intentFromShare);

                            }
                        })
                        .setNegativeButton("No", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                Toast.makeText(SharePost.this, "You are still in Save Post", Toast.LENGTH_SHORT).show();
                            }
                        });
                AlertDialog alert = Builder.create();
                alert.show();
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();

    }

    public void ReadFromDB(String Username, String Day, BarChart barChart, int L){

        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference("/HealthScore/" + Username + "/" + Day + "");

        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    ValueFromDB = dataSnapshot.getValue(Integer.class);
                    Log.d("AAAAAAAAAAAAAA", "Value for "+Day+" is: " + ValueFromDB);
                    setBarChartData(barChart, Day, ValueFromDB, L);
                }
            }

            @Override
            public void onCancelled(DatabaseError error) {
                Log.w("AAAAAAAAAAAAAA", "Failed to read value.", error.toException());
            }
        });
    }
    public void setBarChartData(BarChart barChartname, String Day ,int V, int L){
        barChartname.addBar(new BarModel(Day, V, retColorVal(V)));
    }
    private int retColorVal(int Day){
        switch(Day){
            case 5: return 0xff66BB6A;
            case 4: return 0xff05af9b;
            case 3: return 0xffFFEB3B;
            case 2: return 0xffFF9800;
            case 1: return 0xffC10D00;
            default:return 0xff444444;
        }
    }

    public void UpdatePostSavedPostsDB(String Username, String Day){

        Intent intent = getIntent();
        String PostName = Username + Day;
        String Caption = txtCaption.getText().toString();

        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference("/SavedPosts");

        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    if (BL == 0) {
                        myRef.child(PostName).setValue("");
                        Log.d("AAAAAAAAAAAAAA", "Value for POST is: BLEH");
                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError error) {
                Log.w("AAAAAAAAAAAAAA", "Failed to read value.", error.toException());
            }
        });

        DatabaseReference myRef2 = database.getReference("/SavedPosts/" + PostName);

        myRef2.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    BL = 1;
                    myRef2.child("User").setValue(Username);
                    myRef2.child("Caption").setValue(Caption);
                    for(L=0; L<7; L++) {
                        myRef2.child(DayForDB[L]).setValue(intent.getStringExtra(DayForDB[L]));
                    }
                    Log.d("AAAAAAAAAAAAAA", "INSERT VALUE");
                }
            }

            @Override
            public void onCancelled(DatabaseError error) {
                Log.w("AAAAAAAAAAAAAA", "Failed to read value.", error.toException());
            }
        });
    }
}