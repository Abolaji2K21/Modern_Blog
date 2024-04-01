package africa.semicolon.controllers;


import africa.semicolon.BlogException.BigBlogException;
import africa.semicolon.dtos.requests.RegisterUserRequest;
import africa.semicolon.dtos.responds.ApiResponse;
import africa.semicolon.dtos.responds.RegisterUserResponse;
import africa.semicolon.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import static org.springframework.http.HttpStatus.BAD_REQUEST;
import static org.springframework.http.HttpStatus.CREATED;

@RestController
@RequestMapping ("/api/Modern_Blog")
public class UserController {
    @Autowired
    private UserService userService;


    @PostMapping("/signUp")
    public ResponseEntity<?> registerUser(@RequestBody RegisterUserRequest registerUserRequest) {
        try{
            var result = userService.register(registerUserRequest);
            return new ResponseEntity<>(new ApiResponse(true,result),CREATED);
        } catch (BigBlogException message){
            return new ResponseEntity<>(new ApiResponse(false, message.getMessage()),BAD_REQUEST);
        }
    }




}
