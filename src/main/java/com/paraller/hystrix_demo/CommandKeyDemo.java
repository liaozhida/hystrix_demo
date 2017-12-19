package com.paraller.hystrix_demo;

import com.netflix.hystrix.HystrixCommand;
import com.netflix.hystrix.HystrixCommandGroupKey;
import com.netflix.hystrix.HystrixCommandKey;

public class CommandKeyDemo extends HystrixCommand<String> {

	private final String name;

	public CommandKeyDemo(String name) {
		super(Setter.withGroupKey(HystrixCommandGroupKey.Factory.asKey("ExampleGroup")).andCommandKey(
				HystrixCommandKey.Factory.asKey("HelloWorld")));
		this.name = name;
	}

	@Override
	protected String run() throws Exception {
		return null;
	}

}
