package S5.T2.virtual_pet_api_back.controllers;

import S5.T2.virtual_pet_api_back.dto.PetActionRequest;
import S5.T2.virtual_pet_api_back.dto.PetRequest;
import S5.T2.virtual_pet_api_back.models.Pet;
import S5.T2.virtual_pet_api_back.models.User;
import S5.T2.virtual_pet_api_back.models.UserPrincipal;
import S5.T2.virtual_pet_api_back.models.UserType;
import S5.T2.virtual_pet_api_back.services.PetService;
import S5.T2.virtual_pet_api_back.services.UserService;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.ArraySchema;

import java.util.List;

@RestController
@RequestMapping("/pets")
public class PetController {

    @Autowired
    private PetService petService;

    @Autowired
    private UserService userService;

    private static Logger log = LoggerFactory.getLogger(PetController.class);

    public PetController(PetService petService, UserService userService) {
        this.petService = petService;
        this.userService = userService;
    }

    @Operation(summary = "Create a new pet", description = "Creates a new pet for the authenticated user.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Pet created successfully",
                    content = @Content(schema = @Schema(implementation = Pet.class))),
            @ApiResponse(responseCode = "400", description = "Invalid input data")
    })
   @PostMapping("/create")
    @PreAuthorize("hasAnyAuthority('ADMIN', 'USER')")
    public ResponseEntity<?> createPet(@RequestBody PetRequest petRequest, Authentication authentication) {
       UserDetails userDetails = (UserDetails) authentication.getPrincipal();
        String name = petRequest.getName();
        String color = petRequest.getColor();
        String type = petRequest.getType();
       if (name == null || color == null || type == null) {
           throw new IllegalArgumentException("All fields are required");
       }
       Pet newPet = petService.createPet(userDetails, name, color, type);
       log.info("pet successfully created. Pet id: "+newPet.getPetId());

       return new ResponseEntity<>(newPet, HttpStatus.CREATED);
    }

    @Operation(summary = "Get pet by ID", description = "Returns the details of a pet by its ID.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Pet found",
                    content = @Content(schema = @Schema(implementation = Pet.class))),
            @ApiResponse(responseCode = "404", description = "Pet not found")
    })
    @GetMapping("/{petId}")
    @PreAuthorize("hasAnyAuthority('ADMIN', 'USER')")
    public Pet getPetById(@PathVariable Long petId, Authentication authentication) {
        return petService.getPetById(petId, authentication);
    }

    @Operation(summary = "Get pets by user ID", description = "Returns a list of pets owned by a specific user.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "List of pets found",
                    content = @Content(array = @ArraySchema(schema = @Schema(implementation = Pet.class)))),
            @ApiResponse(responseCode = "403", description = "Access denied")
    })
    @GetMapping
    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_USER')")
    public List<Pet> getPets(Authentication authentication) {
        UserPrincipal userPrincipal = (UserPrincipal) authentication.getPrincipal();
        User user = userPrincipal.getUser();
        String userRole = user.getRole().toUpperCase();
        log.info("User's role: "+userRole);
        if (userRole.equals(UserType.ROLE_ADMIN.name()) ) {
            return petService.getAllPets();
        } else {
            return petService.getPetsByUserId(user.getUserId());
        }
    }


    @Operation(summary = "Update a pet", description = "Updates a pet's status based on the action provided (e.g., feed, disguise).")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Pet updated successfully",
                    content = @Content(schema = @Schema(implementation = Pet.class))),
            @ApiResponse(responseCode = "400", description = "Invalid action"),
            @ApiResponse(responseCode = "403", description = "Access denied")
    })
    @PostMapping("/{petId}/update")
    @PreAuthorize("hasAnyAuthority('ADMIN', 'USER')")
    public ResponseEntity<Pet> updatePet(@PathVariable Long petId,
                                         @RequestBody PetActionRequest request, Authentication authentication ){
        UserDetails userDetails = (UserDetails) authentication.getPrincipal();
        boolean isAdmin = petService.isAdmin(userDetails);
        boolean isOwner = petService.isOwner(userDetails, petId);
        if(isAdmin || isOwner){
            String action = request.getAction();
            Pet updatedPet = petService.updatePet(userDetails, petId, action);
            return ResponseEntity.ok(updatedPet);
        } else {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }


    @Operation(summary = "Remove a pet", description = "Deletes a pet from the database.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Pet removed successfully",
                    content = @Content(schema = @Schema(implementation = Pet.class))),
            @ApiResponse(responseCode = "404", description = "Pet not found")
    })
    @DeleteMapping("/{petId}/delete")
    @PreAuthorize("hasAnyAuthority('ADMIN', 'USER')")
    public ResponseEntity<?> removePet(@PathVariable Long petId, Authentication authentication) {
        UserDetails userDetails = (UserDetails) authentication.getPrincipal();
        Pet removedPet = petService.removePet(petId, authentication);
        return new ResponseEntity<>(removedPet, HttpStatus.OK);
    }

   /* @Operation(summary = "Get predefined pets", description = "Returns a list of predefined pets.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "List of predefined pets",
                    content = @Content(schema = @Schema(implementation = Pet.class)))
    })
    @GetMapping("/predefined")
    @PreAuthorize("hasAnyAuthority('ADMIN', 'USER')")
    public ResponseEntity<List<Pet>>  getPredeterminedPets(){
        return ResponseEntity.ok(petService.getPredefinedPets());
    } */
}
