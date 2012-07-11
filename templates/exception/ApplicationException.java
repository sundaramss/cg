package ${config.project.packageName}.exception;

/**
 * 
 * @author ${config.project.author}
 */
public class ApplicationException extends  Exception {
    
    public ApplicationException() {
    }

    public ApplicationException(String message) {
        super(message);
    }

    public ApplicationException(String message, Throwable cause) {
        super(message, cause);
    }

    public ApplicationException(Throwable cause) {
        super(cause);
    }
}
