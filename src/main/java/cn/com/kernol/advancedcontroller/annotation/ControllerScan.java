package cn.com.kernol.advancedcontroller.annotation;

import org.springframework.context.annotation.Import;
import org.springframework.core.annotation.AliasFor;

import java.lang.annotation.ElementType;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import cn.com.kernol.advancedcontroller.register.ControllerScannerRegistrar;

@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Import(ControllerScannerRegistrar.class)
@Inherited
public @interface ControllerScan {

	@AliasFor("basePackages")
	String[] value() default {};


	@AliasFor("value")
	String[] basePackages() default {};


	Class<?>[] basePackageClasses() default {};
}
