package com.vanilla.rpc.hyxtrix;

import com.netflix.hystrix.*;
import com.vanilla.rpc.Invoker;
import com.vanilla.rpc.MidMan;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/**
 * 熔断proxy
 * 
 * 
 * @author chenqunhui
 *
 */

public class RpcHystrixCommand extends HystrixCommand {

    /**
     * 远程目标方法
     */
    private Method method;

    /**
     * 远程目标接口
     */
    private Object obj;

    /**
     * 远程方法所需要的参数
     */
    private Object[] params;

    private MidMan midMan;
    /**
     * hystrix的上下文信息
     */
    private HystrixContext context;

    public RpcHystrixCommand(Object obj, Method method, Object[] params, MidMan midMan, HystrixContext context) {

        super(HystrixCommand.Setter.withGroupKey(HystrixCommandGroupKey.Factory.asKey(midMan.getClazz().getName()))
                .andCommandKey(HystrixCommandKey.Factory.asKey(method.getName()))
                .andCommandPropertiesDefaults(
                        HystrixCommandProperties.Setter()
                                .withCircuitBreakerEnabled(true)
                                .withCircuitBreakerRequestVolumeThreshold(10)
                                .withCircuitBreakerErrorThresholdPercentage(context.getErrorThresholdPercentage())
                                .withCircuitBreakerSleepWindowInMilliseconds(5 * 1000)
                                .withMetricsRollingStatisticalWindowInMilliseconds(10 * 1000)
                                .withExecutionTimeoutEnabled(false)
                                .withExecutionIsolationStrategy(HystrixCommandProperties.ExecutionIsolationStrategy.SEMAPHORE)
                                .withExecutionIsolationSemaphoreMaxConcurrentRequests(100)
                )
        );
        this.obj = obj;
        this.method = method;
        this.params = params;

        this.midMan = midMan;
        this.context = context;
    }

    @Override
    protected Object run() throws Exception {
        return midMan.invoke(obj, method, params);
    }

    @Override
    protected Object getFallback() {

        Method[] methods = this.context.getFallbackObject().getClass().getMethods();
        for (Method methodFallback : methods) {
            if (this.method.getName().equals(methodFallback.getName())) {
                try {
                    return methodFallback.invoke(context.getFallbackObject(), this.params);
                } catch (IllegalAccessException e) {
                } catch (InvocationTargetException e) {
                }
            }
        }
        return null;
    }


    public static void main(String[] args) throws Exception {

        //RpcHystrixCommand cmd = new RpcHystrixCommand(null,null);
        //System.out.println(cmd.execute());

        //RpcHystrixCommand cmd2 = new RpcHystrixCommand(null,null);
        //System.out.println(cmd2.execute());
    }

}
