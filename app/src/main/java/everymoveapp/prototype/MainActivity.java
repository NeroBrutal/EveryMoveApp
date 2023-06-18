package everymoveapp.prototype;


import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {
    private String selectedAgeCategory = "";
    Button createPlansbtn, btnViewPlans;
    TextView txtName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        btnViewPlans = findViewById(R.id.btnViewDiets);

        createPlansbtn = findViewById(R.id.newDpBtn);
        //txtName = (TextView) findViewById(R.id.txtName2);
        //String ID = txtName.getText().toString();
        Intent intent =getIntent();
        String Username= intent.getStringExtra("Username");
        btnViewPlans.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), viewPlans.class);
                //intent.putExtra("Name", ID);
                intent.putExtra("Username",Username);
                startActivity(intent);

                Toast.makeText(MainActivity.this, "View Plans", Toast.LENGTH_SHORT).show();
            }
        });

        createPlansbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), createDietPlan.class);
                intent.putExtra("Username",Username);
                intent.putExtra("Name", "");
                intent.putExtra("Category", "");
                intent.putExtra("Description", "");
                startActivity(intent);

                Toast.makeText(MainActivity.this, "Add New Diet Plans", Toast.LENGTH_SHORT).show();
            }
        });

        Spinner ageSpinner = findViewById(R.id.age_spinner);
        String[] ageCategories = {"Select Your Age", "Under 18", "18-30", "31-50", "Over 50"};

        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, ageCategories);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        ageSpinner.setAdapter(adapter);
        // set the default selected item to "Select your Age"
        ageSpinner.setSelection(0);

        // Add listener to the age spinner
        ageSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                selectedAgeCategory = parent.getItemAtPosition(position).toString();
                Toast.makeText(MainActivity.this, selectedAgeCategory, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                Toast.makeText(MainActivity.this, "Nothing Selected", Toast.LENGTH_SHORT).show();
            }
        });

        Button breakfastButton = findViewById(R.id.breakfast_button);
        breakfastButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                displayMealPlan("breakfast");
                Toast.makeText(MainActivity.this, "Breakfast", Toast.LENGTH_SHORT).show();
            }
        });

        Button lunchButton = findViewById(R.id.lunch_button);
        lunchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                displayMealPlan("lunch");
                Toast.makeText(MainActivity.this, "Lunch", Toast.LENGTH_SHORT).show();
            }
        });

        Button dinnerButton = findViewById(R.id.dinner_button);
        dinnerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                displayMealPlan("dinner");
                Toast.makeText(MainActivity.this, "Dinner", Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void displayMealPlan(String mealType) {
        TextView mealPlanTextView = findViewById(R.id.meal_plan_text_view);
        String mealPlan = "";

        switch (selectedAgeCategory) {
            case "Under 18":
                switch (mealType) {
                    case "breakfast":
                        mealPlan = "For breakfast, eat oatmeal and fruit.";
                        break;
                    case "lunch":
                        mealPlan = "For lunch, have a turkey and cheese sandwich with a side salad.";
                        break;
                    case "dinner":
                        mealPlan = "For dinner, eat grilled chicken with roasted vegetables.";
                        break;
                }
                break;
            case "18-30":
                switch (mealType) {
                    case "breakfast":
                        mealPlan = "For breakfast, eat a spinach and feta omelet.";
                        break;
                    case "lunch":
                        mealPlan = "For lunch, have a grilled chicken wrap with avocado and hummus.";
                        break;
                    case "dinner":
                        mealPlan = "For dinner, eat salmon with quinoa and steamed vegetables.";
                        break;
                }
                break;
            case "31-50":
                switch (mealType) {
                    case "breakfast":
                        mealPlan = "For breakfast, eat a spinach and mushroom frittata.";
                        break;
                    case "lunch":
                        mealPlan = "For lunch, have a quinoa and black bean salad with grilled chicken.";
                        break;
                    case "dinner":
                        mealPlan = "For dinner, eat a lean steak with sweet potato and green beans.";
                        break;
                }
                break;
            case "Over 50":
                switch (mealType) {
                    case "breakfast":
                        mealPlan = "For breakfast, eat a vef soup.";
                        break;
                    case "lunch":
                        mealPlan = "For lunch, have a quinoa and black bean salad.";
                        break;
                    case "dinner":
                        mealPlan = "For dinner, eat sweet potato and green beans.";
                        break;
                }
        }
        mealPlanTextView.setText(mealPlan);
    }

}