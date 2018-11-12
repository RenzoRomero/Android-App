package pe.com.smartvet;

import android.app.Application;

import com.androidnetworking.AndroidNetworking;

import pe.com.smartvet.models.Owner;
import pe.com.smartvet.models.Pet;
import pe.com.smartvet.models.Product;
import pe.com.smartvet.models.Vet;
import pe.com.smartvet.network.SmartVetService;

public class SmartVetApp extends Application {
    private static SmartVetApp instance;
    private SmartVetService smartVetService;

    public SmartVetApp() {
        super();
        instance = this;
    }

    public static SmartVetApp getInstance() {
        return instance;
    }

    public void onCreate() {
        super.onCreate();
        AndroidNetworking.initialize(getApplicationContext());
        smartVetService = new SmartVetService();
    }

    public Vet getVet() {
        return smartVetService.getCurrentVet();
    }

    public SmartVetApp setVet(Vet vet) {
        smartVetService.setCurrentVet(vet);
        return this;
    }

    public String getToken() {
        return smartVetService.getCurrentToken();
    }

    public SmartVetApp setToken(String token) {
        smartVetService.setCurrentToken(token);
        return this;
    }

    public Owner getOwner() {
        return smartVetService.getCurrentOwner();
    }

    public SmartVetApp setOwner(Owner owner) {
        smartVetService.setCurrentOwner(owner);
        return this;
    }

    public Product getProduct() {
        return smartVetService.getCurrentProduct();
    }

    public SmartVetApp setProduct(Product product) {
        smartVetService.setCurrentProduct(product);
        return this;
    }

    public Pet getPet() {
        return smartVetService.getCurrentPet();
    }

    public SmartVetApp setPet(Pet pet) {
        smartVetService.setCurrentPet(pet);
        return this;
    }
}
