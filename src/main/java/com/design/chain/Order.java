package com.design.chain;

import java.util.Map;

/**
 * 订单类，相当于request
 *
 * @author spuerKun
 * @date 17/11/3.
 */
public class Order {

    private double lat;

    private double lng;

    private Map<String, Integer> order;

    public Order(double lat, double lng, Map<String, Integer> order) {
        this.lat = lat;
        this.lng = lng;
        this.order = order;
    }

    public double getLng() {
        return lng;
    }

    public void setLng(double lng) {
        this.lng = lng;
    }

    public Map<String, Integer> getOrder() {
        return order;
    }

    public void setOrder(Map<String, Integer> order) {
        this.order = order;
    }

    public double getLat() {
        return lat;
    }

    public void setLat(double lat) {
        this.lat = lat;
    }
}
