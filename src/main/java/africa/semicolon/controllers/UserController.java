package africa.semicolon.controllers;


import africa.semicolon.BlogException.BigBlogException;
import africa.semicolon.dtos.requests.RegisterUserRequest;
import africa.semicolon.dtos.responds.RegisterUserResponse;
import africa.semicolon.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping ("/api/Modern_Blog")
public class UserController {
    @Autowired
    private UserService userService;


    public ResponseEntity<?> registerUser(@RequestBody RegisterUserRequest registerUserRequest) {
        try{
            var result = userService.register(registerUserRequest);
            return null;
        } catch (BigBlogException message){
            message.getMessage();
        }
        return null;
    }



}
