package com.haoran.Brainstorming.config.websocket;

import com.haoran.Brainstorming.util.Message;
import com.alibaba.fastjson.JSON;

import javax.websocket.Decoder;
import javax.websocket.EndpointConfig;

public class MessageDecoder implements Decoder.Text<Message> {
    @Override
    public Message decode(String s) {
        return JSON.parseObject(s, Message.class);
    }

    // Vérifiez si la chaîne json est légale, puis entrez la méthode decode() pour la conversion si elle est légale, et lancez une exception directement si elle est illégale
    @Override
    public boolean willDecode(String s) {
        return JSON.isValid(s);
    }

    @Override
    public void init(EndpointConfig endpointConfig) {

    }

    @Override
    public void destroy() {

    }
}
