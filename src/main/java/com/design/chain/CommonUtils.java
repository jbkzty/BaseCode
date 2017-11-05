package com.design.chain;

import java.util.Map;

/**
 * @author spuerKun
 * @date 17/11/3.
 */
public class CommonUtils {

    /**
     * 计算坐标之间的距离
     *
     * @param x1
     * @param y1
     * @param x2
     * @param y2
     * @return
     */
    public static double getDistance(double x1, double y1, double x2, double y2) {
        return Math.sqrt((x1 - x2) * (x1 - x2) + (y1 - y2) * (y1 - y2));
    }

    /**
     * 是否缺货
     *
     * @param menu
     * @param order
     * @return
     */
    public static boolean outOfStock(Map<String, Integer> menu, Map<String, Integer> order) {
        if (order == null || order.size() == 0) {
            return false;
        }
        if (menu == null || menu.size() == 0) {
            return true;
        }
        for (String name : order.keySet()) {
            if (menu.get(name) - order.get(name) < 0) {
                return true;
            }
        }
        return false;
    }
}
