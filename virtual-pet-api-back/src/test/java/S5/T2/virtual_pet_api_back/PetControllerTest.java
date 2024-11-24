package S5.T2.virtual_pet_api_back;

import S5.T2.virtual_pet_api_back.controllers.PetController;
import S5.T2.virtual_pet_api_back.models.*;
import S5.T2.virtual_pet_api_back.services.PetService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class PetControllerTest {

    @InjectMocks
    private PetController petController;

    @Mock
    private PetService petService;

    @Mock
    private Authentication authentication;

    @Test
    void getPetById_shouldReturnPet_whenPetExists() {
        // Datos de prueba
        Long petId = 1L;
        String username = "testUser";
        Pet mockPet = new Pet();
        mockPet.setPetId(petId);
        mockPet.setName("Buddy");
        mockPet.setColor("Brown");
        mockPet.setType(PetType.GREMLIN);

        UserPrincipal mockUserPrincipal = new UserPrincipal(new User(username, "password", UserType.ROLE_ADMIN));

        when(petService.getPetById(petId, authentication)).thenReturn(mockPet);

        ResponseEntity<?> response = petController.getPetById(petId, authentication);

        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(mockPet, response.getBody());

        verify(petService, times(1)).getPetById(petId, authentication);
    }
}
