package S5.T2.virtual_pet_api_back.models;

public enum UserType {
    ROLE_USER,
    ROLE_ADMIN;

    @Override
    public String toString() {
        return this.name().toLowerCase();
    }
}
