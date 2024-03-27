package africa.semicolon.services;

import africa.semicolon.dtos.requests.RegisterUserRequest;
import org.springframework.stereotype.Service;

@Service
public interface UserService {

    void register(RegisterUserRequest registerUserRequest);
}
