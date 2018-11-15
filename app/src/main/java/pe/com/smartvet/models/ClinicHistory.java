package pe.com.smartvet.models;

public class ClinicHistory {
    private String id;
    private String date;
    private Pet pet;
    private Double weight;
    private String details;

    public ClinicHistory() {
    }

    public ClinicHistory(String id, String date, Pet pet, Double weight, String details) {
        this.setId(id);
        this.setDate(date);
        this.setPet(pet);
        this.setWeight(weight);
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

    public String getDetails() {
        return details;
    }

    public ClinicHistory setDetails(String details) {
        this.details = details;
        return this;
    }
}
