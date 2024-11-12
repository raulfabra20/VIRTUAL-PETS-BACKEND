package S5.T2.virtual_pet_api_back.controllers;

import S5.T2.virtual_pet_api_back.models.Pet;
import S5.T2.virtual_pet_api_back.models.User;
import S5.T2.virtual_pet_api_back.services.PetService;
import S5.T2.virtual_pet_api_back.services.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/pets")
public class PetController {

    private PetService petService;

    private UserService userService;

    public PetController(PetService petService, UserService userService) {
        this.petService = petService;
        this.userService = userService;
    }

    @PostMapping("/create")
    public ResponseEntity<User> createPet(@RequestBody  Map<String, String> request, Authentication authentication) {
        String userName = authentication.getName();
        User user = userService.getUserByUsername(userName);
        return ResponseEntity.ok(petService.createPet(pet));
    }

    @GetMapping
    public ResponseEntity<List<Pet>>  getPredeterminedPets(){

    }




    @GetMapping("/user/{petId}")
    public ResponseEntity<Pet> getPetById(@PathVariable Long petId) {
        Pet pet = petService.getPetById(petId);
        return pet.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @GetMapping("/user/{userId}")
    public List<Pet> getPetsByUserId(@PathVariable Long userId) {
        return petService.getPetsByUserId(userId);
    }

    @PostMapping("/{petId}")
    public ResponseEntity<Pet> updatePet(@PathVariable Long petId, @RequestBody Pet updatedPet ){

    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<?> deletePet(@PathVariable Long petId){

    }



}
