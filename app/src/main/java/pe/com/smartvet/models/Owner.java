package pe.com.smartvet.models;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class Owner {
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

    public Owner() {
    }

    public Owner(String id, String email, String password, String name, String lastName, String photo, Integer mobilePhone, String address, String gender, String status, String signUpDate) {
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

    public Owner setId(String id) {
        this.id = id;
        return this;
    }

    public String getEmail() {
        return email;
    }

    public Owner setEmail(String email) {
        this.email = email;
        return this;
    }

    public String getPassword() {
        return password;
    }

    public Owner setPassword(String password) {
        this.password = password;
        return this;
    }

    public String getName() {
        return name;
    }

    public Owner setName(String name) {
        this.name = name;
        return this;
    }

    public String getLastName() {
        return lastName;
    }

    public Owner setLastName(String lastName) {
        this.lastName = lastName;
        return this;
    }

    public String getPhoto() {
        return photo;
    }

    public Owner setPhoto(String photo) {
        this.photo = photo;
        return this;
    }

    public Integer getMobilePhone() {
        return mobilePhone;
    }

    public Owner setMobilePhone(Integer mobilePhone) {
        this.mobilePhone = mobilePhone;
        return this;
    }

    public String getAddress() {
        return address;
    }

    public Owner setAddress(String address) {
        this.address = address;
        return this;
    }

    public String getGender() {
        return gender;
    }

    public Owner setGender(String gender) {
        this.gender = gender;
        return this;
    }

    public String getStatus() {
        return status;
    }

    public Owner setStatus(String status) {
        this.status = status;
        return this;
    }

    public String getSignUpDate() {
        return signUpDate;
    }

    public Owner setSignUpDate(String signUpDate) {
        this.signUpDate = signUpDate;
        return this;
    }

    public static Owner build(JSONObject jsonOwner) {
        if(jsonOwner == null) return null;
        Owner owner = new Owner();
        try {
            owner.setId(jsonOwner.getString("_id"));
            owner.setEmail(jsonOwner.getString("email"));
            owner.setPassword(jsonOwner.getString("password"));
            owner.setName(jsonOwner.getString("name"));
            owner.setLastName(jsonOwner.getString("lastName"));
            owner.setSignUpDate(jsonOwner.getString("signUpDate"));
            owner.setGender(jsonOwner.getString("gender"));
            owner.setMobilePhone(Integer.parseInt(jsonOwner.getString("mobilePhone")));
            owner.setPhoto(jsonOwner.getString("photo"));
            owner.setAddress(jsonOwner.getString("address"));
            owner.setStatus(jsonOwner.getString("status"));
            return owner;
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static List<Owner> build(JSONArray jsonOwners) {
        if(jsonOwners == null) return null;
        int length = jsonOwners.length();
        List<Owner> owners = new ArrayList<>();
        for(int i = 0; i < length; i++)
            try {
                owners.add(Owner.build(jsonOwners.getJSONObject(i)));
            } catch (JSONException e) {
                e.printStackTrace();
            }
        return owners;
    }
}
