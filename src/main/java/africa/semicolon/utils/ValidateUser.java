//package africa.semicolon.utils;
//
//import africa.semicolon.BlogException.UserExistsException;
//import africa.semicolon.data.repositories.UserRepository;
//import lombok.Data;
//import lombok.RequiredArgsConstructor;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.stereotype.Service;
//
//@Data
//@Service
//public class ValidateUser {
//
//    @Autowired
//    private UserRepository userRepository;
//
//    public void validate(String username) {
//        boolean userExists = userRepository.existsByUsername(username);
//        if (userExists) throw new UserExistsException(String.format("%s already exists", username));
//    }
//}
