package everymoveapp.prototype;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.content.Intent;
import android.os.Bundle;
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

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class ProgressOrSaved extends AppCompatActivity {

    CardView ViewProgressCard, SavedPostsCard;
    Button btnBack;
    private TextView UserName;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_progress_or_saved);

        Intent intent = getIntent();
        String Username= intent.getStringExtra("Username");

        btnBack = (Button) findViewById(R.id.btn_Save);

        ViewProgressCard = findViewById(R.id.ViewProgressCard);
        UserName = findViewById(R.id.UserName);
        UserName.setText("Welcome " + Username);



        ViewProgressCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ReportWrite(Username,"Navigated to viewProgress");
                Intent intent = new Intent(getApplicationContext(), ViewProgress.class);
                intent.putExtra("Username",Username);
                startActivity(intent);
                Toast.makeText(ProgressOrSaved.this, "View Progress", Toast.LENGTH_SHORT).show();
            }
        });

        SavedPostsCard = findViewById(R.id.SavedPostsCard);

        SavedPostsCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ReportWrite(Username,"Navigated to Saved Post");
                Intent intent = new Intent(getApplicationContext(), SavedPosts.class);
                intent.putExtra("Username",Username);
                startActivity(intent);
                Toast.makeText(ProgressOrSaved.this, "Saved Posts", Toast.LENGTH_SHORT).show();
            }
        });


        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), DashboardActivity.class);
                startActivity(intent);
                Toast.makeText(ProgressOrSaved.this, "Home", Toast.LENGTH_SHORT).show();
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