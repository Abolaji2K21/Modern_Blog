package africa.semicolon.services;

import africa.semicolon.BlogException.BigBlogException;
import africa.semicolon.BlogException.UserNotFoundException;
import africa.semicolon.data.model.User;
import africa.semicolon.data.repositories.UserRepository;
import africa.semicolon.dtos.requests.LoginUserRequest;
import africa.semicolon.dtos.requests.LogoutUserRequest;
import africa.semicolon.dtos.requests.RegisterUserRequest;
import africa.semicolon.dtos.requests.UpdateUserRequest;
import africa.semicolon.dtos.responds.LoginUserResponse;
import africa.semicolon.dtos.responds.LogoutUserResponse;
import africa.semicolon.dtos.responds.RegisterUserResponse;
import africa.semicolon.dtos.responds.UpdateUserResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Optional;

import static org.bson.assertions.Assertions.assertNotNull;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import static org.junit.jupiter.api.Assertions.*;

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
        assertThat(userRepository.count(), is(1L ));

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

    @Test
    void testThatUserCanLoginAfterRegistration(){
        RegisterUserRequest registerRequest = new RegisterUserRequest();
        registerRequest.setFirstname("PenIs");
        registerRequest.setLastname("Up");
        registerRequest.setUsername("PenIsUp");
        registerRequest.setPassword("Holes");
        userService.register(registerRequest);
        assertThat(userRepository.count(), is(1L));

        LoginUserRequest loginRequest = new LoginUserRequest();
        loginRequest.setUsername("PenIsUp");
        loginRequest.setPassword("Holes");
        LoginUserResponse loginResponse = userService.login(loginRequest);
        assertThat(loginResponse.getUsername(), is("penisup"));


    }


    @Test
    void testThatUserCanLoginAndLogoutSuccessfully() {
        RegisterUserRequest registerRequest = new RegisterUserRequest();
        registerRequest.setFirstname("PenIs");
        registerRequest.setLastname("Up");
        registerRequest.setUsername("PenIsUp");
        registerRequest.setPassword("Holes");
        userService.register(registerRequest);
        assertThat(userRepository.count(), is(1L));

        LoginUserRequest loginRequest = new LoginUserRequest();
        loginRequest.setUsername("PenIsUp");
        loginRequest.setPassword("Holes");

        LoginUserResponse loginResponse = userService.login(loginRequest);
        assertThat(loginResponse.getUsername(), is("penisup"));

        LogoutUserRequest logoutRequest = new LogoutUserRequest();
        logoutRequest.setUsername("PenIsUp");
        LogoutUserResponse logoutResponse = userService.logout(logoutRequest);
        assertThat(logoutResponse.getUsername(), is("penisup"));
    }


    @Test
    void testThatICanNotLoginIfIDontRegister(){
        LoginUserRequest loginRequest = new LoginUserRequest();
        loginRequest.setUsername("PenIsDown");
        loginRequest.setPassword("NoHoles");

        try{
            userService.login(loginRequest);

        } catch (BigBlogException message){
            assertEquals("User with username penisdown not found", message.getMessage());

        }

        assertThat(userRepository.count(), is(0L));
    }

    @Test
    void testThatICantLogoutIfIDontLogin(){
        LogoutUserRequest logoutRequest = new LogoutUserRequest();
        logoutRequest.setUsername("PenIsDown");
        try{
            userService.logout(logoutRequest);

        } catch (BigBlogException message){
            assertEquals("User with username penisdown not found", message.getMessage());

        }

        assertThat(userRepository.count(), is(0L));
    }

    @Test
    void testThatUserCannotLoginWithWrongPasscodeAfterRegistration(){
        RegisterUserRequest registerRequest = new RegisterUserRequest();
        registerRequest.setFirstname("PenIs");
        registerRequest.setLastname("Up");
        registerRequest.setUsername("PenIsUp");
        registerRequest.setPassword("Holes");
        userService.register(registerRequest);
        assertThat(userRepository.count(), is(1L));

        LoginUserRequest loginRequest = new LoginUserRequest();
        loginRequest.setUsername("PenIsUp");
        loginRequest.setPassword("WrongHole");

        try {
            userService.login(loginRequest);
        } catch (BigBlogException message){
            assertEquals("Invalid password for user penisup", message.getMessage());

        }

        assertThat(userRepository.count(), is(1L));

    }
    @Test
    void testRegisterWithEmptyUsername() {
        RegisterUserRequest registerRequest = new RegisterUserRequest();
        registerRequest.setFirstname("PenIs");
        registerRequest.setLastname("Up");
        registerRequest.setUsername("");
        registerRequest.setPassword("Holes");

        assertThrows(BigBlogException.class, () -> userService.register(registerRequest));
    }

    @Test
    void testRegisterWithNullUsername() {
        RegisterUserRequest registerRequest = new RegisterUserRequest();
        registerRequest.setFirstname("PenIs");
        registerRequest.setLastname("Up");
        registerRequest.setUsername(null);
        registerRequest.setPassword("Holes");

        assertThrows(NullPointerException.class, () -> userService.register(registerRequest));
    }


    @Test
    void testLoginWithEmptyUsername() {
        LoginUserRequest loginRequest = new LoginUserRequest();
        loginRequest.setUsername("");
        loginRequest.setPassword("Holes");

        assertThrows(BigBlogException.class, () -> userService.login(loginRequest));
    }

    @Test
    void testUpdateUserProfile() {
        RegisterUserRequest registerRequest = new RegisterUserRequest();
        registerRequest.setFirstname("PenIs");
        registerRequest.setLastname("Up");
        registerRequest.setUsername("PenIsUp");
        registerRequest.setPassword("Holes");
        RegisterUserResponse userResponse = userService.register(registerRequest);

        UpdateUserRequest updateRequest = new UpdateUserRequest();
        updateRequest.setUserId(userResponse.getUserId());
        updateRequest.setFirstname("Rename");
        updateRequest.setLastname("Pen");
        updateRequest.setUsername("penisup");

        UpdateUserResponse response = userService.updateUserProfile(updateRequest);

        Optional<User> updatedUserOptional = userRepository.findById(userResponse.getUserId());
        assertTrue(updatedUserOptional.isPresent());
        User updatedUser = updatedUserOptional.get();

        assertEquals("penisup", response.getUsername());
        assertEquals("Rename", response.getFirstname());
        assertEquals("Pen", response.getLastname());
        assertEquals(updatedUser.getUserId(), response.getUserId());
        assertEquals(updatedUser.getUsername(), response.getUsername());
        assertEquals(updatedUser.getFirstName(), response.getFirstname());
        assertEquals(updatedUser.getLastName(), response.getLastname());
    }

    @Test
    void testUpdateUserProfile_UserNotFound() {
        UpdateUserRequest updateRequest = new UpdateUserRequest();
        updateRequest.setUserId("nonExistingUserId");
        updateRequest.setFirstname("Rename");
        updateRequest.setLastname("Pen");
        updateRequest.setUsername("penisup");

        assertThrows(UserNotFoundException.class, () -> userService.updateUserProfile(updateRequest));
    }


}