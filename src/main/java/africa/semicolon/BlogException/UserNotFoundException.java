package africa.semicolon.BlogException;

public class UserNotFoundException extends BigBlogException{
    public UserNotFoundException(String message) {
        super(message);
    }
}
