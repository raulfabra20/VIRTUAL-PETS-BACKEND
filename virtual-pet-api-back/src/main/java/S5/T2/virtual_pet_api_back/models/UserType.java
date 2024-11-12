package S5.T2.virtual_pet_api_back.models;

public enum UserType {
    USER,
    ADMIN;

    @Override
    public String toString() {
        return this.name().toLowerCase(); // Ejemplo: devuelve "admin" o "user"
    }
}
