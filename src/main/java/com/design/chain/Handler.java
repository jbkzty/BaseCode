package com.design.chain;

/**
 * @author spuerKun
 * @date 17/11/3.
 */
public interface Handler {

    /**
     * 设置继承者
     *
     * @param handler
     */
    void setSuccessor(Handler handler);

    /**
     * 调用链处理逻辑
     *
     * @param order
     * @return
     */
    boolean handleRequest(Order order);


}
