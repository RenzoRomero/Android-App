package pe.com.smartvet.network;

import pe.com.smartvet.models.Owner;
import pe.com.smartvet.models.Product;
import pe.com.smartvet.models.Vet;

public class SmartVetService {
    public static String SIGNIN_VET_URL = "https://smart-vet.herokuapp.com/api/vet/signin";
    public static String SIGNUP_VET_URL = "https://smart-vet.herokuapp.com/api/vet/signup";
    public static String OWNER_URL = "https://smart-vet.herokuapp.com/api/owner";
    public static String PRODUCT_URL = "https://smart-vet.herokuapp.com/api/product";

    private Vet currentVet;
    private String currentToken;
    private Owner currentOwner;
    private Product currentProduct;

    public Vet getCurrentVet() {
        return currentVet;
    }

    public SmartVetService setCurrentVet(Vet currentVet) {
        this.currentVet = currentVet;
        return this;
    }

    public String getCurrentToken() {
        return currentToken;
    }

    public SmartVetService setCurrentToken(String currentToken) {
        this.currentToken = currentToken;
        return this;
    }

    public Owner getCurrentOwner() {
        return currentOwner;
    }

    public SmartVetService setCurrentOwner(Owner currentOwner) {
        this.currentOwner = currentOwner;
        return this;
    }

    public Product getCurrentProduct() {
        return currentProduct;
    }

    public SmartVetService setCurrentProduct(Product currentProduct) {
        this.currentProduct = currentProduct;
        return this;
    }
}
