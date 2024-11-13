package S5.T2.virtual_pet_api_back.models;

import jakarta.persistence.*;

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

    private boolean isDisguised;


    @ManyToOne
    @JoinColumn(name = "owner_id")
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

    public void setOwner(User user) {
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

    public void setTypeToEnum(String type) {
        if (type.equalsIgnoreCase("mogwai")) {
            setType(PetType.MOGWAI);
        } else if (type.equalsIgnoreCase("gremlin")) {
            setType(PetType.GREMLIN);
        } else {
            throw new IllegalArgumentException("Not a valid type.");
        }
    }

    public Long getOwnerId(){
        return owner.getUserId();
    }
}
