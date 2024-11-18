package S5.T2.virtual_pet_api_back.controllers;


import S5.T2.virtual_pet_api_back.models.User;
import S5.T2.virtual_pet_api_back.services.UserService;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;
import io.swagger.v3.oas.annotations.Operation;

import java.util.Map;

@RestController
@RequestMapping("/user")
public class UserController {

    @Autowired
    private UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @Operation(summary = "Login a user", description = "Returns the details of the authenticated user.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "User authenticated successfully",
                    content = @Content(schema = @Schema(implementation = UserDetails.class))),
            @ApiResponse(responseCode = "401", description = "Not valid credentials.")
    })
    @PostMapping("/login")
    public String login(@RequestBody Map<String, String> payload){
        String username = payload.get("username");
        String password = payload.get("password");
        return userService.verify(username, password);
    }


    @Operation(summary = "Register a user", description = "Registers a new user in the system.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "User registered successfully"),
            @ApiResponse(responseCode = "40", description = "User data not valid.")
    })
    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody Map<String, String> payload){
        String username = payload.get("username");
        String password = payload.get("password");
        String role = payload.get("role");

        if (username == null || password == null || role == null) {
            return new ResponseEntity<>("All fields are required", HttpStatus.BAD_REQUEST);
        }
        try {
            User newUser = userService.createUser(username, password, role);
            return new ResponseEntity<>(newUser, HttpStatus.CREATED);
        } catch (IllegalArgumentException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }
}
