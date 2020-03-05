package cn.com.kernol.advancedcontroller.annotation;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.core.annotation.AliasFor;

import java.lang.annotation.ElementType;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 对特定方法进行特定设置。
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Inherited
public @interface AdvancedApi {

	/**
	 * 目标类中的方法名
	 */
	@AliasFor("name")
	String value() default "";

	/**
	 * 目标类中的方法名
	 */
	@AliasFor("value")
	String name() default "";

	/**
	 * 目标类或接口,此类必须是SpringBean
	 */
	Class<?> target() default Void.class;

	/**
	 * 目标类的名称，可用于多个实现类时，指定具体哪一个类。功能类似{@link Qualifier}
	 */
	String qualifier() default "";
}
