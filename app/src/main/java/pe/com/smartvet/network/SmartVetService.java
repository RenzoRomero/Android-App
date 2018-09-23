package pe.com.smartvet.network;

import pe.com.smartvet.models.Vet;

public class SmartVetService {
    public static String SIGNIN_VET_URL = "https://smart-vet.herokuapp.com/api/vet/signin";
    public static String SIGNUP_VET_URL = "https://smart-vet.herokuapp.com/api/vet/signup";
    public static String PRODUCT_URL = "https://smart-vet.herokuapp.com/api/product";

    private Vet currentVet;
    private String currentToken;

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
}
