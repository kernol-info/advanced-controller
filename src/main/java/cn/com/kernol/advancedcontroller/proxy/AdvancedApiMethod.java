package cn.com.kernol.advancedcontroller.proxy;

import org.springframework.context.ApplicationContext;
import org.springframework.util.StringUtils;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import cn.com.kernol.advancedcontroller.annotation.AdvancedApi;
import cn.com.kernol.advancedcontroller.annotation.AdvancedController;
import cn.com.kernol.advancedcontroller.annotation.AdvancedTarget;
import cn.com.kernol.advancedcontroller.aware.ApplicationContextHolder;
import cn.com.kernol.advancedcontroller.exception.NoTargetClassException;
import cn.com.kernol.advancedcontroller.exception.NoTargetMethodException;

public class AdvancedApiMethod {

	/** 缓存的api接口方法 */
	private final static Map<Method, AdvancedApiMethod> cachedApiMethods = new ConcurrentHashMap<>();
	/** 缓存的目标类标记为{@link AdvancedTarget} 的方法 */
	private final static Map<String, Method> cachedAnnoTargetMethods = new ConcurrentHashMap<>();

	private Method apiMethod;
	private Class<?> apiInterface;

	private Method targetMethod;
	private String targetMethodName;
	private Class<?> targetClass;
	private String targetQualifier;
	private Object target;

	private AdvancedApiMethod(Method apiMethod, Class<?> apiInterface) {
		this.apiMethod = apiMethod;
		this.apiInterface = apiInterface;
		handleTarget();
	}

	static AdvancedApiMethod getApiMethod(Method method, Class<?> apiInterface) {
		return cachedApiMethods.computeIfAbsent(method, m -> new AdvancedApiMethod(m, apiInterface));
	}

	private void handleTarget() {
		ApplicationContext context = ApplicationContextHolder.applicationContext;
		AdvancedApi methodAnno = apiMethod.getDeclaredAnnotation(AdvancedApi.class);
		AdvancedController classAnno = apiInterface.getAnnotation(AdvancedController.class);

		Class<?> targetClass = classAnno.value().equals(Void.class) ? classAnno.target() : classAnno.value();
		String targetQualifier;

		String targetMethodName;
		if (methodAnno == null) {
			//Api方法上没有注解，直接使用类注解
			targetQualifier = classAnno.qualifier();
			targetMethodName = apiMethod.getName();
		} else {
			if (methodAnno.target().equals(Void.class)) {
				//api方法上没有目标类
				targetQualifier = classAnno.qualifier();
			} else {
				//api方法上有目标类
				targetClass = methodAnno.target();
				targetQualifier = methodAnno.qualifier();
			}
			String value = StringUtils.isEmpty(methodAnno.name()) ? methodAnno.value() : methodAnno.name();
			targetMethodName = StringUtils.isEmpty(value) ? apiMethod.getName() : value;
		}

		this.targetClass = targetClass;
		this.targetMethodName = targetMethodName;
		this.targetQualifier = targetQualifier;

		if (!StringUtils.isEmpty(targetQualifier)) {
			this.target = context.getBean(targetQualifier);
		} else {
			this.target = context.getBean(targetClass);
		}

		try {
			//检查apiMethod的参数和targetMethod的参数，要一致才行
			this.targetMethod = this.target.getClass().getMethod(targetMethodName, apiMethod.getParameterTypes());
		} catch (NoSuchMethodException e) {
			System.out.println("没有找到对应的目标方法");
			throw new NoTargetMethodException(
					"没有找到对应的目标方法, targetClass:" + targetClass + ";\ntarget:" + target.getClass().getName() + ";\ntargetQualifier:" +
							targetQualifier + ";\ntargetMethodName:" + targetMethodName);
		}

	}

	Object execute(Object[] args) throws InvocationTargetException, IllegalAccessException {
		if (targetMethod != null && target != null) {
			return targetMethod.invoke(target, args);
		} else if (target == null) {
			throw new NoTargetClassException(
					"没有找到对应的目标类, targetClass:" + targetClass + ";\ntargetQualifier:" + targetQualifier + ";\ntargetMethodName:" +
							targetMethodName);
		} else {
			throw new NoTargetMethodException(
					"没有找到对应的目标方法, targetClass:" + targetClass + ";\ntarget:" + target.getClass().getName() + ";\ntargetQualifier:" +
							targetQualifier + ";\ntargetMethodName:" + targetMethodName);
		}
	}

}
