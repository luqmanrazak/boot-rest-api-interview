package posmy.interview.boot.exception;

import java.util.Set;

public class UserInvalidStatusException extends RuntimeException {

    public UserInvalidStatusException(String username) {
        super("User " + username + " status invalid.");
    }
}
