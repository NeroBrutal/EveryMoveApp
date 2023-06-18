package everymoveapp.prototype;

public class DietPlans {
    String planCategory, planDescription,planID,planName;

    public DietPlans(){
    }


    public DietPlans(String planCategory, String planDescription, String planID, String planName) {
        this.planCategory = planCategory;
        this.planDescription = planDescription;
        this.planID = planID;
        this.planName = planName;
    }

    public String getPlanCategory() {
        return planCategory;
    }

    public String getPlanDescription() {
        return planDescription;
    }

    public String getPlanID() {
        return planID;
    }

    public String getPlanName() {
        return planName;
    }
}
