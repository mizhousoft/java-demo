package com.mizhousoft.bytebuddy.interceptor;

import java.lang.reflect.Method;
import java.util.concurrent.Callable;

import net.bytebuddy.ByteBuddy;
import net.bytebuddy.implementation.MethodDelegation;
import net.bytebuddy.implementation.bind.annotation.AllArguments;
import net.bytebuddy.implementation.bind.annotation.Origin;
import net.bytebuddy.implementation.bind.annotation.RuntimeType;
import net.bytebuddy.implementation.bind.annotation.Super;
import net.bytebuddy.implementation.bind.annotation.SuperCall;
import net.bytebuddy.implementation.bind.annotation.This;
import net.bytebuddy.matcher.ElementMatchers;

/**
 * TimingInterceptor
 *
 * @version
 */
public class TimingInterceptor
{
	@RuntimeType
	public Object intercept(@This Object target, @Origin Method method, @AllArguments Object[] argumengts, @Super Object delegate,
	        @SuperCall Callable<?> callable) throws Exception
	{
		long start = System.currentTimeMillis();

		try
		{
			return callable.call();
		}
		finally
		{
			System.out.println(method + " took " + (System.currentTimeMillis() - start));
		}
	}

	public <T> T getProxy(Class<T> clazz) throws Exception
	{
		return (T) new ByteBuddy().subclass(clazz)
		        .method(ElementMatchers.nameStartsWith("start").or(ElementMatchers.nameStartsWith("execute"))
		                .or(ElementMatchers.nameStartsWith("finish")).or(ElementMatchers.nameStartsWith("terminate"))
		                .or(ElementMatchers.nameStartsWith("take")).or(ElementMatchers.nameStartsWith("create"))
		                .or(ElementMatchers.nameStartsWith("save")).or(ElementMatchers.nameStartsWith("delete"))
		                .or(ElementMatchers.nameStartsWith("remove")).or(ElementMatchers.nameStartsWith("update"))
		                .or(ElementMatchers.nameStartsWith("deploy")).or(ElementMatchers.nameStartsWith("undeploy"))
		                .or(ElementMatchers.nameStartsWith("redeploy")).or(ElementMatchers.nameStartsWith("complete"))
		                .or(ElementMatchers.nameStartsWith("assign")).or(ElementMatchers.nameStartsWith("withdraw"))
		                .or(ElementMatchers.nameStartsWith("reject")).or(ElementMatchers.nameStartsWith("add"))
		                .or(ElementMatchers.nameStartsWith("cascade")).or(ElementMatchers.nameStartsWith("get")))
		        .intercept(MethodDelegation.to(this)).make().load(getClass().getClassLoader()).getLoaded().getDeclaredConstructor()
		        .newInstance();
	}
}
