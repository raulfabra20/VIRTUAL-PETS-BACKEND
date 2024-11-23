package S5.T2.virtual_pet_api_back.services;

import S5.T2.virtual_pet_api_back.exception.UserNotFoundException;
import S5.T2.virtual_pet_api_back.models.Pet;
import S5.T2.virtual_pet_api_back.models.User;
import S5.T2.virtual_pet_api_back.repositories.PetRepository;
import S5.T2.virtual_pet_api_back.repositories.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.NoSuchElementException;


@Service
public class PetService {

    private PetRepository petRepository;

    private UserRepository userRepository;

    private static Logger log = LoggerFactory.getLogger(PetService.class);

    @Autowired
    public PetService(PetRepository petRepository, UserRepository userRepository) {
        this.petRepository = petRepository;
        this.userRepository = userRepository;
    }

   public Pet createPet(UserDetails userDetails, String petName, String petColor, String petType) {
       User newUser = userRepository.findByUsername(userDetails.getUsername());
        if(newUser == null){
            throw new UserNotFoundException("User not found");
        }
        log.info("Creating pet for user with id: "+newUser.getUserId());
       Pet newPet = new Pet();
        newPet.setTypeToEnum(petType);
        newPet.setName(petName);
        newPet.setColor(petColor);
        newPet.setOwner(newUser);
        setPetNeeds(newPet);
        newPet.setLastInteraction(LocalDateTime.now());
        newUser.addPet(newPet);

        log.info("Pet created: "+newPet.getPetId()+"\nName: "+newPet.getName()+"\nColor: "+newPet.getColor()+
                "\nType: "+newPet.getType());
        return petRepository.save(newPet);
    }

    public void setPetNeeds(Pet pet){
        pet.setHungerLevel(60);
        pet.setHappinessLevel(60);
    }

    public Pet updatePet(UserDetails userDetails, Long petId, String action ) {
        User newUser = userRepository.findByUsername(userDetails.getUsername());
        if(newUser == null){
            throw new NoSuchElementException("User not found");
        }
        Pet pet = petRepository.findById(petId).orElseThrow(() -> new NoSuchElementException("Pet not found"));

        switch (action) {
            case "feed":
                pet.setHungerLevel(pet.getHungerLevel() - 20);
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
                pet.setHappinessLevel(pet.getHappinessLevel() + 10);
                break;
            case "disguiseOut":
                pet.setDisguised(false);
                pet.setHappinessLevel(pet.getHappinessLevel() - 10);
                break;
            default:
                throw new IllegalArgumentException("Unknown action: " + action);
        }

        pet.setHappinessLevel(Math.max(0, Math.min(100, pet.getHappinessLevel())));
        pet.setHungerLevel(Math.max(0, Math.min(100, pet.getHungerLevel())));

        pet.setLastInteraction(LocalDateTime.now());
        return petRepository.save(pet);
    }


    public List<Pet> getAllPets() {
        return petRepository.findAll();
    }

    public List<Pet> getPetsByUserId(Long userId) {
        User user = userRepository.findByUserId(userId);
        if (user != null) {
            return petRepository.findByOwner_UserId(userId);
        } else {
            throw new AccessDeniedException("Access denied to see these pets.");
        }
    }
    public Pet getPetById(Long petId, Authentication authentication) {
        UserDetails userDetails = (UserDetails) authentication.getPrincipal();
        boolean isAdmin = isAdmin(userDetails);
        if (isAdmin) {
            return petRepository.findById(petId)
                    .orElseThrow(() -> new NoSuchElementException("Pet not found"));
        }
        Pet pet = petRepository.findById(petId)
                .orElseThrow(() -> new NoSuchElementException("Pet not found"));

        if (!pet.getOwner().getUsername().equals(userDetails.getUsername())) {
            throw new AccessDeniedException("Access denied to see this pet.");
        }
        return pet;
    }

    public Pet removePet(Long petId, Authentication authentication) {
        Pet pet = petRepository.findById(petId).orElseThrow(() -> new NoSuchElementException("Pet not found"));
        UserDetails userDetails = (UserDetails) authentication.getPrincipal();
        boolean isAdmin = isAdmin(userDetails);
        boolean isOwner = isOwner(userDetails, petId);
        if (isAdmin || isOwner) {
            User newUser = userRepository.findByUsername(userDetails.getUsername());
            newUser.removePet(pet);
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
                .anyMatch(auth -> auth.getAuthority().equalsIgnoreCase("ROLE_ADMIN"));
    }

    public Long getUserIdFromAuthentication(Authentication authentication) {
        UserDetails userDetails = (UserDetails) authentication.getPrincipal();
        User user = userRepository.findByUsername(userDetails.getUsername());
        return user.getUserId();
    }


}
