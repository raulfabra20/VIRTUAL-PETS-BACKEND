package S5.T2.virtual_pet_api_back.services;

import S5.T2.virtual_pet_api_back.models.Pet;
import S5.T2.virtual_pet_api_back.repositories.PetRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class PetService {

    private PetRepository petRepository;

    @Autowired
    public PetService(PetRepository petRepository) {
        this.petRepository = petRepository;
    }

    public Pet createPet(Pet pet) {
        return petRepository.save(pet);
    }

    public Pet updatePet(Pet pet) {
        return petRepository.save(pet);
    }


    public List<Pet> getAllPets() {
        return petRepository.findAll();
    }


    public Pet getPetById(Long petId) {
        return petRepository.findById(petId);
    }


    public List<Pet> getPetsByUserId(Long userId) {
        return petRepository.findByUserId(userId);
    }

    public Pet removePet(Long petId){
         petRepository.findById(petId);

         return petRepository.deleteById(petId);
    }
}
