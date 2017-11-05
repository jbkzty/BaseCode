package com.design.chain;

import java.util.HashMap;
import java.util.Map;

/**
 * @author spuerKun
 * @date 17/11/3.
 */
public class Client {

    public static void main(String[] args) {

        //假设初始菜单都是以下这些东西
        Map<String, Integer> menu = new HashMap<>(4);
        menu.put("汉堡", 5);
        menu.put("薯条", 5);
        menu.put("可乐", 5);
        menu.put("雪碧", 5);

        //假设有5个分店
        Handler mcSubbranch1 = new HandlerImpl(0, 0, new HashMap<>(menu));
        Handler mcSubbranch2 = new HandlerImpl(100, 120, new HashMap<>(menu));
        Handler mcSubbranch3 = new HandlerImpl(-100, -120, new HashMap<>(menu));
        Handler mcSubbranch4 = new HandlerImpl(1000, 20, new HashMap<>(menu));
        Handler mcSubbranch5 = new HandlerImpl(-500, 0, new HashMap<>(menu));

        //以下设置职责链
        mcSubbranch4.setSuccessor(mcSubbranch5);
        mcSubbranch3.setSuccessor(mcSubbranch4);
        mcSubbranch2.setSuccessor(mcSubbranch3);
        mcSubbranch1.setSuccessor(mcSubbranch2);

        //开始订餐，假设小左的坐标是900,20
        Map<String, Integer> order = new HashMap<>(3);
        order.put("汉堡", 2);
        order.put("可乐", 1);
        order.put("薯条", 1);

        print(mcSubbranch1);
        System.out.println("------------------------------------------");
        mcSubbranch1.handleRequest(new Order(900, 20, order));
        System.out.println("------------------------------------------");
        print(mcSubbranch1);

    }

    public static void print(Handler subbranch) {
        if (subbranch == null) {
            return;
        }
        do {
            if (subbranch instanceof Handler) {
                System.out.println("[" + subbranch + "]的菜单:" + ((HandlerImpl) subbranch).getMenu());
            }
        } while ((subbranch = ((HandlerImpl) subbranch).getNextSubbranch()) != null);
    }

}
