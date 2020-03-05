package cn.com.kernol.advancedcontroller.register;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.AnnotatedBeanDefinition;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.config.BeanDefinitionHolder;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.beans.factory.support.GenericBeanDefinition;
import org.springframework.context.annotation.ClassPathBeanDefinitionScanner;
import org.springframework.core.type.filter.AnnotationTypeFilter;

import java.util.Arrays;
import java.util.Set;

import cn.com.kernol.advancedcontroller.annotation.AdvancedController;
import cn.com.kernol.advancedcontroller.proxy.ControllerFactoryBean;

public class ControllerClassPathScanner extends ClassPathBeanDefinitionScanner {

	private Logger logger = LoggerFactory.getLogger(ControllerScannerRegistrar.class);

	public ControllerClassPathScanner(BeanDefinitionRegistry registry, boolean useDefaultFilters) {
		super(registry, useDefaultFilters);
	}

	@Override
	protected Set<BeanDefinitionHolder> doScan(String... basePackages) {
		addIncludeFilter(new AnnotationTypeFilter(AdvancedController.class));
		Set<BeanDefinitionHolder> beanDefinitionHolders = super.doScan(basePackages);

		if (beanDefinitionHolders.isEmpty()) {
			logger.warn("在以下包名下没有找到标记为AdvancedController的接口: '" + Arrays.toString(basePackages) + "'.");
		} else {
			processBeanDefinitions(beanDefinitionHolders);
		}

		return beanDefinitionHolders;
	}

	/**
	 * 注册接口，如果是
	 *
	 * @param beanDefinitions
	 */
	private void processBeanDefinitions(Set<BeanDefinitionHolder> beanDefinitions) {
		GenericBeanDefinition definition;
		for (BeanDefinitionHolder holder : beanDefinitions) {
			definition = (GenericBeanDefinition) holder.getBeanDefinition();
			String beanClassName = definition.getBeanClassName();
			logger.debug("Creating MapperFactoryBean with name '" + holder.getBeanName() + "' and '" + beanClassName + "' " +
					"ControllerInterface");

			try {
				Class<?> beanClazz = Class.forName(beanClassName);
				//在这里，我们可以给该对象的属性注入对应的实例。
				//比如mybatis，就在这里注入了dataSource和sqlSessionFactory，
				// 注意，如果采用definition.getPropertyValues()方式的话，
				// 类似definition.getPropertyValues().add("interfaceType", beanClazz);
				// 则要求在FactoryBean提供setter方法，否则会注入失败
				// 如果采用definition.getConstructorArgumentValues()，
				// 则FactoryBean中需要提供包含该属性的构造方法，否则会注入失败
				definition.getConstructorArgumentValues().addGenericArgumentValue(beanClazz);

				//注意，这里的BeanClass是生成Bean实例的工厂，不是Bean本身。
				// FactoryBean是一种特殊的Bean，其返回的对象不是指定类的一个实例，
				// 其返回的是该工厂Bean的getObject方法所返回的对象。
				definition.setBeanClass(ControllerFactoryBean.class);

				//这里采用的是byType方式注入，类似的还有byName等
				definition.setAutowireMode(GenericBeanDefinition.AUTOWIRE_BY_TYPE);
			} catch (ClassNotFoundException e) {
				e.printStackTrace();
			}

		}
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected boolean isCandidateComponent(AnnotatedBeanDefinition beanDefinition) {
		return beanDefinition.getMetadata().isInterface() && beanDefinition.getMetadata().isIndependent();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected boolean checkCandidate(String beanName, BeanDefinition beanDefinition) {
		if (super.checkCandidate(beanName, beanDefinition)) {
			return true;
		} else {
			logger.warn("Skipping ControllerFactoryBean with name '" + beanName + "' and '" + beanDefinition.getBeanClassName() +
					"' ControllerInterface" + ". Bean already defined with the same name!");
			return false;
		}
	}

}
