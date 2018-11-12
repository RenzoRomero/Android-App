package pe.com.smartvet.models;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class Pet {
    private String id;
    private Owner owner;
    private String name;
    private String photo;
    private String breed;
    private String birthdate;
    private String gender;
    private String status;

    public Pet() {
    }

    public Pet(String id, Owner owner, String name, String photo, String breed, String birthdate, String gender, String status) {
        this.setId(id);
        this.setOwner(owner);
        this.setName(name);
        this.setPhoto(photo);
        this.setBreed(breed);
        this.setBirthdate(birthdate);
        this.setGender(gender);
        this.setStatus(status);
    }

    public String getId() {
        return id;
    }

    public Pet setId(String id) {
        this.id = id;
        return this;
    }

    public Owner getOwner() {
        return owner;
    }

    public Pet setOwner(Owner owner) {
        this.owner = owner;
        return this;
    }

    public String getName() {
        return name;
    }

    public Pet setName(String name) {
        this.name = name;
        return this;
    }

    public String getPhoto() {
        return photo;
    }

    public Pet setPhoto(String photo) {
        this.photo = photo;
        return this;
    }

    public String getBreed() {
        return breed;
    }

    public Pet setBreed(String breed) {
        this.breed = breed;
        return this;
    }

    public String getBirthdate() {
        return birthdate;
    }

    public Pet setBirthdate(String birthdate) {
        this.birthdate = birthdate;
        return this;
    }

    public String getGender() {
        return gender;
    }

    public Pet setGender(String gender) {
        this.gender = gender;
        return this;
    }

    public String getStatus() {
        return status;
    }

    public Pet setStatus(String status) {
        this.status = status;
        return this;
    }

    public static Pet build(JSONObject jsonPet) {
        if(jsonPet == null) return null;
        Pet pet = new Pet();
        try {
            pet.setId(jsonPet.getString("_id"));
            pet.setOwner(Owner.build(jsonPet.getJSONObject("owner")));
            pet.setName(jsonPet.getString("name"));
            pet.setPhoto(jsonPet.getString("photo"));
            pet.setBreed(jsonPet.getString("breed"));
            pet.setBirthdate(jsonPet.getString("birthdate"));
            pet.setGender(jsonPet.getString("gender"));
            pet.setStatus(jsonPet.getString("status"));
            return pet;
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static List<Pet> build(JSONArray jsonPets) {
        if(jsonPets == null) return null;
        int length = jsonPets.length();
        List<Pet> pets = new ArrayList<>();
        for(int i = 0; i < length; i++)
            try {
                pets.add(Pet.build(jsonPets.getJSONObject(i)));
            } catch (JSONException e) {
                e.printStackTrace();
            }
        return pets;
    }
}
