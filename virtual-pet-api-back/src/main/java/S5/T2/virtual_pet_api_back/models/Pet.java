package S5.T2.virtual_pet_api_back.models;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "pet")
public class Pet {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long petId;

    @Enumerated(EnumType.STRING)
    @Column(name = "type")
    private PetType type;

    @Column(name = "name")
    private String name;

    @Column(name = "color")
    private String color;

    private int hungerLevel;

    private int happinessLevel;

    private boolean isDisguised = false;


    private LocalDateTime lastInteraction;


    @ManyToOne
    @JoinColumn(name = "owner_id", referencedColumnName = "userId")
    @JsonBackReference
    private User owner;

    public Pet() {

    }

    public Pet(PetType type, String name, String color, User owner) {
        this.type = type;
        this.name = name;
        this.color = color;
        this.owner = owner;
    }

    public Long getPetId() {
        return petId;
    }

    public void setPetId(Long petId) {
        this.petId = petId;
    }

    public PetType getType() {
        return type;
    }

    public void setType(PetType type) {
        this.type = type;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public User getOwner() {
        return owner;
    }

    public void setOwner(User owner) {
        this.owner = owner;
    }

    public int getHungerLevel() {
        return hungerLevel;
    }

    public void setHungerLevel(int hungerLevel) {
        this.hungerLevel = hungerLevel;
    }

    public int getHappinessLevel() {
        return happinessLevel;
    }

    public void setHappinessLevel(int happinessLevel) {
        this.happinessLevel = happinessLevel;
    }

    public boolean isDisguised() {
        return isDisguised;
    }

    public void setDisguised(boolean disguised) {
        isDisguised = disguised;
    }

    public LocalDateTime getLastInteraction() {
        return lastInteraction;
    }

    public void setLastInteraction(LocalDateTime lastInteraction) {
        this.lastInteraction = lastInteraction;
    }

    public void setTypeToEnum(String type) {
        try {
            this.type = PetType.valueOf(type.toUpperCase());
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("Not a valid type.");
        }
    }

    public Long getOwnerId(){
        return owner.getUserId();
    }

    public void adjustHungerLevel(int amount) {
        this.hungerLevel = Math.max(0, Math.min(this.hungerLevel + amount, 100));
    }

    public void adjustHappinessLevel(int amount) {
        this.happinessLevel = Math.max(0, Math.min(this.happinessLevel + amount, 100));
    }
}
