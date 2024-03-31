package africa.semicolon.services;

import africa.semicolon.data.model.User;
import africa.semicolon.dtos.requests.LoginUserRequest;
import africa.semicolon.dtos.requests.LogoutUserRequest;
import africa.semicolon.dtos.requests.RegisterUserRequest;
import africa.semicolon.dtos.responds.LoginUserResponse;
import africa.semicolon.dtos.responds.LogoutUserResponse;
import africa.semicolon.dtos.responds.RegisterUserResponse;
import org.springframework.stereotype.Service;

@Service
public interface UserService {

    RegisterUserResponse register(RegisterUserRequest registerUserRequest);
    LoginUserResponse login(LoginUserRequest loginRequest);
    LogoutUserResponse logout(LogoutUserRequest logoutRequest);
    User findUserBy(String username);
}
