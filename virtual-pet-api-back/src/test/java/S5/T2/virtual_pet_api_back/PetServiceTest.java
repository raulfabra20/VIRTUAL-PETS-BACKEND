package S5.T2.virtual_pet_api_back;

import S5.T2.virtual_pet_api_back.models.Pet;
import S5.T2.virtual_pet_api_back.repositories.UserRepository;
import S5.T2.virtual_pet_api_back.repositories.PetRepository;
import S5.T2.virtual_pet_api_back.services.PetService;
import S5.T2.virtual_pet_api_back.models.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.NoSuchElementException;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;


public class PetServiceTest {

    @InjectMocks
    private PetService petService;

    @Mock
    private UserRepository userRepository;

    @Mock
    private PetRepository petRepository;

    @Mock
    private UserDetails userDetails;

    private User user;
    private Pet pet;


    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        user = new User();
        user.setUsername("testUser");

        pet = new Pet();
        pet.setPetId(1L);
        pet.setHungerLevel(50);
        pet.setHappinessLevel(50);
    }

    @Test
    void updatePet_shouldIncreaseHappiness_whenActionIsFeed() {

        when(userDetails.getUsername()).thenReturn("testUser");
        when(userRepository.findByUsername("testUser")).thenReturn(user);
        when(petRepository.findById(1L)).thenReturn(Optional.of(pet));
        when(petRepository.save(any(Pet.class))).thenReturn(pet);


        Pet updatedPet = petService.updatePet(userDetails, 1L, "feed");


        assertEquals(70, updatedPet.getHappinessLevel());
        assertEquals(30, updatedPet.getHungerLevel());


        verify(userRepository, times(1)).findByUsername("testUser");
        verify(petRepository, times(1)).findById(1L);
        verify(petRepository, times(1)).save(updatedPet);
    }

    @Test
    void updatePet_shouldThrowException_whenPetNotFound() {

        when(userDetails.getUsername()).thenReturn("testUser");
        when(userRepository.findByUsername("testUser")).thenReturn(user);
        when(petRepository.findById(1L)).thenReturn(Optional.empty());


        assertThrows(NoSuchElementException.class, () -> {
            petService.updatePet(userDetails, 1L, "feed");
        });


        verify(userRepository, times(1)).findByUsername("testUser");
        verify(petRepository, times(1)).findById(1L);
        verify(petRepository, never()).save(any(Pet.class));
    }
}
