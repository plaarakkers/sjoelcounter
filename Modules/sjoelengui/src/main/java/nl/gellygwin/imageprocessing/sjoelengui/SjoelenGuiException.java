package nl.gellygwin.imageprocessing.sjoelengui;

/**
 *
 * SjoelenGuiException
 */
public class SjoelenGuiException extends RuntimeException {

    public SjoelenGuiException() {
    }

    public SjoelenGuiException(String message) {
        super(message);
    }
    
    public SjoelenGuiException(String message, Throwable throwable) {
        super(message, throwable);
    }
}
