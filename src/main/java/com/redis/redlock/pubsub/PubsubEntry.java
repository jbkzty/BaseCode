package com.redis.redlock.pubsub;

import redis.clients.jedis.JedisPubSub;

import java.util.concurrent.Semaphore;

/**
 * @author superKun
 * @date 2018/5/6.
 */
public class PubsubEntry {

    private Semaphore latch;

    private JedisPubSub pubSub;

    public PubsubEntry() {
        this.latch = new Semaphore(0);
    }

    public void countDown() {
        latch.release();
    }

    public Semaphore getLatch() {
        return latch;
    }

    public JedisPubSub getPubSub() {
        return pubSub;
    }

    public void setPubSub(JedisPubSub pubSub) {
        this.pubSub = pubSub;
    }
}
