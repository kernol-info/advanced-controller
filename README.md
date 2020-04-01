# advanced-controller
@AdvancedController是一个增强型的RestController注解，可以使用接口来定义Controller类，自动匹配Service类并调用Service中的方法。
简单示例：

`TestController.java`
```
@RequestMapping("/test")
@AdvancedController(TestService.class)
public interface TestCotroller {

	@RequestMapping("hello")
	String callHello();
}
```

`TestService.java`
```
@Service
public class TestService {

	public String callHello() {
		return "hello world!";
	}
}
```
