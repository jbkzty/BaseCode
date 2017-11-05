package com.design.chain.char2;

import com.design.chain.Handler;
import com.design.chain.HandlerImpl;
import com.design.chain.Order;

import java.util.HashMap;
import java.util.Map;

/**
 * 责任链的优点:
 * (1) 客户端与具体的处理者解构，客户端只认识一个Handler接口，降低了客户端与处理者的耦合度
 * (2) 客户端和处理者都不关心职责链的具体结构，而是交给了职责链的创造者(类似我们创建的manager类)
 * 也正是因为如此，在职责链中添加处理者的时候，对客户端和处理者来说，都是透明的，二者不知道也没必要知道责任链的变化
 *
 * @author spuerKun
 * @date 17/11/5.
 */
public class Client {

    public static void main(String[] args) {

        //假设初始菜单都是以下这些东西
        Map<String, Integer> menu = new HashMap<String, Integer>();
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

        //注册5个分店
        OrderManager.getOrderManager().registerHandler(mcSubbranch1, mcSubbranch2, mcSubbranch3, mcSubbranch4, mcSubbranch5);

        //小左开始订餐，假设小左的坐标是900,20
        Map<String, Integer> order = new HashMap<String, Integer>();
        order.put("汉堡", 2);
        order.put("可乐", 1);
        order.put("薯条", 1);

        print(mcSubbranch1);
        System.out.println("------------------------------------------");

        //小左开始订餐，直接找订餐管理部门订餐
        OrderManager.getOrderManager().handleOrder(new Order(900, 20, order));

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
