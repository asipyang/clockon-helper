package itman.useful.helper.exception;

public class EndEarlyException extends Exception {
	private static final long serialVersionUID = 1L;

	public EndEarlyException(String msg) {
		super(msg);
	}

	public EndEarlyException(String msg, Throwable cause) {
		super(msg, cause);
	}
}
