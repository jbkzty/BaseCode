package com.redis.redlock.pubsub;

/**
 * @author superKun
 * @date 2018/5/6.
 */
public class PubsubCommand {

    private String channel;

    private String message;

    public PubsubCommand(String channel, String message) {
        this.channel = channel;
        this.message = message;
    }

    public String getChannel() {
        return channel;
    }

    public String getMessage() {
        return message;
    }
}
