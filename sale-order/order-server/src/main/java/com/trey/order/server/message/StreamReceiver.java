package com.trey.order.server.message;

import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.stream.annotation.EnableBinding;
import org.springframework.cloud.stream.annotation.StreamListener;

@Slf4j
@EnableBinding(value = {StreamSink.class})
public class StreamReceiver {

    /**
     * 接收端
     * @param message
     */
    @StreamListener(StreamSink.MY_MESSAGE)
    public void receive(String message) {
        log.info("Received: {}", message);
    }
}