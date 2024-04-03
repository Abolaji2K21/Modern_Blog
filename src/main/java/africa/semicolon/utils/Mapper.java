package africa.semicolon.utils;

import africa.semicolon.data.model.Post;
import africa.semicolon.data.model.User;
import africa.semicolon.dtos.requests.RegisterUserRequest;
import africa.semicolon.dtos.responds.EditPostResponse;
import africa.semicolon.dtos.responds.LoginUserResponse;
import africa.semicolon.dtos.responds.LogoutUserResponse;
import africa.semicolon.dtos.responds.RegisterUserResponse;
import org.springframework.beans.BeanUtils;

import java.time.format.DateTimeFormatter;

public class Mapper {
    public static User map(RegisterUserRequest registerUserRequest) {
        User user = new User();
        user.setFirstName(registerUserRequest.getFirstname());
        user.setLastName(registerUserRequest.getLastname());
        user.setPassword(registerUserRequest.getPassword());
        user.setUsername(registerUserRequest.getUsername());
        return user;
    }

    public static RegisterUserResponse map(User user){
        RegisterUserResponse registerUserResponse = new RegisterUserResponse();
        registerUserResponse.setUsername(user.getUsername());
        registerUserResponse.setId(user.getId());
        registerUserResponse.setDateRegistered(DateTimeFormatter.ofPattern("dd-MM-yyyy, hh:mm:ss").format(user.getDateCreated()));
        return registerUserResponse;
    }

//    public static EditPostResponse mapEditPostResponse(Post post) {
//        EditPostResponse response = new EditPostResponse();
//        BeanUtils.copyProperties(post, response);
//        response.setPostId(post.getId());
//        response.setDateCreated(post.getDateTimeCreated().toString());
//        return response;
//    }

}
