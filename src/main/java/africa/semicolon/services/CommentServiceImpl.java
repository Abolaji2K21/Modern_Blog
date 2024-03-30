package africa.semicolon.services;

import africa.semicolon.BlogException.PostNotFoundException;
import africa.semicolon.BlogException.UserNotFoundException;
import africa.semicolon.data.model.Comment;
import africa.semicolon.data.model.Post;
import africa.semicolon.data.model.User;
import africa.semicolon.data.repositories.CommentRepository;
import africa.semicolon.data.repositories.PostRepository;
import africa.semicolon.data.repositories.UserRepository;
import africa.semicolon.dtos.requests.CreateCommentRequest;
import africa.semicolon.dtos.responds.CreateCommentResponse;
import africa.semicolon.dtos.responds.CreatePostResponse;
import org.springframework.beans.BeanUtils;
import org.springframework.data.annotation.Id;

import java.time.LocalDateTime;
import java.util.List;

public class CommentServiceImpl implements CommentService {

    private CommentRepository commentRepository;
    private PostRepository postRepository;
    private UserRepository userRepository;


    public List<Comment> getCommentBy(String postId){
        Post post = postRepository.findById(postId).orElseThrow(()-> new PostNotFoundException("Post not found")) ;
        return  commentRepository.findByPostId(post.getId());
    }
    public List<Comment> getCommentsByUser(String postId, String userId){
        User user = userRepository.findById(userId).orElseThrow(()-> new UserNotFoundException("User not found"));
        Post post = postRepository.findById(postId).orElseThrow(()-> new PostNotFoundException("Post not found")) ;
        return  commentRepository.findByPostId(post.getId());
    }


    @Override
    public CreateCommentResponse createCommentWith(CreateCommentRequest createCommentRequest) {
        User user = findUserBy(createCommentRequest.getUsername());
        Comment newComment = new Comment();
        BeanUtils.copyProperties(createCommentRequest,newComment);
        newComment.setUserId(user.getId());
        commentRepository.save(newComment);

        CreateCommentResponse response = new CreateCommentResponse();
        BeanUtils.copyProperties(newComment,response);
        return response;
    }

    @Override
    public User findUserBy(String username) {
        User user = userRepository.findByUsername(username);
        if (user == null) {
            throw new UserNotFoundException("User with username " + username + " not found");
        }
        return user;
    }
}
