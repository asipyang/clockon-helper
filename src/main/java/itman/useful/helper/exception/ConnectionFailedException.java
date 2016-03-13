package itman.useful.helper.exception;

public class ConnectionFailedException extends Exception {
	private static final long serialVersionUID = 1L;

	public ConnectionFailedException(String msg) {
		super(msg);
	}

	public ConnectionFailedException(String msg, Throwable cause) {
		super(msg, cause);
	}
}
