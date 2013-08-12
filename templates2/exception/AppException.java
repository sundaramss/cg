package ${config.project.packageName}.exception;

/**
 * 
 * @author ${config.project.author}
 */
public class AppException extends  Exception {
    
	/** To identify the message */
	String message = null;

    private ErrorCode errorCode;

    public enum ErrorCode{
    	
    }

	public AppException(String message) {
		super(message);
		this.message = message;
	}

    public AppException(String message,ErrorCode errorCode) {
        super(message);
        this.message = message;
        this.errorCode = errorCode;
    }

    public ErrorCode getErrorCode() {
        return errorCode;
    }

    public String toString() {
		return message;
	}

}
