package com.paraller.hystrix_demo;

import java.util.concurrent.TimeUnit;

import com.netflix.hystrix.HystrixCommand;
import com.netflix.hystrix.HystrixCommandGroupKey;

import rx.Observable;
import rx.Observer;
import rx.functions.Action1;

public class RegisterDemo extends HystrixCommand<String> {

	public RegisterDemo(String name) {
		super(HystrixCommandGroupKey.Factory.asKey("ExampleGroup"));
	}
	
	@Override
	protected String run() throws Exception {
		
		TimeUnit.MILLISECONDS.sleep(500);
		System.out.println("run ");
		return "run done";
		
	}

	public static void main(String[] args) {
		registerDemo();
	}

	static void registerDemo() {

		// 注册 观察者事件拦截器
		Observable<String> fs = new RegisterDemo("World").observe();
		
		System.out.println("already invoke run");

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
