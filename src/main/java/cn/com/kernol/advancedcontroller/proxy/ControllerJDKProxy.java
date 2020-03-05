package cn.com.kernol.advancedcontroller.proxy;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.Assert;

import java.lang.invoke.MethodHandles;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.lang.reflect.Proxy;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

public class ControllerJDKProxy<T> implements InvocationHandler {

	private final static Logger logger = LoggerFactory.getLogger(ControllerJDKProxy.class);

	private Class<T> controllerInterface;
	private Set<Method> apiMethods;

	public ControllerJDKProxy(Class<T> controllerInterface) {
		this.controllerInterface = controllerInterface;
		initMethods();
	}

	public static <T> Object newProxyInstance(Class<T> controllerInterface) {
		ControllerJDKProxy<T> handler = new ControllerJDKProxy<T>(controllerInterface);
		return Proxy.newProxyInstance(controllerInterface.getClassLoader(), new Class[]{controllerInterface}, handler);
	}

	private void initMethods() {
		Assert.notNull(controllerInterface, "controller接口不能为null");

		Method[] methods = controllerInterface.getMethods();
		apiMethods = new HashSet<>();
		if (methods.length > 0) {
			apiMethods.addAll(Arrays.asList(methods));
		}
	}

	@Override
	public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
		logger.debug("方法调用：" + method.getName());
		if (Object.class.equals(method.getDeclaringClass())) {
			return method.invoke(this, args);
		} else if (isDefaultMethod(method)) {
			return invokeDefaultMethod(proxy, method, args);
		}

		try {
			return AdvancedApiMethod.getApiMethod(method, controllerInterface).execute(args);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	private Object invokeDefaultMethod(Object proxy, Method method, Object[] args) throws Throwable {
		final Constructor<MethodHandles.Lookup> constructor = MethodHandles.Lookup.class.getDeclaredConstructor(Class.class, int.class);
		if (!constructor.isAccessible()) {
			constructor.setAccessible(true);
		}
		final Class<?> declaringClass = method.getDeclaringClass();
		return constructor.newInstance(declaringClass,
				MethodHandles.Lookup.PRIVATE | MethodHandles.Lookup.PROTECTED | MethodHandles.Lookup.PACKAGE | MethodHandles.Lookup.PUBLIC)
				.unreflectSpecial(method, declaringClass)
				.bindTo(proxy)
				.invokeWithArguments(args);
	}

	/**
	 * Backport of java.lang.reflect.Method#isDefault()
	 */
	private boolean isDefaultMethod(Method method) {
		return ((method.getModifiers() & (Modifier.ABSTRACT | Modifier.PUBLIC | Modifier.STATIC)) == Modifier.PUBLIC) &&
				method.getDeclaringClass().isInterface();
	}
}
