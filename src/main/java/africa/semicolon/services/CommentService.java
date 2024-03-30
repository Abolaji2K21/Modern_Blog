package africa.semicolon.services;

import africa.semicolon.data.model.Comment;
import africa.semicolon.data.model.User;
import africa.semicolon.dtos.requests.CreateCommentRequest;
import africa.semicolon.dtos.requests.CreatePostRequest;
import africa.semicolon.dtos.responds.CreateCommentResponse;
import africa.semicolon.dtos.responds.CreatePostResponse;

import java.util.List;

public interface CommentService {

    User findUserBy(String username);

    List<Comment> getCommentBy(String postId);
    List<Comment> getCommentsByUser(String postId, String userId);
    CreateCommentResponse createCommentWith(CreateCommentRequest createCommentRequest);

}
