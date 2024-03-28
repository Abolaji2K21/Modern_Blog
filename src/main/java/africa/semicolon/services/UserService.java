package africa.semicolon.services;

import africa.semicolon.dtos.requests.RegisterUserRequest;
import africa.semicolon.dtos.responds.RegisterUserResponse;
import org.springframework.stereotype.Service;

@Service
public interface UserService {

    RegisterUserResponse register(RegisterUserRequest registerUserRequest);
}
