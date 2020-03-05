package cn.com.kernol.advancedcontroller.annotation;

import org.springframework.core.annotation.AliasFor;

import java.lang.annotation.ElementType;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Inherited
public @interface AdvancedTarget {

	@AliasFor("name")
	String value() default "";

	@AliasFor("value")
	String name() default "";
}
