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


import org.eazegraph.lib.charts.BarChart;
import org.eazegraph.lib.charts.PieChart;
import org.eazegraph.lib.models.BarModel;
import org.eazegraph.lib.models.PieModel;
import org.eazegraph.lib.charts.ValueLineChart;
import org.eazegraph.lib.models.ValueLinePoint;
import org.eazegraph.lib.models.ValueLineSeries;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class ViewProgress extends AppCompatActivity {

    TextView tvVeryGood, tvGood, tvNormal, tvBad, tvVeryBad, txtCaption, txtDate, txtWeek;
    PieChart pieChart;
    Button btnShare, btnShareBar, btnBack, btnPrev, btnNext;
    AlertDialog.Builder Builder;
    String[] Dates;
    String[] DayForDB;
    int ValueFromDB, L=0;
    int N_VG=0, N_G=0, N_N=0, N_B=0, N_VB=0;
    int Count=0, Count2=0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_progress);

        btnShare = (Button) findViewById(R.id.btn_view_progress);
        btnShareBar = (Button) findViewById(R.id.btn_share_bar);
        btnBack = (Button) findViewById(R.id.btn_Save);
        btnPrev = (Button) findViewById(R.id.btn_Prev);
        btnNext = (Button) findViewById(R.id.btn_Next);

        BarChart barChart = (BarChart) findViewById(R.id.barchart);
        ValueLineChart lineChart = (ValueLineChart) findViewById(R.id.linechart);
        ValueLineSeries series = new ValueLineSeries();
        series.setColor(0xff05af9b);

        Builder = new AlertDialog.Builder(this);

        txtCaption = (TextView) findViewById(R.id.txtCaption);

        txtWeek = (TextView) findViewById(R.id.txtWeek);
        txtDate = (TextView) findViewById(R.id.tvDate);

        tvVeryGood = (TextView) findViewById(R.id.tvVeryGood);
        tvGood = findViewById(R.id.tvGood);
        tvNormal = findViewById(R.id.tvNormal);
        tvBad = findViewById(R.id.tvBad);
        tvVeryBad = findViewById(R.id.tvVeryBad);

        String Username = "aaroophan";

        FirebaseDatabase database = FirebaseDatabase.getInstance();

        Date date = new Date();
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
        String Date = formatter.format(date);;
        String[] DateParts = Date.split("-");
        int DateDay = Integer.parseInt(DateParts[2]);
        int DateMonth = Integer.parseInt(DateParts[1]);
        int DateYear = Integer.parseInt(DateParts[0]);

        txtWeek.setText(DateYear+"-"+DateMonth+"-"+DateDay+" - Progress");
        txtDate.setText(DateYear+"-"+DateMonth+"-"+DateDay);

        Dates = new String[7];

        for(L=0; L<7; L++) {
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


        for(L=0; L<7; L++) {
            Log.d("AAAAAAAAAAAAAA", "                       DATE PARTS - "+Dates[L]);}

        DayForDB = new String[7];
        DayForDB[0] = "Day0";
        DayForDB[1] = "Day1";
        DayForDB[2] = "Day2";
        DayForDB[3] = "Day3";
        DayForDB[4] = "Day4";
        DayForDB[5] = "Day5";
        DayForDB[6] = "Day6";

        for(L=0; L<7; L++) {
            ReadFromDB(Username, Dates[L], barChart, L, series, lineChart, 1);
        }

        btnShare.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ReportWrite(Username,"Button shared clicked");
                Intent intent = new Intent(getApplicationContext(), SavedPosts.class);
                intent.putExtra("Username", Username);
                startActivity(intent);
                Toast.makeText(ViewProgress.this, "Saved Posts", Toast.LENGTH_SHORT).show();
            }
        });

        btnShareBar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ReportWrite(Username,"Button backed Click");
                AlertMsg(Username, "BAR", "SharePassBar");
            }
        });


        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Builder.setTitle("Back")
                        .setMessage("Do you want to go back?")
                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                ReportWrite(Username,"Button Backed Click");
                                Toast.makeText(ViewProgress.this, "Going back", Toast.LENGTH_SHORT).show();
                                Intent intent = new Intent(getApplicationContext(), ProgressOrSaved.class);
                                intent.putExtra("Username", Username);
                                startActivity(intent);
                            }
                        })
                        .setNegativeButton("No", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                Toast.makeText(ViewProgress.this, "You are still in Saved Posts", Toast.LENGTH_SHORT).show();
                            }
                        });
                AlertDialog alert = Builder.create();
                alert.show();
            }
        });

        btnPrev.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ReportWrite(Username,"Button Previous clicked");
                barChart.clearChart();
                String Prev3 = Dates[5];

                DateClacPrev(Prev3, Username, barChart, series, lineChart);
            }
        });

        btnNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ReportWrite(Username,"Button Next clicked");
                barChart.clearChart();
                String Prev4 = Dates[1];

                DateClacNext(Prev4, Username, barChart, series, lineChart);

            }
        });

    }

    @Override
    protected void onResume() {
        super.onResume();

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

    private void AlertMsg(String Username, String ToBeSent, String Pass){
        Builder.setTitle("Save")
                .setMessage("Do you want to save?")
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
                        Toast.makeText(ViewProgress.this, "Going to Save Post", Toast.LENGTH_SHORT).show();
                    }
                })
                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        Toast.makeText(ViewProgress.this, "Saving Cancelled", Toast.LENGTH_SHORT).show();
                    }
                });
        AlertDialog alert = Builder.create();
        alert.show();
    }

    public void ReadFromDB(String Username, String Day, BarChart barChart, int L, ValueLineSeries series, ValueLineChart lineChart, int NP){

        // Write a message to the database
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference("/HealthScore/" + Username + "/" + Day + "");

        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    ValueFromDB = dataSnapshot.getValue(Integer.class);
                    Log.d("AAAAAAAAAAAAAA", "Value for " + Day + " is: " + ValueFromDB);
                    setBarChartData(barChart, Day, ValueFromDB, L);
                    if(NP == 1) {
                        setLineChartData(series, lineChart, Day, ValueFromDB);
                        setPieChartData(Day, ValueFromDB, L);
                    }
                    if (L == 6) {
                        pieChart.startAnimation();
                        Log.d("AAAAAAAAAAAAAA", "LINE");
                    }
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
    private void setLineChartData(ValueLineSeries series, ValueLineChart lineChart, String Day, int V){
        series.addPoint(new ValueLinePoint(Day, V));
        Count2 = Count + 1;
        if(Count2==7){

            lineChart.addSeries(series);
        }
        Log.d("AAAAAAAAAAAAAA", "Value for LINE "+Day+" is: " + V);
    }

    private void setPieChartData(String Day ,int V, int L){

        if(V == 1){N_VB = N_VB + 1;}
        else if(V==2){N_B = N_B + 1;}
        else if(V==3){N_N = N_N + 1;}
        else if(V==4){N_G = N_G + 1;}
        else if(V==5){N_VG = N_VG + 1;}
        else{N_VB = 0;}


        pieChart = findViewById(R.id.piechart);

        tvVeryGood.setText(Integer.toString(N_VB*100/7));
        tvGood.setText(Integer.toString(N_B*100/7));
        tvNormal.setText(Integer.toString(N_N*100/7));
        tvBad.setText(Integer.toString(N_G*100/7));
        tvVeryBad.setText(Integer.toString(N_VG*100/7));

        String PVG = tvVeryGood.getText().toString();
        Log.d("AAAAAAAAAAAAAA", "PVG FROM GETTEXT "+PVG);
        String PG = tvGood.getText().toString();
        Log.d("AAAAAAAAAAAAAA", "PG FROM GETTEXT "+PG);
        String PN = tvNormal.getText().toString();
        Log.d("AAAAAAAAAAAAAA", "PN FROM GETTEXT "+PN);
        String PB = tvBad.getText().toString();
        Log.d("AAAAAAAAAAAAAA", "PB FROM GETTEXT "+PB);
        String PVB = tvVeryBad.getText().toString();
        Log.d("AAAAAAAAAAAAAA", "PVB FROM GETTEXT "+PVB);

        Count = Count +1;
        if(Count==7) {

            pieChart = findViewById(R.id.piechart);

            Log.d("AAAAAAAAAAAAAA", "V = " + V + " : N_VG = " + PVG);
            pieChart.addPieSlice(new PieModel("Very Good", Float.parseFloat(PVG), retColorVal(5)));

            Log.d("AAAAAAAAAAAAAA", "V = " + V + " : N_G = " + PG);
            pieChart.addPieSlice(new PieModel("Good", Float.parseFloat(PG), retColorVal(4)));

            Log.d("AAAAAAAAAAAAAA", "V = " + V + " : N_N = " + PN);
            pieChart.addPieSlice(new PieModel("Normal", Float.parseFloat(PN), retColorVal(3)));

            Log.d("AAAAAAAAAAAAAA", "V = " + V + " : N_B = " + PB);
            pieChart.addPieSlice(new PieModel("Bad", Float.parseFloat(PB), retColorVal(2)));

            Log.d("AAAAAAAAAAAAAA", "V = " + V + " : N_VB = " + PVB);
            pieChart.addPieSlice(new PieModel("Very Bad", Float.parseFloat(PVB), retColorVal(1)));
        }
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
                Dates[6-L] = DateYear+"-"+(DateMonth+1)+"-"+1;
            }
            else if(DateDay+(6-L) == 33){
                Dates[6-L] = DateYear+"-"+(DateMonth+1)+"-"+2;
            }
            else if(DateDay+(6-L) == 34){
                Dates[6-L] = DateYear+"-"+(DateMonth+1)+"-"+3;
            }
            else if(DateDay+(6-L) == 35){
                Dates[6-L] = DateYear+"-"+(DateMonth+1)+"-"+4;
            }
            else if(DateDay+(6-L) == 36){
                Dates[6-L] = DateYear+"-"+(DateMonth+1)+"-"+5;
            }
            else if(DateDay+(6-L) == 37){
                Dates[6-L] = DateYear+"-"+(DateMonth+1)+"-"+6;
            }
            else {
                Dates[6-L] = DateYear + "-" + DateMonth + "-" + (DateDay + (6 - L));
            }
        }

        for(int L=0; L<7; L++) {
            Log.d("AAAAAAAAAAAAAA", "                       DATE PARTS - "+Dates[L]);}
    }

    public void DateClacPrev(String Prev, String Username, BarChart barChart, ValueLineSeries series, ValueLineChart lineChart) {

        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRefCaption = database.getReference("/HealthScore/" + Username +"/"+ Prev);

        Log.d("AAAAAAAAAAAAAA", "                                             Value for myRefCaption      " + myRefCaption);

        String[] TempDateParts = Prev.split("-");
        int TempDateDay = Integer.parseInt(TempDateParts[2]);
        int TempDateMonth = Integer.parseInt(TempDateParts[1]);
        int TempDateYear = Integer.parseInt(TempDateParts[0]);

        myRefCaption.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    int ValueFromDB_S = dataSnapshot.getValue(Integer.class);
                    Log.d("AAAAAAAAAAAAAA", "                                    Value for PREV " + /*Prev +*/ " in SAVEDPOSTS is: " + ValueFromDB_S);
                    AddDates(TempDateDay, TempDateMonth, TempDateYear);
                    ReadFromDB(Username, Dates[0], barChart, 0, series, lineChart, 0);
                    ReadFromDB(Username, Dates[1], barChart, 0, series, lineChart, 0);
                    ReadFromDB(Username, Dates[2], barChart, 0, series, lineChart, 0);
                    ReadFromDB(Username, Dates[3], barChart, 0, series, lineChart, 0);
                    ReadFromDB(Username, Dates[4], barChart, 0, series, lineChart, 0);
                    ReadFromDB(Username, Dates[5], barChart, 0, series, lineChart, 0);
                    ReadFromDB(Username, Dates[6], barChart, 0, series, lineChart, 0);

                    txtDate.setText(Dates[6]);
                }
            }

            @Override
            public void onCancelled(DatabaseError error) {
                Log.w("AAAAAAAAAAAAAA", "Failed to read value.", error.toException());
            }
        });
    }


    public void DateClacNext(String Prev, String Username, BarChart barChart, ValueLineSeries series, ValueLineChart lineChart) {

        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRefCaption = database.getReference("/HealthScore/" + Username +"/"+ Prev);

        Log.d("AAAAAAAAAAAAAA", "                                             Value for myRefCaption      " + myRefCaption);

        String[] TempDateParts = Prev.split("-");
        int TempDateDay = Integer.parseInt(TempDateParts[2]);
        int TempDateMonth = Integer.parseInt(TempDateParts[1]);
        int TempDateYear = Integer.parseInt(TempDateParts[0]);

        myRefCaption.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    int ValueFromDB_S = dataSnapshot.getValue(Integer.class);
                    Log.d("AAAAAAAAAAAAAA", "                                    Value for PREV " + /*Prev +*/ " in SAVEDPOSTS is: " + ValueFromDB_S);
                    RedDates(TempDateDay, TempDateMonth, TempDateYear);
                    ReadFromDB(Username, Dates[0], barChart, 0, series, lineChart, 0);
                    ReadFromDB(Username, Dates[1], barChart, 0, series, lineChart, 0);
                    ReadFromDB(Username, Dates[2], barChart, 0, series, lineChart, 0);
                    ReadFromDB(Username, Dates[3], barChart, 0, series, lineChart, 0);
                    ReadFromDB(Username, Dates[4], barChart, 0, series, lineChart, 0);
                    ReadFromDB(Username, Dates[5], barChart, 0, series, lineChart, 0);
                    ReadFromDB(Username, Dates[6], barChart, 0, series, lineChart, 0);

                    txtDate.setText(Dates[6]);
                }
            }

            @Override
            public void onCancelled(DatabaseError error) {
                Log.w("AAAAAAAAAAAAAA", "Failed to read value.", error.toException());
            }
        });
    }

    public void ReportWrite(String Username, String Entry){

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
        DatabaseReference myRef = database.getReference("/Report/ViewProgress");

        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    myRef.child(Time).setValue(Username+" : "+Entry);
                }
            }
            @Override
            public void onCancelled(DatabaseError error) {
                Log.w("AAAAAAAAAAAAAA", "Failed to read value.", error.toException());
            }
        });
    }


}