package africa.semicolon.services;

import africa.semicolon.data.model.Post;
import africa.semicolon.data.model.User;
import africa.semicolon.dtos.requests.CreatePostRequest;
import africa.semicolon.dtos.requests.DeletePostRequest;
import africa.semicolon.dtos.requests.EditPostRequest;
import africa.semicolon.dtos.responds.CreatePostResponse;
import africa.semicolon.dtos.responds.DeletePostResponse;
import africa.semicolon.dtos.responds.EditPostResponse;
import org.springframework.stereotype.Service;

import java.util.List;

public interface PostService {
    CreatePostResponse createPostWith(CreatePostRequest createPostRequest);
    EditPostResponse editPostWith(EditPostRequest editPostRequest);
    DeletePostResponse deletePostWith(DeletePostRequest deletePostRequest);
    User findUserBy(String username);

    List<Post> getAllPosts();

    List<Post> getUserPost(String username);



    }
