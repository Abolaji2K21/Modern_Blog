package africa.semicolon.services;

import africa.semicolon.BlogException.InvalidPassCodeException;
import africa.semicolon.BlogException.UserExistsException;
import africa.semicolon.BlogException.UserNotFoundException;
import africa.semicolon.data.model.User;
import africa.semicolon.dtos.requests.LoginUserRequest;
import africa.semicolon.dtos.requests.RegisterUserRequest;
import africa.semicolon.dtos.responds.LoginUserResponse;
import africa.semicolon.dtos.responds.LogoutUserResponse;
import africa.semicolon.dtos.responds.RegisterUserResponse;
import africa.semicolon.data.repositories.UserRepository;
import africa.semicolon.utils.Mapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

import static africa.semicolon.utils.Mapper.map;

@Service
public class UserServiceImpl implements UserService {


    @Autowired
    private UserRepository userRepository;


    @Override
    public RegisterUserResponse register(RegisterUserRequest registerUserRequest) {
        String username = registerUserRequest.getUsername().toLowerCase();
        registerUserRequest.setUsername(username);
        validate(username.toLowerCase());
        User myUser = map(registerUserRequest);
        RegisterUserResponse result = map(myUser);
        userRepository.save(myUser);
        return result;
    }

    @Override
    public LoginUserResponse login(LoginUserRequest loginUserRequest) {
        String username = loginUserRequest.getUsername();
        String password = loginUserRequest.getPassword();

        User user = userRepository.findByUsername(username);
        if (user == null) {
            throw new UserNotFoundException("User with username " + username + " not found");
        }

        if (!password.equals(user.getPassword())) {
            throw new InvalidPassCodeException("Invalid password for user " + username);
        }

        return new LoginUserResponse();
    }

    @Override
    public LogoutUserResponse logout(LogoutUserResponse logoutRequest) {
        return null;
    }

    @Override
    public User findUserBy(String username) {
        User user = userRepository.findByUsername(username);
        if (user == null) {
            throw new UserNotFoundException("User with username " + username + " not found");
        }
        return user;
    }


    private void validate(String username) {
        boolean userExists = userRepository.existsByUsername(username);
        if (userExists) throw new UserExistsException(String.format("%s already exists", username));
    }
}
