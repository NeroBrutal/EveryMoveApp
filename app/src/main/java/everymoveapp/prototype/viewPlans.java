package everymoveapp.prototype;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class viewPlans extends AppCompatActivity {

    Button btnDietPlanA,btnDietPlanB;

    TextView txtName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_plans);

        btnDietPlanA =findViewById(R.id.btnDietA);
        txtName = (TextView) findViewById(R.id.txtName2);

        btnDietPlanA.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String ID = txtName.getText().toString();
                Toast.makeText(viewPlans.this, "View Diet Plan : "+ID, Toast.LENGTH_SHORT).show();
                Log.d("AAAAAAAAAAAAAA", "                                                                Value "+ID);
                Intent intent = new Intent(getApplicationContext(), dietPlanA.class);
                intent.putExtra("Name", ID);
                startActivity(intent);
            }
        });

    }
}