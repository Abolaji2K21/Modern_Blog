package africa.semicolon.services;

import africa.semicolon.BlogException.BigBlogException;
import africa.semicolon.BlogException.PostNotFoundException;
import africa.semicolon.BlogException.UserNotFoundException;
import africa.semicolon.data.model.Comment;
import africa.semicolon.data.model.Post;
import africa.semicolon.data.model.User;
import africa.semicolon.data.repositories.CommentRepository;
import africa.semicolon.data.repositories.PostRepository;
import africa.semicolon.data.repositories.UserRepository;
import africa.semicolon.dtos.requests.CreateCommentRequest;
import africa.semicolon.dtos.requests.DeleteCommentRequest;
import africa.semicolon.dtos.requests.EditCommentRequest;
import africa.semicolon.dtos.responds.*;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.annotation.Id;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class CommentServiceImpl implements CommentService {

    @Autowired
    private CommentRepository commentRepository;

    @Autowired
    private PostRepository postRepository;

    @Autowired
    private UserRepository userRepository;


    @Override
    public List<Comment> getCommentBy(String postId){
        Post post = postRepository.findByPostId(postId).orElseThrow(()-> new PostNotFoundException("Post not found")) ;
        return  commentRepository.findByPostId(post.getPostId());
    }

    @Override
    public List<Comment> getCommentsByUser(String postId, String userId){
        User user = userRepository.findById(userId).orElseThrow(()-> new UserNotFoundException("User not found"));
        Post post = postRepository.findByPostId(postId).orElseThrow(()-> new PostNotFoundException("Post not found")) ;
        return  commentRepository.findByPostId(post.getPostId());
    }


    @Override
    public CreateCommentResponse createCommentWith(CreateCommentRequest createCommentRequest) {
        findUserBy(createCommentRequest.getUserId());

        postRepository.findByPostId(createCommentRequest.getPostId())
                .orElseThrow(() -> new PostNotFoundException("Post not found"));

        Comment newComment = new Comment();
        BeanUtils.copyProperties(createCommentRequest,newComment);
        commentRepository.save(newComment);

        CreateCommentResponse response = new CreateCommentResponse();
        BeanUtils.copyProperties(newComment, response);
        return response;
    }

    @Override
    public EditCommentResponse editCommentWith(CreateCommentRequest createCommentRequest) {

        Post post = postRepository.findById(createCommentRequest.getPostId())
                .orElseThrow(() -> new PostNotFoundException("Post not found"));


        Comment comment = commentRepository.findByPostIdAndUserId(createCommentRequest.getPostId(), createCommentRequest.getUserId())
                .orElseThrow(() -> new BigBlogException("Comment not found for postId: "));


        if (!createCommentRequest.getUserId().equals(comment.getUserId())) {
            throw new UserNotFoundException("User does not have permission to edit this comment");
        }

        comment.setComment(createCommentRequest.getComment());
        commentRepository.save(comment);
        EditCommentResponse response = new EditCommentResponse();
        BeanUtils.copyProperties(comment, response);
        return response;
    }

    @Override
    public DeleteCommentResponse deleteCommentWith(DeleteCommentRequest deleteCommentRequest) {
        User user = findUserBy(deleteCommentRequest.getUserId());
        Comment comment = commentRepository.findById(deleteCommentRequest.getCommentId())
                .orElseThrow(() -> new BigBlogException("Comment not found"));

        if (!user.getUserId().equals(comment.getUserId())) {
            throw new UserNotFoundException("You are not authorized to delete this post");
        }

        Optional<Post> post = postRepository.findByPostId(comment.getPostId());
        post.get().getComments().remove(comment);
        postRepository.save(post.get());
        commentRepository.delete(comment);
        return new DeleteCommentResponse();
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
