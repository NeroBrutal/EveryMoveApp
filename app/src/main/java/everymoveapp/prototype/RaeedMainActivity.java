package everymoveapp.prototype;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class RaeedMainActivity extends AppCompatActivity {

    Button buttonA, buttonB, buttonEdits, buttonC,buttonD, daily;







    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_raeed_main);

        buttonA = findViewById(R.id.btnA);
        buttonB =findViewById(R.id.btnB);
        buttonC = findViewById(R.id.btnC);
        buttonD =findViewById(R.id.btnD);


        buttonEdits= findViewById(R.id.EditsBtn);

        Intent intent = getIntent();
        String UserID = intent.getStringExtra("UserID");


        buttonA.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(getApplicationContext(), PlanA.class);
                intent.putExtra("UserID",UserID);
                startActivity(intent);

            }
        });

        buttonB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), PlanB.class);
                intent.putExtra("UserID",UserID);
                startActivity(intent);
            }
        });

        buttonEdits.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), editWorkouts.class);
                intent.putExtra("UserID",UserID);
                startActivity(intent);
            }
        });


        buttonC.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), PlanC.class);
                intent.putExtra("UserID",UserID);
                startActivity(intent);
            }
        });

        buttonD.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), PlanD.class);
                startActivity(intent);
            }
        });





    }
}