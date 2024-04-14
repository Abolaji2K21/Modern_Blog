package africa.semicolon.services;

import africa.semicolon.BlogException.BigBlogException;
import africa.semicolon.BlogException.InvalidPassCodeException;
import africa.semicolon.BlogException.UserNotFoundException;
import africa.semicolon.data.model.User;
import africa.semicolon.dtos.requests.LoginUserRequest;
import africa.semicolon.dtos.requests.LogoutUserRequest;
import africa.semicolon.dtos.requests.RegisterUserRequest;
import africa.semicolon.dtos.requests.UpdateUserRequest;
import africa.semicolon.dtos.responds.LoginUserResponse;
import africa.semicolon.dtos.responds.LogoutUserResponse;
import africa.semicolon.dtos.responds.RegisterUserResponse;
import africa.semicolon.data.repositories.UserRepository;
import africa.semicolon.dtos.responds.UpdateUserResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

import static africa.semicolon.utils.Mapper.map;
import static africa.semicolon.utils.Mapper.mapUpdateUserResponse;

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
            userRepository.save(myUser);

            return map(myUser);
        }

        @Override
        public LoginUserResponse login(LoginUserRequest loginUserRequest) {
            String username = loginUserRequest.getUsername().toLowerCase();
            String password = loginUserRequest.getPassword();
            User user = findUserBy(username);
            if (user == null) {
                throw new UserNotFoundException("User with username " + username + " not found");
            }

            if (!password.equals(user.getPassword())) {
                throw new InvalidPassCodeException("Invalid password for user " + username);
            }

            user.setLoggedIn(true);
            userRepository.save(user);
            return new LoginUserResponse(user.getUserId(), user.getUsername().toLowerCase(),user.isLoggedIn());    }

        @Override
        public LogoutUserResponse logout(LogoutUserRequest logoutUserRequest) {
            String username = logoutUserRequest.getUsername().toLowerCase();
            User user = findUserBy(username);
            if (user == null) {
                throw new UserNotFoundException("User with username " + username + " not found");
            } else {
                user.setLoggedIn(false);
                userRepository.save(user);
                return new LogoutUserResponse(user.getUserId(), user.getUsername(),user.isLoggedIn());
            }
        }

        public UpdateUserResponse updateUserProfile(UpdateUserRequest request) {
            String userId = request.getUserId();
            User user = userRepository.findById(userId)
                    .orElseThrow(() -> new UserNotFoundException("User with ID " + userId + " not found"));

            user.setFirstName(request.getFirstname());
            user.setLastName(request.getLastname());
            user.setUsername(request.getUsername());
            user.setDateUpdated(LocalDateTime.now());
            userRepository.save(user);

            return mapUpdateUserResponse(user);

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
            if (username == null || username.isEmpty()) {
                throw new BigBlogException("Username cannot be null or empty");
            }

            String existingUser = username.toLowerCase();
            if (userRepository.existsByUsername(existingUser)) {
                throw new BigBlogException(existingUser + " already exists");
            }
        }


        @Override
        public boolean isUserRegistered(String username) {
            return userRepository.existsByUsername(username);
        }

        @Override
        public boolean isUserLoggedIn(String username) {
            String lowercaseUsername = username.toLowerCase();
            User user = findUserBy(lowercaseUsername);
            return user.isLoggedIn();
        }

    }