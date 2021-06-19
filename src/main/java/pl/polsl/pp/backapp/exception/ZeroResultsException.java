package pl.polsl.pp.backapp.exception;

public class ZeroResultsException extends RuntimeException {

    public ZeroResultsException(String keyword) {
        super("It didn't find any results for keyword " + keyword + " ");
    }
}
