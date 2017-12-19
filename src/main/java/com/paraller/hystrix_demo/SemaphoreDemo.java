package com.paraller.hystrix_demo;

import com.netflix.hystrix.HystrixCommand;
import com.netflix.hystrix.HystrixCommandGroupKey;
import com.netflix.hystrix.HystrixCommandProperties;

public class SemaphoreDemo extends HystrixCommand<String> {

	private final String name;

	public SemaphoreDemo(String name) {
		super(Setter.withGroupKey(HystrixCommandGroupKey.Factory.asKey("HelloWorldGroup"))
				.andCommandPropertiesDefaults(
						HystrixCommandProperties.Setter().withExecutionIsolationStrategy(
								HystrixCommandProperties.ExecutionIsolationStrategy.SEMAPHORE)));
		this.name = name;
	}

	@Override
	protected String run() throws Exception {
		return "HystrixThread:" + Thread.currentThread().getName();
	}

	public static void main(String[] args) throws Exception {
		SemaphoreDemo command = new SemaphoreDemo("semaphore");
		String result = command.execute();
		System.out.println(result);
		System.out.println("MainThread:" + Thread.currentThread().getName());
	}
}