package everymoveapp.prototype;

public class Workouts {
    String db_ID;
    String planID;
    String planName;




    public Workouts(){}

    public Workouts(String db_ID, String planID, String planName) {
        this.db_ID = db_ID;
        this.planID = planID;
        this.planName = planName;
    }

    public String getDb_ID() {
        return db_ID;
    }

    public String getPlanID() {
        return planID;
    }

    public String getPlanName() {
        return planName;
    }
}
