package africa.semicolon.services;

import africa.semicolon.BlogException.PostNotFoundException;
import africa.semicolon.BlogException.UserNotFoundException;
import africa.semicolon.data.model.Comment;
import africa.semicolon.data.model.Post;
import africa.semicolon.data.model.User;
import africa.semicolon.data.repositories.CommentRepository;
import africa.semicolon.data.repositories.PostRepository;
import africa.semicolon.data.repositories.UserRepository;

import java.util.List;

public class CommentServiceImpl {

    private CommentRepository commentRepository;
    private PostRepository postRepository;
    private UserRepository userRepository;


    public List<Comment> getComments(String postId){
        Post post = postRepository.findById(postId).orElseThrow(()-> new PostNotFoundException("Post not found")) ;
        return  commentRepository.findByPostId(post.getId());
    }
    public List<Comment> getComments(String postId, String userId){
        User user = userRepository.findById(userId).orElseThrow(()-> new UserNotFoundException("User not found"));
        Post post = postRepository.findById(postId).orElseThrow(()-> new PostNotFoundException("Post not found")) ;
        return  commentRepository.findByPostId(post.getId());
    }
}
