package com.vanilla.rpc.hyxtrix;

import lombok.Data;

/**
 * @Author: JiangTao
 * @Date: Created on 上午11:22 2018/2/8.
 * @Description:
 */

@Data
public class HystrixContext {
    /**
     * 服务降级的伪装者类对象
     */
    private Object fallbackObject;
   
	/**
     * 错误熔断的百分比，取之范围0～100
     */
    private int errorThresholdPercentage = 50;
    
}
