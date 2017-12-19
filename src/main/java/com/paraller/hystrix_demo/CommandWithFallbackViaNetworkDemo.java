package com.paraller.hystrix_demo;

import com.netflix.hystrix.HystrixCommand;
import com.netflix.hystrix.HystrixCommandGroupKey;
import com.netflix.hystrix.HystrixCommandKey;
import com.netflix.hystrix.HystrixThreadPoolKey;

public class CommandWithFallbackViaNetworkDemo {
	public static void main(String[] args) {
		CommandWithFallbackViaNetwork commandWithFallbackViaNetwork = new CommandWithFallbackViaNetwork(1);
		String result = commandWithFallbackViaNetwork.execute();
		System.out.println("result is " + result);

	}
}

class CommandWithFallbackViaNetwork extends HystrixCommand<String> {
	private final int id;

	protected CommandWithFallbackViaNetwork(int id) {
		super(Setter.withGroupKey(HystrixCommandGroupKey.Factory.asKey("RemoteServiceX")).andCommandKey(
				HystrixCommandKey.Factory.asKey("GetValueCommand")));
		this.id = id;
	}

	@Override
	protected String run() {
		System.out.println(" -----run------ ");
		throw new RuntimeException("force failure for example");
	}

	@Override
	protected String getFallback() {
		return new FallbackViaNetwork(id).execute();
	}

	private static class FallbackViaNetwork extends HystrixCommand<String> {
		private final int id;

		public FallbackViaNetwork(int id) {
			super(Setter.withGroupKey(HystrixCommandGroupKey.Factory.asKey("RemoteServiceX"))
					.andCommandKey(HystrixCommandKey.Factory.asKey("GetValueFallbackCommand"))
					.andThreadPoolKey(HystrixThreadPoolKey.Factory.asKey("RemoteServiceXFallback")));
			this.id = id;
		}

		@Override
		protected String run() {
			System.out.println("run fallback id:" + id);
			return "fallback result:" + id;
		}

		@Override
		protected String getFallback() {
			return null;
		}
	}
}
