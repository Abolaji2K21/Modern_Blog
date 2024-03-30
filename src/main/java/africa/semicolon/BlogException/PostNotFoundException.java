package africa.semicolon.BlogException;

public class PostNotFoundException extends BigBlogException{
    public PostNotFoundException(String message) {
        super(message);
    }
}
