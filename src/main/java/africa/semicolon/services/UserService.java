package africa.semicolon.services;

import africa.semicolon.data.model.User;
import africa.semicolon.dtos.requests.LoginUserRequest;
import africa.semicolon.dtos.requests.LogoutUserRequest;
import africa.semicolon.dtos.requests.RegisterUserRequest;
import africa.semicolon.dtos.requests.UpdateUserRequest;
import africa.semicolon.dtos.responds.LoginUserResponse;
import africa.semicolon.dtos.responds.LogoutUserResponse;
import africa.semicolon.dtos.responds.RegisterUserResponse;
import africa.semicolon.dtos.responds.UpdateUserResponse;
import org.springframework.stereotype.Service;

@Service
public interface UserService {

    RegisterUserResponse register(RegisterUserRequest registerUserRequest);
    LoginUserResponse login(LoginUserRequest loginRequest);
    LogoutUserResponse logout(LogoutUserRequest logoutRequest);
    UpdateUserResponse updateUserProfile(UpdateUserRequest request);

    User findUserBy(String username);
    boolean isUserRegistered(String username);
    boolean isUserLoggedIn(String username);
}
