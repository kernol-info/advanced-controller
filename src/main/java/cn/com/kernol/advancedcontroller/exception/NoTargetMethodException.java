package cn.com.kernol.advancedcontroller.exception;

/**
 * 目标类中没有目标方法
 */
public class NoTargetMethodException extends RuntimeException {

	public NoTargetMethodException() {
	}

	public NoTargetMethodException(String message) {
		super(message);
	}

	public NoTargetMethodException(String message, Throwable cause) {
		super(message, cause);
	}

	public NoTargetMethodException(Throwable cause) {
		super(cause);
	}
}
