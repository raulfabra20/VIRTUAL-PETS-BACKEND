package S5.T2.virtual_pet_api_back.models;

import jakarta.persistence.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "user")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long userId;

    @Column(name = "username")
    private String username;

    @Column(name = "password")
    private String password;

    @Enumerated(EnumType.STRING)
    @Column(name = "role")
    private UserType role;

    @OneToMany(mappedBy = "owner", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Pet> petList = new ArrayList<>();

    public User(){

    }
    public User(String username, String password, UserType role) {
        this.username = username;
        this.password = password;
        this.role = role;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getRole() {
        return role.toString();
    }

    public void setRole(UserType role) {
        this.role = role;
    }


    public void setRoleToEnum(String role){
        if (role == null) {
            throw new IllegalArgumentException("Role cannot be null.");
        }
        if(role.equalsIgnoreCase("admin")){
            setRole(UserType.ADMIN);
        } else if(role.equalsIgnoreCase("user")){
            setRole(UserType.USER);
        } else {
            throw new IllegalArgumentException("Not a valid role.");
        }
    }

    public List<Pet> getPetList() {
        return petList;
    }

    public void setPetList(List<Pet> petList) {
        this.petList = petList;
    }

    public void addPet(Pet pet){
        petList.add(pet);
    }
}
