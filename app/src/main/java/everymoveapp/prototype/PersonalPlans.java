package everymoveapp.prototype;

public class PersonalPlans {
    String UserID;
    String PlanName;

    public PersonalPlans() {}

    public PersonalPlans(String userID, String planName) {
        UserID = userID;
        PlanName = planName;
    }

    public String getUserID() {
        return UserID;
    }

    public String getPlanName() {
        return PlanName;
    }
}

