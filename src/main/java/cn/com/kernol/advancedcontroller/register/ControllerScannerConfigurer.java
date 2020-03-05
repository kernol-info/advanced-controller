package cn.com.kernol.advancedcontroller.register;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.BeanFactoryAware;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.beans.factory.support.BeanDefinitionRegistryPostProcessor;
import org.springframework.boot.autoconfigure.AutoConfigurationPackages;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.util.StringUtils;

import java.util.List;

import cn.com.kernol.advancedcontroller.aware.ApplicationContextHolder;

public class ControllerScannerConfigurer implements BeanFactoryAware, BeanDefinitionRegistryPostProcessor,  ApplicationContextAware {

	private static final Logger logger = LoggerFactory.getLogger(ControllerScannerConfigurer.class);

	private String basePackage;
	private BeanFactory beanFactory;

	public void setBasePackage(String basePackage) {
		this.basePackage = basePackage;
	}

	@Override
	public void setBeanFactory(BeanFactory beanFactory) throws BeansException {
		this.beanFactory = beanFactory;
	}

	@Override
	public void postProcessBeanDefinitionRegistry(BeanDefinitionRegistry registry) {
		logger.debug("开始查找 @AdvancedController 标注的接口");
		ControllerClassPathScanner scanner = new ControllerClassPathScanner(registry, false);
		String[] packages = null;
		try {
			if (StringUtils.isEmpty(basePackage)) {
				List<String> packageList = AutoConfigurationPackages.get(this.beanFactory);
				packages = StringUtils.toStringArray(packageList);
			} else {
				packages = StringUtils.tokenizeToStringArray(this.basePackage, ConfigurableApplicationContext.CONFIG_LOCATION_DELIMITERS);
			}

			scanner.doScan(packages);
		} catch (IllegalStateException e) {
			logger.debug("查找 @AdvancedController 标注的接口失败，包名：" + StringUtils.arrayToDelimitedString(packages, ","), e);
		}
	}

	@Override
	public void postProcessBeanFactory(ConfigurableListableBeanFactory beanFactory) throws BeansException {

	}

	@Override
	public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
		ApplicationContextHolder.applicationContext = applicationContext;
	}
}
