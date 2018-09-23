package pe.com.smartvet.models;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class Vet {
    private String id;
    private String email;
    private String password;
    private String name;
    private String lastName;
    private String photo;
    private Integer mobilePhone;
    private String address;
    private String gender;
    private String status;
    private String signUpDate;

    public Vet() {
    }

    public Vet(String id, String email, String password, String name, String lastName, String photo, Integer mobilePhone, String address, String gender, String status, String signUpDate) {
        this.setId(id);
        this.setEmail(email);
        this.setPassword(password);
        this.setName(name);
        this.setLastName(lastName);
        this.setPhoto(photo);
        this.setMobilePhone(mobilePhone);
        this.setAddress(address);
        this.setGender(gender);
        this.setStatus(status);
        this.setSignUpDate(signUpDate);
    }

    public String getId() {
        return id;
    }

    public Vet setId(String id) {
        this.id = id;
        return this;
    }

    public String getEmail() {
        return email;
    }

    public Vet setEmail(String email) {
        this.email = email;
        return this;
    }

    public String getPassword() {
        return password;
    }

    public Vet setPassword(String password) {
        this.password = password;
        return this;
    }

    public String getName() {
        return name;
    }

    public Vet setName(String name) {
        this.name = name;
        return this;
    }

    public String getLastName() {
        return lastName;
    }

    public Vet setLastName(String lastName) {
        this.lastName = lastName;
        return this;
    }

    public String getPhoto() {
        return photo;
    }

    public Vet setPhoto(String photo) {
        this.photo = photo;
        return this;
    }

    public Integer getMobilePhone() {
        return mobilePhone;
    }

    public Vet setMobilePhone(Integer mobilePhone) {
        this.mobilePhone = mobilePhone;
        return this;
    }

    public String getAddress() {
        return address;
    }

    public Vet setAddress(String address) {
        this.address = address;
        return this;
    }

    public String getGender() {
        return gender;
    }

    public Vet setGender(String gender) {
        this.gender = gender;
        return this;
    }

    public String getStatus() {
        return status;
    }

    public Vet setStatus(String status) {
        this.status = status;
        return this;
    }

    public String getSignUpDate() {
        return signUpDate;
    }

    public Vet setSignUpDate(String signUpDate) {
        this.signUpDate = signUpDate;
        return this;
    }

    public static Vet build(JSONObject jsonVet) {
        if(jsonVet == null) return null;
        Vet vet = new Vet();
        try {
            vet.setId(jsonVet.getString("_id"));
            vet.setEmail(jsonVet.getString("email"));
            vet.setPassword(jsonVet.getString("password"));
            vet.setName(jsonVet.getString("name"));
            vet.setLastName(jsonVet.getString("lastName"));
            vet.setSignUpDate(jsonVet.getString("signUpDate"));
            vet.setGender(jsonVet.getString("gender"));
            vet.setMobilePhone(Integer.parseInt(jsonVet.getString("mobilePhone")));
            vet.setPhoto(jsonVet.getString("photo"));
            vet.setAddress(jsonVet.getString("address"));
            vet.setStatus(jsonVet.getString("status"));
            return vet;
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static List<Vet> build(JSONArray jsonVets) {
        if(jsonVets == null) return null;
        int length = jsonVets.length();
        List<Vet> vets = new ArrayList<>();
        for(int i = 0; i < length; i++)
            try {
                vets.add(Vet.build(jsonVets.getJSONObject(i)));
            } catch (JSONException e) {
                e.printStackTrace();
            }
        return vets;
    }
}
