package cn.com.kernol.advancedcontroller.register;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.BeanFactoryAware;
import org.springframework.beans.factory.support.BeanDefinitionBuilder;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.context.annotation.ImportBeanDefinitionRegistrar;
import org.springframework.core.type.AnnotationMetadata;

public class AutoConfiguredControllerScannerRegistrar implements BeanFactoryAware, ImportBeanDefinitionRegistrar {

	private static final Logger logger = LoggerFactory.getLogger(AutoConfiguredControllerScannerRegistrar.class);

	private BeanFactory beanFactory;

	@Override
	public void setBeanFactory(BeanFactory beanFactory) throws BeansException {
		this.beanFactory = beanFactory;
	}

	@Override
	public void registerBeanDefinitions(AnnotationMetadata importingClassMetadata, BeanDefinitionRegistry registry) {
		if (registry.containsBeanDefinition(ControllerScannerConfigurer.class.getName())) {
			logger.debug("已经注册ControllerScannerConfigurer，跳过！");
			return;
		}

		BeanDefinitionBuilder builder = BeanDefinitionBuilder.genericBeanDefinition(ControllerScannerConfigurer.class);
		registry.registerBeanDefinition(ControllerScannerConfigurer.class.getName(), builder.getBeanDefinition());
	}
}
