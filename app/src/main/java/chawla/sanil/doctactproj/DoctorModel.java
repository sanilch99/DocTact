package chawla.sanil.doctactproj;

public class DoctorModel {

    String Name,Image,Address,Spec;

    public DoctorModel(String name, String image, String address, String spec) {
        Name = name;
        Image = image;
        Address = address;
        Spec = spec;
    }


    public DoctorModel() {
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    public String getImage() {
        return Image;
    }

    public void setImage(String image) {
        Image = image;
    }

    public String getAddress() {
        return Address;
    }

    public void setAddress(String address) {
        Address = address;
    }

    public String getSpec() {
        return Spec;
    }

    public void setSpec(String spec) {
        Spec = spec;
    }
}
