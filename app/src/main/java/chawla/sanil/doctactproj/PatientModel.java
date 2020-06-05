package chawla.sanil.doctactproj;

public class PatientModel {
    String age,gender,name,problems,image,address,id;

    public PatientModel() {
    }

    public PatientModel(String age, String gender, String name, String problems, String image, String address,String id) {
        this.age = age;
        this.gender = gender;
        this.name = name;
        this.problems = problems;
        this.image = image;
        this.address = address;
        this.id=id;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getAge() {
        return age;
    }

    public void setAge(String age) {
        this.age = age;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getProblems() {
        return problems;
    }

    public void setProblems(String problems) {
        this.problems = problems;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }
}
