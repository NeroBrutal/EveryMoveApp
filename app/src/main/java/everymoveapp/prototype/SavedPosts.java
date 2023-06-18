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

import java.text.SimpleDateFormat;
import java.util.Date;

public class SavedPosts extends AppCompatActivity {

    TextView txtDate, txtCaption;
    Button btnSave, btnPrev, btnNext, btnDelete, btnEdit, btnProgress;
    AlertDialog.Builder Builder, Builder2;
    int ValueFromDB, L=0;
    String[] DayForDB;

    int T;
    String[] Dates;
    String Prev;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_saved_posts);


        BarChart barChart = (BarChart) findViewById(R.id.barchart);

        btnSave = (Button) findViewById(R.id.btn_Save);
        btnPrev = (Button) findViewById(R.id.btn_Prev);
        btnNext = (Button) findViewById(R.id.btn_Next);
        btnDelete = (Button) findViewById(R.id.btnDelete);
        btnEdit = (Button) findViewById(R.id.btnEdit);
        btnProgress = (Button) findViewById(R.id.btn_view_progress);

        Builder = new AlertDialog.Builder(this);
        Builder2 = new AlertDialog.Builder(this);

        txtDate = (TextView) findViewById(R.id.tvDate);
        txtCaption = (TextView) findViewById(R.id.txtCaption);


        DayForDB = new String[7];
        DayForDB[0] = "Day0";
        DayForDB[1] = "Day1";
        DayForDB[2] = "Day2";
        DayForDB[3] = "Day3";
        DayForDB[4] = "Day4";
        DayForDB[5] = "Day5";
        DayForDB[6] = "Day6";

        Intent intent = getIntent();
        String Username = intent.getStringExtra("Username");

        Date date = new Date();
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
        String Date = formatter.format(date);;

        String[] DateParts = Date.split("-");
        int DateDay = Integer.parseInt(DateParts[2]);
        int DateMonth = Integer.parseInt(DateParts[1]);
        int DateYear = Integer.parseInt(DateParts[0]);

        Prev = DateYear +"-"+ DateMonth +"-"+ DateDay;
        DateClacPrev(Prev, Username, barChart);

        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Builder.setTitle("Back")
                        .setMessage("Do you want to go back?")
                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                Toast.makeText(SavedPosts.this, "Going back", Toast.LENGTH_SHORT).show();
                                Intent intent = new Intent(getApplicationContext(), ProgressOrSaved.class);
                                intent.putExtra("Username",Username);
                                startActivity(intent);
                            }
                        })
                        .setNegativeButton("No", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                Toast.makeText(SavedPosts.this, "You are still in Saved Posts", Toast.LENGTH_SHORT).show();
                            }
                        });
                AlertDialog alert = Builder.create();
                alert.show();
            }
        });

        btnPrev.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                barChart.clearChart();
                String Prev3 = Dates[5];
                DateClacPrev(Prev3, Username, barChart);
            }
        });

        btnNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                barChart.clearChart();
                String Prev4 = Dates[5];
                DateClacNext(Prev4, Username, barChart);

            }
        });


        btnDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Builder.setTitle("Delete")
                        .setMessage("Do you want to delete?")
                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {

                                deleteFromDB(Username, Dates[6]);

                                Toast.makeText(SavedPosts.this, "Deleted", Toast.LENGTH_SHORT).show();
                                Intent intent = new Intent(getApplicationContext(), ViewProgress.class);
                                startActivity(intent);
                            }
                        })
                        .setNegativeButton("No", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                Toast.makeText(SavedPosts.this, "You are still in Saved Posts", Toast.LENGTH_SHORT).show();
                            }
                        });
                AlertDialog alert = Builder.create();
                alert.show();
            }
        });

        btnEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                AlertMsg(Username, "BAR", "SharePassBar");
            }
        });


        btnProgress.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), ViewProgress.class);
                intent.putExtra("Username",Username);
                startActivity(intent);
                Toast.makeText(SavedPosts.this, "View Progress", Toast.LENGTH_SHORT).show();
            }
        });

    }


    public void ReadFromDB(String Username, String Day, BarChart barChart, int L){

        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRefCaption = database.getReference("/SavedPosts/" + Username+Day+"/Caption");

        myRefCaption.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    String ValueFromDB_S = dataSnapshot.getValue(String.class);
                    Log.d("AAAAAAAAAAAAAA", "Value for " + Day + " in SAVEDPOSTS is: " + ValueFromDB_S);
                    txtCaption.setText(ValueFromDB_S);
                }
                else{
                    Toast.makeText(SavedPosts.this, "Going back to progress", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(getApplicationContext(), ViewProgress.class);
                    startActivity(intent);
                }
            }

            @Override
            public void onCancelled(DatabaseError error) {
                Log.w("AAAAAAAAAAAAAA", "Failed to read value.", error.toException());
            }
        });

        DatabaseReference myRefUser = database.getReference("/SavedPosts/" + Username+Day+"/Day6");

        Log.d("AAAAAAAAAAAAAA", "Value for DAY in ReadFromDB: " + Day);

        myRefUser.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    String ValueFromDB_S = dataSnapshot.getValue(String.class);
                    Log.d("AAAAAAAAAAAAAA", "Value for " + Day + " in SAVEDPOSTS is: " + ValueFromDB_S);
                    txtDate.setText(ValueFromDB_S);
                }
                else{
                    Toast.makeText(SavedPosts.this, "Going back to progress", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(getApplicationContext(), ViewProgress.class);
                    startActivity(intent);
                }
            }

            @Override
            public void onCancelled(DatabaseError error) {
                Log.w("AAAAAAAAAAAAAA", "Failed to read value.", error.toException());
            }
        });

        ReadFromDBforDAY(Username, Day, barChart, L, "Day0");
        ReadFromDBforDAY(Username, Day, barChart, L, "Day1");
        ReadFromDBforDAY(Username, Day, barChart, L, "Day2");
        ReadFromDBforDAY(Username, Day, barChart, L, "Day3");
        ReadFromDBforDAY(Username, Day, barChart, L, "Day4");
        ReadFromDBforDAY(Username, Day, barChart, L, "Day5");
        ReadFromDBforDAY(Username, Day, barChart, L, "Day6");

    }
    public void setBarChartData(BarChart barChartname, String Day ,int V, int L){
        barChartname.addBar(new BarModel(Day, V, retColorVal(V)));
    }

    public void ReadFromDB_HS(String Username, String Day, BarChart barChart, int L){

        // Write a message to the database
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference("/HealthScore/" + Username + "/" + Day + "");
        Log.d("AAAAAAAAAAAAAA", "MYREF     " + myRef);

        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    ValueFromDB = dataSnapshot.getValue(Integer.class);
                    Log.d("AAAAAAAAAAAAAA", "Value for " + Day + " is: " + ValueFromDB);
                    setBarChartData(barChart, Day, ValueFromDB, L);
                }
            }

            @Override
            public void onCancelled(DatabaseError error) {
                Log.w("AAAAAAAAAAAAAA", "Failed to read value.", error.toException());
            }
        });
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

    public void ReadFromDBforDAY(String Username, String Day, BarChart barChart, int L, String DayofDB)
    {
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRefDay = database.getReference("/SavedPosts/" + Username + Day + "/"+DayofDB+"");

        myRefDay.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    String ValueFromDB_S = dataSnapshot.getValue(String.class);
                    Log.d("AAAAAAAAAAAAAA", "Value for " + Day + " in SAVEDPOSTS is: " + ValueFromDB_S);

                    ReadFromDB_HS(Username, ValueFromDB_S, barChart, L);
                } else {
                    Toast.makeText(SavedPosts.this, "Going back to progress", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(getApplicationContext(), ViewProgress.class);
                    startActivity(intent);
                }
            }

            @Override
            public void onCancelled(DatabaseError error) {
                Log.w("AAAAAAAAAAAAAA", "Failed to read value.", error.toException());
            }
        });
    }

    public void deleteFromDB(String Username, String Day){
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference("/SavedPosts").child(Username+Day);
        myRef.removeValue();
        Log.d("AAAAAAAAAAAAAA", "                                                                Value     XXXXXXXXXXXXXXXXXXXXXXX");
    }

    public void AddDates(int DateDay, int DateMonth, int DateYear){

        Dates = new String[7];
        for(int L=0; L<7; L++) {
            if(DateDay-(6-L) == 0){
                Dates[L] = (DateYear)+"-"+(DateMonth-1)+"-"+31;
            }
            else if(DateDay-(6-L) == -1){
                Dates[L] = DateYear+"-"+(DateMonth-1)+"-"+30;
            }
            else if(DateDay-(6-L) == -2){
                Dates[L] = DateYear+"-"+(DateMonth-1)+"-"+29;
            }
            else if(DateDay-(6-L) == -3){
                Dates[L] = DateYear+"-"+(DateMonth-1)+"-"+28;
            }
            else if(DateDay-(6-L) == -4){
                Dates[L] = DateYear+"-"+(DateMonth-1)+"-"+27;
            }
            else if(DateDay-(6-L) == -5){
                Dates[L] = DateYear+"-"+(DateMonth-1)+"-"+26;
            }
            else {
                Dates[L] = DateYear + "-" + DateMonth + "-" + (DateDay - (6 - L));
            }
        }

        for(int L=0; L<7; L++) {
            Log.d("AAAAAAAAAAAAAA", "                       DATE PARTS - "+Dates[L]);}
    }

    public void RedDates(int DateDay, int DateMonth, int DateYear){

        Dates = new String[7];
        for(int L=0; L<7; L++) {
            if(DateDay+(6-L) == 32){
                Dates[L] = DateYear+"-"+(DateMonth+1)+"-"+1;
            }
            else if(DateDay+(6-L) == 33){
                Dates[L] = DateYear+"-"+(DateMonth+1)+"-"+2;
            }
            else if(DateDay+(6-L) == 34){
                Dates[L] = DateYear+"-"+(DateMonth+1)+"-"+3;
            }
            else if(DateDay+(6-L) == 35){
                Dates[L] = DateYear+"-"+(DateMonth+1)+"-"+4;
            }
            else if(DateDay+(6-L) == 36){
                Dates[L] = DateYear+"-"+(DateMonth+1)+"-"+5;
            }
            else if(DateDay+(6-L) == 37){
                Dates[L] = DateYear+"-"+(DateMonth+1)+"-"+6;
            }
            else {
                Dates[L] = DateYear + "-" + DateMonth + "-" + (DateDay + (6 - L));
            }
        }

        for(int L=0; L<7; L++) {
            Log.d("AAAAAAAAAAAAAA", "                       DATE PARTS - "+Dates[L]);}
    }


    public void DateClacPrev(String Prev, String Username, BarChart barChart) {

        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRefCaption = database.getReference("/SavedPosts/" + Username + Prev + "/Caption");

        Log.d("AAAAAAAAAAAAAA", "                                             Value for myRefCaption      " + myRefCaption);

        String[] TempDateParts = Prev.split("-");
        int TempDateDay = Integer.parseInt(TempDateParts[2]);
        int TempDateMonth = Integer.parseInt(TempDateParts[1]);
        int TempDateYear = Integer.parseInt(TempDateParts[0]);

        myRefCaption.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    String ValueFromDB_S = dataSnapshot.getValue(String.class);
                    Log.d("AAAAAAAAAAAAAA", "                                    Value for PREV " + /*Prev +*/ " in SAVEDPOSTS is: " + ValueFromDB_S);
                    AddDates(TempDateDay, TempDateMonth, TempDateYear);
                    ReadFromDB(Username, Dates[6], barChart, 0);
                }
                else{
                    String Prev2;
                    if (TempDateMonth > 0) {
                        if (TempDateDay - 1 == 0) {
                            Prev2 = TempDateYear + "-" + (TempDateMonth - 1) + "-" + 31;
                        } else if (TempDateDay - 1 == -1) {
                            Prev2 = TempDateYear + "-" + (TempDateMonth - 1) + "-" + 30;
                        } else if (TempDateDay - 1 == -2) {
                            Prev2 = TempDateYear + "-" + (TempDateMonth - 1) + "-" + 29;
                        } else if (TempDateDay - 1 == -3) {
                            Prev2 = TempDateYear + "-" + (TempDateMonth - 1) + "-" + 28;
                        } else if (TempDateDay - 1 == -4) {
                            Prev2 = TempDateYear + "-" + (TempDateMonth - 1) + "-" + 27;
                        } else if (TempDateDay - 1 == -5) {
                            Prev2 = TempDateYear + "-" + (TempDateMonth - 1) + "-" + 26;
                        } else {
                            Prev2 = TempDateYear + "-" + TempDateMonth + "-" + (TempDateDay - 1);
                            Log.d("AAAAAAAAAAAAAA", "                                    Value for ELSE " + Prev2);
                        }

                        DateClacPrev(Prev2, Username, barChart);

                    } else {
                        T = 1;
                    }
                    Log.d("AAAAAAAAAAAAAA", "                                    Value for T " + T);
                }
            }

            @Override
            public void onCancelled(DatabaseError error) {
                Log.w("AAAAAAAAAAAAAA", "Failed to read value.", error.toException());
            }
        });
    }

    public void DateClacNext(String Prev, String Username, BarChart barChart) {

        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRefCaption = database.getReference("/SavedPosts/" + Username + Prev + "/Caption");

        Log.d("AAAAAAAAAAAAAA", "                                             Value for myRefCaption      " + myRefCaption);

        String[] TempDateParts = Prev.split("-");
        int TempDateDay = Integer.parseInt(TempDateParts[2]);
        int TempDateMonth = Integer.parseInt(TempDateParts[1]);
        int TempDateYear = Integer.parseInt(TempDateParts[0]);

        myRefCaption.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    String ValueFromDB_S = dataSnapshot.getValue(String.class);
                    Log.d("AAAAAAAAAAAAAA", "                                    Value for PREV " + /*Prev +*/ " in SAVEDPOSTS is: " + ValueFromDB_S);
                    RedDates(TempDateDay, TempDateMonth, TempDateYear);
                    ReadFromDB(Username, Dates[6], barChart, 0);
                }
                else{
                    String Prev2;

                    if (TempDateMonth < 13) {
                        if (TempDateDay + 1 == 32) {
                            Prev2 = TempDateYear + "-" + (TempDateMonth + 1) + "-" + 1;
                        } else if (TempDateDay + 1 == 33) {
                            Prev2 = TempDateYear + "-" + (TempDateMonth + 1) + "-" + 2;
                        } else if (TempDateDay + 1 == 34) {
                            Prev2 = TempDateYear + "-" + (TempDateMonth + 1) + "-" + 3;
                        } else if (TempDateDay + 1 == 35) {
                            Prev2 = TempDateYear + "-" + (TempDateMonth + 1) + "-" + 4;
                        } else if (TempDateDay + 1 == 36) {
                            Prev2 = TempDateYear + "-" + (TempDateMonth + 1) + "-" + 5;
                        } else if (TempDateDay + 1 == 37) {
                            Prev2 = TempDateYear + "-" + (TempDateMonth + 1) + "-" + 6;
                        } else {
                            Prev2 = TempDateYear + "-" + TempDateMonth + "-" + (TempDateDay + 1);
                            Log.d("AAAAAAAAAAAAAA", "                                    Value for ELSE " + Prev2);
                        }

                        DateClacNext(Prev2, Username, barChart);

                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError error) {
                Log.w("AAAAAAAAAAAAAA", "Failed to read value.", error.toException());
            }
        });
    }

    private void AlertMsg(String Username, String ToBeSent, String Pass){
        Builder.setTitle("Edit")
                .setMessage("Do you want to edit?")
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                        Intent intent = new Intent(getApplicationContext(), SharePost.class);
                        for(L=0; L<7; L++) {
                            intent.putExtra(DayForDB[L], Dates[L]);
                        }
                        intent.putExtra("Name", Username);
                        intent.putExtra("Caption", txtCaption.getText());
                        startActivity(intent);
                        Toast.makeText(SavedPosts.this, "Going to Edit Post", Toast.LENGTH_SHORT).show();
                    }
                })
                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        Toast.makeText(SavedPosts.this, "Editing Cancelled", Toast.LENGTH_SHORT).show();
                    }
                });
        AlertDialog alert = Builder.create();
        alert.show();
    }
}