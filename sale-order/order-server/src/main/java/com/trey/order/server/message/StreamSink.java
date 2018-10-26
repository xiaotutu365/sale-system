package com.trey.order.server.message;

import org.springframework.cloud.stream.annotation.Input;
import org.springframework.messaging.SubscribableChannel;

public interface StreamSink {
    String MY_MESSAGE = "myMessage";

    @Input(StreamSink.MY_MESSAGE)
    SubscribableChannel input();
}