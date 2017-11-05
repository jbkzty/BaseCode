package com.design.chain.char2;

import com.design.chain.Handler;
import com.design.chain.Order;

/**
 * 订单管理部门，并不是职责链模式中的角色，但是在职责链模式中，通常情况下会有一个专门负责维护责任链的类
 *
 * @author spuerKun
 * @date 17/11/3.
 */
public class OrderManager {

    private static OrderManager orderManager = new OrderManager();

    private Handler head;

    private Handler last;

    private OrderManager() {
    }

    public static OrderManager getOrderManager() {
        return orderManager;
    }

    /**
     * 注册处理器
     *
     * @param handlers
     */
    public void registerHandler(Handler... handlers) {
        for (Handler handler : handlers) {
            registerSubbranch(handler);
        }
    }

    public void registerSubbranch(Handler handler) {
        if (head == null) {
            last = head = handler;
        } else {
            last.setSuccessor(handler);
            last = handler;
        }
    }

    public boolean handleOrder(Order order) {
        return head.handleRequest(order);
    }
}
