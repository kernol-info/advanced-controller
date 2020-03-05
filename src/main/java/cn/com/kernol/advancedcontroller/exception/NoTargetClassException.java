package cn.com.kernol.advancedcontroller.exception;

/**
 * 没有目标类异常
 */
public class NoTargetClassException extends RuntimeException {

	public NoTargetClassException() {
	}

	public NoTargetClassException(String message) {
		super(message);
	}

	public NoTargetClassException(String message, Throwable cause) {
		super(message, cause);
	}

	public NoTargetClassException(Throwable cause) {
		super(cause);
	}
}
