package com.paraller.hystrix_demo;

import java.util.concurrent.TimeUnit;

import com.netflix.hystrix.HystrixCommand;
import com.netflix.hystrix.HystrixCommandGroupKey;
import com.netflix.hystrix.HystrixCommandKey;
import com.netflix.hystrix.HystrixCommandProperties;

/**
 * Hello world!
 *
 */

public class FallbackDemo extends HystrixCommand<String> {

	private final String name;

	public FallbackDemo(String name) {
		super(Setter.withGroupKey(HystrixCommandGroupKey.Factory.asKey("HelloWorldGroup"))
				.andCommandPropertiesDefaults(
						HystrixCommandProperties.Setter().withExecutionIsolationThreadTimeoutInMilliseconds(500)));
		this.name = name;

	}

	public FallbackDemo(String name, int a) {
		super(Setter.withGroupKey(HystrixCommandGroupKey.Factory.asKey("ExampleGroup"))
		/* HystrixCommandKey factory defined dependent name */
		.andCommandKey(HystrixCommandKey.Factory.asKey("HelloWorld")));
		this.name = name;
	}

	@Override
	protected String getFallback() {
		return "exeucute Falled";
	}

	@Override
	protected String run() {
		try {
			TimeUnit.MILLISECONDS.sleep(1000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		System.out.println("被打断调用getFallback() 后依然会执行");
		return "Hello " + name + " thread:" + Thread.currentThread().getName();
	}

	public static void main(String[] args) throws Exception {

		FallbackDemo command = new FallbackDemo("test-Fallback");
		String result = command.execute();
		System.out.println(result);

	}
}