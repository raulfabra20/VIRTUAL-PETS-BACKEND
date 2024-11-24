package S5.T2.virtual_pet_api_back.services;

import S5.T2.virtual_pet_api_back.exception.PetNotFoundException;
import S5.T2.virtual_pet_api_back.models.Pet;
import S5.T2.virtual_pet_api_back.repositories.PetRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;

@Service
public class PetStatusService {

    private final PetRepository petRepository;

    private static Logger log = LoggerFactory.getLogger(PetStatusService.class);

    public PetStatusService(PetRepository petRepository) {
        this.petRepository = petRepository;
    }

    @Scheduled(fixedRate = 60000)
    public void updatePetStatus() {
        List<Pet> pets = petRepository.findAll();
        for (Pet pet : pets) {
            if (pet.getLastInteraction() == null) {
                pet.setLastInteraction(LocalDateTime.now());
                petRepository.save(pet);
                continue;
            }
            long minutesSinceLastInteraction = Duration.between(pet.getLastInteraction(), LocalDateTime.now()).toMinutes();
            if (minutesSinceLastInteraction > 1) {
                int previousHunger = pet.getHungerLevel();
                int previousHappiness = pet.getHappinessLevel();

                pet.adjustHungerLevel(5);
                pet.adjustHappinessLevel(-2);

                log.info("Updating pet with ID: {} | Hunger: {} -> {} | Happiness: {} -> {}",
                        pet.getPetId(), previousHunger, pet.getHungerLevel(), previousHappiness, pet.getHappinessLevel());

                petRepository.save(pet);
            }
        }
    }

    /*public void feedGremlin(Long petId) {
        Pet pet = petRepository.findById(petId).orElseThrow(() ->
                new PetNotFoundException("Pet with id: "+petId+" not found."));
        pet.adjustHungerLevel(-20);
        pet.adjustHappinessLevel(10);
        pet.setLastInteraction(LocalDateTime.now());
        petRepository.save(pet);
    }*/
}
