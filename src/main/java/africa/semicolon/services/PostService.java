package africa.semicolon.services;

import africa.semicolon.data.model.Post;
import africa.semicolon.data.model.User;
import africa.semicolon.dtos.requests.CreatePostRequest;
import africa.semicolon.dtos.requests.DeletePostRequest;
import africa.semicolon.dtos.requests.EditPostRequest;
import africa.semicolon.dtos.responds.CreatePostResponse;
import africa.semicolon.dtos.responds.DeletePostResponse;
import africa.semicolon.dtos.responds.EditPostResponse;
import africa.semicolon.dtos.responds.ActivitiesResponse;

import java.util.List;

public interface PostService {
    CreatePostResponse createPostWith(CreatePostRequest createPostRequest);

    EditPostResponse edit(EditPostRequest editPostRequest);

    DeletePostResponse delete(DeletePostRequest deletePostRequest);

    User findUserBy(String username);

    List<ActivitiesResponse> getAllPosts();

    List<Post> getUserPost(String username);

    ActivitiesResponse getPost(String postId, String username);


}
