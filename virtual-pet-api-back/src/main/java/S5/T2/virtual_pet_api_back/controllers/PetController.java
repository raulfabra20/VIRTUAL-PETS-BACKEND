package S5.T2.virtual_pet_api_back.controllers;

import S5.T2.virtual_pet_api_back.models.Pet;
import S5.T2.virtual_pet_api_back.models.User;
import S5.T2.virtual_pet_api_back.services.PetService;
import S5.T2.virtual_pet_api_back.services.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;

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
    public ResponseEntity<?> createPet(@RequestBody  Map<String, String> payload, Authentication authentication) {
       UserDetails userDetails = (UserDetails) authentication.getPrincipal();
        String name = payload.get("name");
        String color = payload.get("color");
        String type = payload.get("type");
       if (name == null || color == null || type == null) {
           return new ResponseEntity<>("All fields are required", HttpStatus.BAD_REQUEST);
       }
       try {
           Pet newPet = petService.createPet(userDetails, name, color, type);
           return new ResponseEntity<>(newPet, HttpStatus.CREATED);
       } catch (IllegalArgumentException e) {
           return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
       }
    }

    @GetMapping("/predefined")
    public ResponseEntity<List<Pet>>  getPredeterminedPets(){
        return ResponseEntity.ok(petService.getPredefinedPets());
    }

    @GetMapping("/{petId}")
    public Pet getPetById(@PathVariable Long petId, Authentication authentication) {
        return petService.getPetById(petId, authentication);
    }

    @GetMapping
    public List<Pet> getPetsByUserId(@PathVariable Long userId, Authentication authentication) {
        UserDetails userDetails = (UserDetails) authentication.getPrincipal();
        boolean isAdmin = petService.isAdmin(userDetails);
        if (isAdmin) {
            return petService.getAllPets();
        } else {
            return petService.getPetsByUserId(userId, authentication);
        }
    }

    @PostMapping("/{petId}/update")
    public ResponseEntity<Pet> updatePet(@PathVariable Long petId,
                                         @RequestBody Map<String, String> payload, Authentication authentication ){
        UserDetails userDetails = (UserDetails) authentication.getPrincipal();
        boolean isAdmin = petService.isAdmin(userDetails);
        boolean isOwner = petService.isOwner(userDetails, petId);

        if(isAdmin || isOwner){
            String action = payload.get("action");
            Pet updatedPet = petService.updatePet(userDetails, petId, action);
            return ResponseEntity.ok(updatedPet);
        } else {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }





    }

    @DeleteMapping("/{petId}/delete")
    public ResponseEntity<?> removePet(@PathVariable Long petId, Authentication authentication) {
        UserDetails userDetails = (UserDetails) authentication.getPrincipal();
        Pet removedPet = petService.removePet(petId, userDetails);
        return new ResponseEntity<>(removedPet, HttpStatus.OK);
    }



}
