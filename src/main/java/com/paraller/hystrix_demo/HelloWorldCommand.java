package com.paraller.hystrix_demo;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import rx.Observable;
import rx.Observer;
import rx.functions.Action1;

import com.netflix.hystrix.HystrixCommand;
import com.netflix.hystrix.HystrixCommandGroupKey;
import com.netflix.hystrix.HystrixCommandProperties;

/**
 * Hello world!
 *
 */

public class HelloWorldCommand extends HystrixCommand<String> {

	private final String name;

	public HelloWorldCommand(String name) {
		// 至少要指定一个 command group name 的值(CommandGroup)
		super(HystrixCommandGroupKey.Factory.asKey("ExampleGroup"));
		this.name = name;
	}

	public HelloWorldCommand(String name, boolean fallback) {
		super(Setter.withGroupKey(HystrixCommandGroupKey.Factory.asKey("HelloWorldGroup"))
		/* The configuration dependent timeout milliseconds, 500 */
		.andCommandPropertiesDefaults(
				HystrixCommandProperties.Setter().withExecutionIsolationThreadTimeoutInMilliseconds(500)));
		this.name = name;
	}
	
	@Override
    protected String getFallback() {
        return "exeucute Falled";
    }
	
	@Override
    protected String run() throws Exception {
        //Sleep 1 seconds, call a timeout
        TimeUnit.MILLISECONDS.sleep(1000);
        return "Hello " + name +" thread:" + Thread.currentThread().getName();
    }

	
	

	public static void main(String[] args) throws Exception {

//		hystrixCommandDemo();
//
//		System.out.println("----------------------");
//
//		registerDemo();
//		
//		System.out.println("----------------------");
		
		fallbackDemo();
	}

	
	static void fallbackDemo(){
		HelloWorldCommand command = new HelloWorldCommand("test-Fallback");
        String result = command.execute();
	}
	
	static void hystrixCommandDemo() throws InterruptedException, ExecutionException, TimeoutException {

		// 每一个 Command object 只能被调用一次
		HelloWorldCommand helloWorldCommand = new HelloWorldCommand("Synchronous-hystrix");

		// 同步的调用代码,等价于: helloWorldCommand.queue().get();
		String result = helloWorldCommand.execute();
		System.out.println("result=" + result);
		// 重复调用会抛出这样的异常信息:This instance can only be executed once. Please
		// instantiate a new instance.
		// helloWorldCommand.execute();

		// 异步的代码调用 //The get 方法不能超过command 定义的 timeout时间, 默认是: 1
		helloWorldCommand = new HelloWorldCommand("Asynchronous-hystrix");
		Future<String> future = helloWorldCommand.queue();
		result = future.get(1000, TimeUnit.MILLISECONDS);
		System.out.println("result=" + result);
		// 重复调用会抛出这样的异常信息:This instance can only be executed once. Please
		// instantiate a new instance.
		// helloWorldCommand.queue();

		System.out.println("mainThread=" + Thread.currentThread().getName());
	}

	static void registerDemo() {

		// 注册 观察者事件拦截器
		Observable<String> fs = new HelloWorldCommand("World").observe();

		// 注册完成的生命周期事件
		fs.subscribe(new Observer<String>() {
			@Override
			public void onCompleted() {
				System.out.println("execute onCompleted");
			}

			@Override
			public void onError(Throwable e) {
				System.out.println("onError " + e.getMessage());
				e.printStackTrace();
			}

			@Override
			public void onNext(String v) {
				System.out.println("onNext: " + v);
			}
		});

		// 观察者订阅事件
		fs.subscribe(new Action1<String>() {
			@Override
			public void call(String result) {
				System.out.println("register demo ");
			}
		});
	}

}
