package everymoveapp.prototype;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.List;

public class WorkoutsList extends ArrayAdapter<Workouts> {

    private Activity context;
    private List<Workouts>WorkoutList;

    public WorkoutsList(Activity context,List<Workouts>WorkoutList){

        super(context,R.layout.activity_workouts_list,WorkoutList);
        this.context =context;
        this.WorkoutList = WorkoutList;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

        LayoutInflater inflater = context.getLayoutInflater();
        View listViewItem = inflater.inflate(R.layout.activity_workouts_list,null,true);
        TextView textViewName= (TextView) listViewItem.findViewById(R.id.textViewName);
        TextView textViewID= (TextView) listViewItem.findViewById(R.id.textViewID);
        TextView textViewDBID= (TextView) listViewItem.findViewById(R.id.textViewDBID);

        Workouts workouts = WorkoutList.get(position);
        textViewName.setText(workouts.getPlanName());
        textViewID.setText(workouts.getPlanID());
        textViewDBID.setText(workouts.getDb_ID());

        return listViewItem;
    }


}

