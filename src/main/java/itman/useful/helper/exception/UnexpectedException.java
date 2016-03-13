package itman.useful.helper.exception;

public class UnexpectedException extends Exception {
	private static final long serialVersionUID = 1L;

	public UnexpectedException(String msg, Throwable cause) {
		super(msg, cause);
	}

	public UnexpectedException(Throwable cause) {
		super(cause);
	}
}
