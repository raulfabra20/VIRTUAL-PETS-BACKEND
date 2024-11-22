package S5.T2.virtual_pet_api_back.exception;

public class PetNotFoundException extends RuntimeException{
    public PetNotFoundException(String message) {
        super(message);
    }
}
