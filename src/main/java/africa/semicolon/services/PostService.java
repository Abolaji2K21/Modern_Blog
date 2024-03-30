package africa.semicolon.services;

import africa.semicolon.data.model.Post;
import africa.semicolon.dtos.requests.CreatePostRequest;
import africa.semicolon.dtos.requests.DeletePostRequest;
import africa.semicolon.dtos.requests.EditPostRequest;
import africa.semicolon.dtos.responds.DeletePostResponse;
import org.springframework.stereotype.Service;

@Service
public interface PostService {
    Post createPostWith(CreatePostRequest createPostRequest);
    Post editPostWith(EditPostRequest editPostRequest);
    DeletePostResponse deletePostWith(DeletePostRequest deletePostRequest);


}
