package itman.useful.helper.exception;

public class ElementNotFoundException extends Exception {
	private static final long serialVersionUID = 1L;

	public ElementNotFoundException(String msg) {
		super(msg);
	}

	public ElementNotFoundException(String msg, Throwable cause) {
		super(msg, cause);
	}
}
