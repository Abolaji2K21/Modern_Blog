package africa.semicolon.utils;

import africa.semicolon.data.model.Post;
import africa.semicolon.data.model.User;
import africa.semicolon.dtos.requests.RegisterUserRequest;
import africa.semicolon.dtos.responds.*;
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
        registerUserResponse.setUserId(user.getUserId());
        registerUserResponse.setDateRegistered(DateTimeFormatter.ofPattern("dd-MM-yyyy, hh:mm:ss").format(user.getDateCreated()));
        return registerUserResponse;
    }

    public static UpdateUserResponse mapUpdateUserResponse(User user) {
        UpdateUserResponse response = new UpdateUserResponse();
        response.setUserId(user.getUserId());
        response.setUsername(user.getUsername());
        response.setFirstname(user.getFirstName());
        response.setLastname(user.getLastName());
        response.setDateUpdated(user.getDateUpdated().toString());
        response.setLoggedIn(user.isLoggedIn());
        return response;
    }

//    public static EditPostResponse mapEditPostResponse(Post post) {
//        EditPostResponse response = new EditPostResponse();
//        BeanUtils.copyProperties(post, response);
//        response.setPostId(post.getId());
//        response.setDateCreated(post.getDateTimeCreated().toString());
//        return response;
//    }

}
