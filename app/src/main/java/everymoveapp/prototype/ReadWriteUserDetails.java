package everymoveapp.prototype;

public class ReadWriteUserDetails {

    public String Dob,Gender,Height,Weight;

    public ReadWriteUserDetails() {
    }

    public ReadWriteUserDetails(String textDob, String textGender, String textHeight, String textWeight) {
        this.Dob = textDob;
        this.Gender = textGender;
        this.Height = textHeight;
        this.Weight = textWeight;
    }
}
