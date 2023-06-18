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

public class DietList extends ArrayAdapter<DietPlans> {
    private Activity context;
    private List<DietPlans> dietPlansList;

    public DietList(Activity context, List<DietPlans>dietPlansList){

        super(context,R.layout.activity_diet_list,dietPlansList);
        this.context=context;
        this.dietPlansList=dietPlansList;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        LayoutInflater inflater = context.getLayoutInflater();
        View listViewItem =inflater.inflate(R.layout.activity_diet_list,null,true);
        TextView textViewName = (TextView) listViewItem.findViewById(R.id.txtPlanName);
        TextView textViewID = (TextView) listViewItem.findViewById(R.id.txtPlanID);
        TextView textViewCategory = (TextView) listViewItem.findViewById(R.id.txtPlanCategory);
        TextView textViewDescription = (TextView) listViewItem.findViewById(R.id.txtPlanDesc);

        DietPlans dietPlans = dietPlansList.get(position);
        textViewName.setText(dietPlans.getPlanName());
        textViewID.setText(dietPlans.getPlanID());
        textViewCategory.setText(dietPlans.getPlanCategory());
        textViewDescription.setText(dietPlans.getPlanDescription());

        return listViewItem;


    }
}
