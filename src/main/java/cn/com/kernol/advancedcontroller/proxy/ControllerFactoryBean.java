package cn.com.kernol.advancedcontroller.proxy;

import org.springframework.beans.factory.SmartFactoryBean;

public class ControllerFactoryBean implements SmartFactoryBean<Object> {

	private Class<?> interfaceType;

	public ControllerFactoryBean(Class<?> interfaceType) {
		this.interfaceType = interfaceType;
	}

	@Override
	public Object getObject() {
		return ControllerJDKProxy.newProxyInstance(interfaceType);
	}

	@Override
	public Class<?> getObjectType() {
		return interfaceType;
	}

	@Override
	public boolean isSingleton() {
		return true;
	}

	@Override
	public boolean isEagerInit() {
		return true;
	}
}
