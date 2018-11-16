package pe.com.smartvet.models;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class ClinicHistory {
    private String id;
    private String date;
    private Pet pet;
    private Double weight;
    private Double height;
    private String details;

    public ClinicHistory() {
    }

    public ClinicHistory(String id, String date, Pet pet, Double weight, Double height, String details) {
        this.setId(id);
        this.setDate(date);
        this.setPet(pet);
        this.setWeight(weight);
        this.setHeight(height);
        this.setDetails(details);
    }

    public String getId() {
        return id;
    }

    public ClinicHistory setId(String id) {
        this.id = id;
        return this;
    }

    public String getDate() {
        return date;
    }

    public ClinicHistory setDate(String date) {
        this.date = date;
        return this;
    }

    public Pet getPet() {
        return pet;
    }

    public ClinicHistory setPet(Pet pet) {
        this.pet = pet;
        return this;
    }

    public Double getWeight() {
        return weight;
    }

    public ClinicHistory setWeight(Double weight) {
        this.weight = weight;
        return this;
    }

    public Double getHeight() {
        return height;
    }

    public ClinicHistory setHeight(Double height) {
        this.height = height;
        return this;
    }

    public String getDetails() {
        return details;
    }

    public ClinicHistory setDetails(String details) {
        this.details = details;
        return this;
    }

        public static ClinicHistory build(JSONObject jsonClinicHistory) {
        if(jsonClinicHistory == null) return null;
        ClinicHistory clinicHistory= new ClinicHistory();
        try {
            clinicHistory.setId(jsonClinicHistory.getString("_id"));
            clinicHistory.setDate(jsonClinicHistory.getString("date"));
            clinicHistory.setWeight(Double.parseDouble(jsonClinicHistory.getString("weight")));
            clinicHistory.setHeight(Double.parseDouble(jsonClinicHistory.getString("height")));
            clinicHistory.setDetails(jsonClinicHistory.getString("details"));
            clinicHistory.setPet(Pet.build(jsonClinicHistory.getJSONObject("pet")));
            return clinicHistory;
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static List<ClinicHistory> build(JSONArray jsonClinicalHistories) {
        if(jsonClinicalHistories == null) return null;
        int length = jsonClinicalHistories.length();
        List<ClinicHistory> clinicalHistories = new ArrayList<>();
        for(int i = 0; i < length; i++)
            try {
                clinicalHistories.add(ClinicHistory.build(jsonClinicalHistories.getJSONObject(i)));
            } catch (JSONException e) {
                e.printStackTrace();
            }
        return clinicalHistories;
    }
}
