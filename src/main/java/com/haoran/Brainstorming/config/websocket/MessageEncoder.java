package com.haoran.Brainstorming.config.websocket;

import com.haoran.Brainstorming.util.Message;
import com.alibaba.fastjson.JSON;

import javax.websocket.Encoder;
import javax.websocket.EndpointConfig;

public class MessageEncoder implements Encoder.Text<Message> {
    @Override
    public String encode(Message o) {
        return JSON.toJSONString(o);
    }

    @Override
    public void init(EndpointConfig endpointConfig) {

    }

    @Override
    public void destroy() {

    }
}
