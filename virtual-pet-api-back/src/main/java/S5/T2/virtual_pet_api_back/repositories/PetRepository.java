package S5.T2.virtual_pet_api_back.repositories;

import S5.T2.virtual_pet_api_back.models.Pet;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PetRepository extends JpaRepository<Pet, Long> {
    List<Pet> findByOwner_UserId(Long userId);
}
