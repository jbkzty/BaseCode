package com.design.chain;

import java.util.Collections;
import java.util.Map;

/**
 * @author spuerKun
 * @date 17/11/3.
 */
public class HandlerImpl implements Handler {

    /**
     * 假设是500米以内送餐
     */
    private final static int MIN_DISTANCE = 800;

    /**
     * 类计数
     */
    private static int count;

    /**
     * 分店号
     */
    private final int number;

    /**
     * 分店的横坐标，用于判断距离
     */
    private double x;

    /**
     * 分店的纵坐标，用于判断距离
     */
    private double y;

    /**
     * 下一个处理者
     */
    private Handler nextHandler;

    /**
     * 菜单
     */
    private Map<String, Integer> menu;

    public HandlerImpl(double x, double y, Map<String, Integer> menu) {
        this.x = x;
        this.y = y;
        this.menu = menu;
        number = ++count;
    }


    @Override
    public void setSuccessor(Handler handler) {
        this.nextHandler = handler;
    }

    /**
     * 如果距离小于500米并且订单中的食物不缺货，则订单成功，否则失败
     *
     * @param order
     * @return
     */
    @Override
    public boolean handleRequest(Order order) {
        if (CommonUtils.getDistance(order.getLat(), order.getLng(), this.x, this.y) < MIN_DISTANCE && !CommonUtils.outOfStock(menu, order.getOrder())) {
            for (String name : order.getOrder().keySet()) {
                menu.put(name, menu.get(name) - order.getOrder().get(name));
            }
            System.out.println("订餐成功，接受订单的分店是：" + this);
            return true;
        }
        if (nextHandler == null) {
            return false;
        }
        return nextHandler.handleRequest(order);
    }

    public Map<String, Integer> getMenu() {
        return Collections.unmodifiableMap(menu);
    }

    public Handler getNextSubbranch() {
        return nextHandler;
    }

    @Override
    public String toString() {
        return "麦当劳分店第" + number + "个";
    }
}
