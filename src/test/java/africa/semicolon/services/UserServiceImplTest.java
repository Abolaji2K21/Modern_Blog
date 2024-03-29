package africa.semicolon.services;

import africa.semicolon.BlogException.BigBlogException;
import africa.semicolon.data.repositories.UserRepository;
import africa.semicolon.dtos.requests.RegisterUserRequest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
class UserServiceImplTest {

    @Autowired
    private UserService userService;

    @Autowired
    private UserRepository userRepository;


    @BeforeEach
    void setUp(){
        userRepository.deleteAll();
    }


    @Test
    void testThatUserCanRegister(){
        RegisterUserRequest registerRequest = new RegisterUserRequest();
        registerRequest.setFirstname("PenIs");
        registerRequest.setLastname("Up");
        registerRequest.setUsername("PenIsUp");
        registerRequest.setPassword("Holes");

        assertThat(userRepository.count(), is(0L));
        userService.register(registerRequest);

    }

    @Test
    void testThatUserTriesToRegisterAgainAfterRegisteringTheFirstTime(){
        RegisterUserRequest registerRequest = new RegisterUserRequest();
        registerRequest.setFirstname("PenIs");
        registerRequest.setLastname("Up");
        registerRequest.setUsername("PenIsUp");
        registerRequest.setPassword("Holes");
        assertThat(userRepository.count(), is(0L));

        userService.register(registerRequest);
        try{
            userService.register(registerRequest);

        } catch (BigBlogException message){
            assertEquals("penisup already exists", message.getMessage());

        }

        assertThat(userRepository.count(), is(1L));
    }



}