package cn.com.kernol.advancedcontroller.annotation;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.core.annotation.AliasFor;
import org.springframework.web.bind.annotation.RestController;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 增强RestController，标记在接口上。直接替换{@link RestController}注解。
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@RestController
public @interface AdvancedController {

	/**
	 * 目标类或接口,此类必须是SpringBean
	 */
	@AliasFor("target") Class<?> value() default Void.class;

	/**
	 * 目标类或接口,此类必须是SpringBean
	 */
	@AliasFor("value") Class<?> target() default Void.class;

	/**
	 * 目标类的名称，可用于多个实现类时，指定具体哪一个类。功能类似{@link Qualifier}
	 */
	String qualifier() default "";
}
