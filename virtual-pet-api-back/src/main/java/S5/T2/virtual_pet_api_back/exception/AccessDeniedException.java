package S5.T2.virtual_pet_api_back.exception;

public class AccessDeniedException extends RuntimeException {
    public AccessDeniedException(String message){
        super(message);
    }
}
