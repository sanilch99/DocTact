package chawla.sanil.doctactproj.Homes.PatientFull;

public class DoctorModel extends DocId{

    public String address,gender,age, image,name,speciality;

    public DoctorModel() {
        //empty constructor
    }

    public DoctorModel(String address, String gender, String age, String image, String name, String speciality) {
        this.address = address;
        this.gender = gender;
        this.age = age;
        this.image = image;
        this.name = name;
        this.speciality = speciality;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getAge() {
        return age;
    }

    public void setAge(String age) {
        this.age = age;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSpeciality() {
        return speciality;
    }

    public void setSpeciality(String speciality) {
        this.speciality = speciality;
    }
}

