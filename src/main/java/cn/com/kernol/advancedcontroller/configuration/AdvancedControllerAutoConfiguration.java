package cn.com.kernol.advancedcontroller.configuration;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

import cn.com.kernol.advancedcontroller.proxy.ControllerFactoryBean;
import cn.com.kernol.advancedcontroller.register.AutoConfiguredControllerScannerRegistrar;
import cn.com.kernol.advancedcontroller.register.ControllerScannerConfigurer;

@Configuration
public class AdvancedControllerAutoConfiguration {

	@Configuration
	@Import(AutoConfiguredControllerScannerRegistrar.class)
	@ConditionalOnMissingBean({ControllerFactoryBean.class, ControllerScannerConfigurer.class})
	public static class ControllerScannerNotFounndConfiguration implements InitializingBean {

		private static final Logger logger = LoggerFactory.getLogger(ControllerScannerNotFounndConfiguration.class);

		@Override
		public void afterPropertiesSet() {
			logger.info("Not found configuration for registering mapper bean using @ControllerScan, AdvancedControllerFactoryBean.");
		}

	}
}
