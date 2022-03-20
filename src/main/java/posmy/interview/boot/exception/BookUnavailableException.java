package posmy.interview.boot.exception;

import java.util.Set;

public class BookUnavailableException extends RuntimeException {

    public BookUnavailableException(Set<Long> keys) {
        super("Book " + keys.toString() + " currently unavailable.");
    }
}
