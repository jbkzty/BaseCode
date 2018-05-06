package com.redis.redlock.pubsub;

import redis.clients.jedis.JedisPubSub;

/**
 * @author superKun
 * @date 2018/5/6.
 */
public class Pubsub {

    public static final String UNLOCK_MESSAGE = "UNLOCK";

    public PubsubEntry subscribe(String channel) {
        PubsubEntry entry = new PubsubEntry();
        entry.setPubSub(new PubsubListener(pubsubCommand -> {
            if (channel.equals(pubsubCommand.getChannel()) && UNLOCK_MESSAGE.equals(pubsubCommand.getMessage())) {
                entry.countDown();
            }
        }));
        return entry;
    }

    public void unsubscribe(JedisPubSub pubSub) {
        pubSub.unsubscribe();
    }
}
