package cn.com.kernol.advancedcontroller.register;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.support.BeanDefinitionBuilder;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.context.annotation.ImportBeanDefinitionRegistrar;
import org.springframework.core.annotation.AnnotationAttributes;
import org.springframework.core.type.AnnotationMetadata;
import org.springframework.core.type.StandardAnnotationMetadata;
import org.springframework.util.ClassUtils;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import cn.com.kernol.advancedcontroller.annotation.ControllerScan;

/**
 * 配合ControllerScan注解引入此注册类。
 */
public class ControllerScannerRegistrar implements ImportBeanDefinitionRegistrar {

	private Logger logger = LoggerFactory.getLogger(ControllerScannerRegistrar.class);

	@Override
	public void registerBeanDefinitions(AnnotationMetadata annotationMetadata, BeanDefinitionRegistry registry) {
		if (registry.containsBeanDefinition(ControllerScannerConfigurer.class.getName())) {
			logger.debug("已经注册ControllerScannerConfigurer，跳过！");
			return;
		}

		//获取所有注解的属性和值
		AnnotationAttributes annoAttrs =
				AnnotationAttributes.fromMap(annotationMetadata.getAnnotationAttributes(ControllerScan.class.getName()));

		//获取到basePackage的值
		List<String> basePackages = new ArrayList<>();
		basePackages.addAll(
				Arrays.stream(annoAttrs.getStringArray("value")).filter(StringUtils::hasText).collect(Collectors.toList()));

		basePackages.addAll(Arrays.stream(annoAttrs.getStringArray("basePackages")).filter(StringUtils::hasText)
				.collect(Collectors.toList()));

		basePackages.addAll(Arrays.stream(annoAttrs.getClassArray("basePackageClasses")).map(ClassUtils::getPackageName)
				.collect(Collectors.toList()));

		//如果没有设置basePackage 扫描路径,就扫描对应包下面的值
		if(CollectionUtils.isEmpty(basePackages)){
			basePackages.add(((StandardAnnotationMetadata) annotationMetadata).getIntrospectedClass().getPackage().getName());
		}

		BeanDefinitionBuilder builder = BeanDefinitionBuilder.genericBeanDefinition(ControllerScannerConfigurer.class);
		builder.addPropertyValue("basePackage", StringUtils.collectionToCommaDelimitedString(basePackages));
		registry.registerBeanDefinition(ControllerScannerConfigurer.class.getName(), builder.getBeanDefinition());


	}

}
