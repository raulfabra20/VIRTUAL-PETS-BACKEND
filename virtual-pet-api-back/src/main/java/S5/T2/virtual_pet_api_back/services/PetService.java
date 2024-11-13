package S5.T2.virtual_pet_api_back.services;

import S5.T2.virtual_pet_api_back.models.Pet;
import S5.T2.virtual_pet_api_back.models.User;
import S5.T2.virtual_pet_api_back.repositories.PetRepository;
import S5.T2.virtual_pet_api_back.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.NoSuchElementException;


@Service
public class PetService {

    private PetRepository petRepository;

    private UserRepository userRepository;

    @Autowired
    public PetService(PetRepository petRepository) {
        this.petRepository = petRepository;
    }

   public Pet createPet(UserDetails userDetails, String petName, String petColor, String petType) {
       User newUser = userRepository.findByUsername(userDetails.getUsername());
        if(newUser == null){
            throw new NoSuchElementException("user not found");
        }
       Pet newPet = new Pet();
        newPet.setTypeToEnum(petType);
        newPet.setName(petName);
        newPet.setColor(petColor);
        newPet.setOwner(newUser);
        setPetNeeds(newPet);
        newUser.addPet(newPet);

        return petRepository.save(newPet);
    }

    public void setPetNeeds(Pet pet){
        pet.setHungerLevel(60);
        pet.setHappinessLevel(60);
        pet.setDisguised(false);
    }

    public Pet updatePet(UserDetails userDetails, Long petId, String action ) {
        User newUser = userRepository.findByUsername(userDetails.getUsername());
        if(newUser == null){
            throw new NoSuchElementException("user not found");
        }
        Pet pet = petRepository.findById(petId).orElseThrow(() -> new NoSuchElementException("Pet not found"));

        switch (action) {
            case "feed":
                pet.setHungerLevel(pet.getHungerLevel() + 20);
                pet.setHappinessLevel(pet.getHappinessLevel() + 20);
                break;
            case "lightOn":
                pet.setHappinessLevel(pet.getHappinessLevel() - 50);
                break;
            case "lightOff":
                pet.setHappinessLevel(pet.getHappinessLevel() + 50);
                break;
            case "disguise":
                pet.setDisguised(true);
                break;
            default:
                throw new IllegalArgumentException("Unknown action: " + action);
        }

        return petRepository.save(pet);
    }


    public List<Pet> getAllPets() {
        return petRepository.findAll();
    }

    public List<Pet> getPredefinedPets(){
        return petRepository.findAll();
    }

    public List<Pet> getPetsByUserId(Long userId, Authentication authentication) {
        UserDetails userDetails = (UserDetails) authentication.getPrincipal();
        User newUser = userRepository.findByUsername(userDetails.getUsername());
        boolean isAdmin = isAdmin(userDetails);
        if (isAdmin) {
            return petRepository.findAll();
        } else {
            if (newUser != null && newUser.getUserId().equals(userId)) {
                return petRepository.findByUserId(userId);
            } else {
                throw new AccessDeniedException("Access denied to see this pet.");
            }
        }
    }
    public Pet getPetById(Long petId, Authentication authentication) {
        UserDetails userDetails = (UserDetails) authentication.getPrincipal();
        User newUser = userRepository.findByUsername(userDetails.getUsername());
        Pet pet = petRepository.findById(petId)
                .orElseThrow(() -> new NoSuchElementException("Pet not found"));
        if (!pet.getOwner().getUsername().equals(userDetails.getUsername())) {
            throw new AccessDeniedException("Access denied to see this pet.");
        }
        return pet;
    }

    public Pet removePet(Long petId, UserDetails userDetails) {
        Pet pet = petRepository.findById(petId).orElseThrow(() -> new NoSuchElementException("Pet not found"));
        boolean isAdmin = isAdmin(userDetails);
        boolean isOwner = isOwner(userDetails, petId);
        if (isAdmin || isOwner) {
            petRepository.deleteById(petId);
            return pet;
        } else {
            throw new AccessDeniedException("Access denied to see this pet.");
        }
    }

    public Pet findPet(Long petId){
        return petRepository.findAll()
                .stream()
                .filter(pet -> pet.getPetId().equals(petId))
                .findFirst()
                .orElse(null);
    }

    public boolean isOwner(UserDetails userDetails, Long petId) {
        Pet pet = findPet(petId);
        return pet.getOwner().getUsername().equals(userDetails.getUsername());
    }

    public boolean isAdmin(UserDetails userDetails) {
        return userDetails.getAuthorities().stream()
                .anyMatch(auth -> auth.getAuthority().equalsIgnoreCase("ADMIN"));
    }


}
