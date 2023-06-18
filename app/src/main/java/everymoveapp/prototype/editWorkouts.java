package everymoveapp.prototype;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import com.google.firebase.FirebaseApp;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class editWorkouts extends AppCompatActivity {

    EditText editTextPName,editTextPID;
    String name, ID;
    Button buttonAdd;
    TextView textView;

    ListView listViewWorkouts;



    DatabaseReference databaseWorkouts;

    List<Workouts>workoutsList;






    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_workouts);
        databaseWorkouts= FirebaseDatabase.getInstance().getReference("Workouts");

        editTextPName= findViewById(R.id.editTextName);
        editTextPID=findViewById(R.id.editTextId);
        buttonAdd = findViewById(R.id.btnDB);
        listViewWorkouts= findViewById(R.id.listViewWorkouts);
        workoutsList= new ArrayList<>();




        buttonAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addPlan();

            }
        });



    }

    @Override
    protected void onStart() {
        super.onStart();
        databaseWorkouts.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                workoutsList.clear();


                for (DataSnapshot workoutSnapshot : snapshot.getChildren()){

                    Workouts workouts = workoutSnapshot.getValue(Workouts.class);

                    workoutsList.add(workouts);

                    // this part iterates through the all the children in the snpashot object iteratively like in flowr xquery expression
                    // assigning each to the workoutsnapshot object, getvalue returns the valu as objects of Workouts type


                }

                Log.d("List fill","for loop as run and filled te list");

                WorkoutsList adapter = new WorkoutsList(editWorkouts.this,workoutsList);
                listViewWorkouts.setAdapter(adapter);

                Log.d("adapter","listview adapter has been set");




            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {




            }
        });


    }

    private void addPlan(){
        name=editTextPName.getText().toString();
        ID=editTextPID.getText().toString();

        if(!TextUtils.isEmpty(name)){
            String DB_ID= databaseWorkouts.push().getKey();
            Workouts workouts = new Workouts(DB_ID,ID,name);
            databaseWorkouts.child(DB_ID).setValue(workouts);

            Toast.makeText(this,"values entered successfully",Toast.LENGTH_SHORT).show();

        }

        else {
            Toast.makeText(this, "enter values", Toast.LENGTH_SHORT).show();


        }

    }
}