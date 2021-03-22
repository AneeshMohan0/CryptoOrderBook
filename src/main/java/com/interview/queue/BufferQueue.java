package com.interview.queue;

import com.interview.data.OrderVO;
import org.springframework.stereotype.Component;

import java.util.concurrent.LinkedBlockingQueue;

@Component
public class BufferQueue {

    private final LinkedBlockingQueue<OrderVO> queue
            = new LinkedBlockingQueue<>(100);

    public LinkedBlockingQueue<OrderVO> getQueue() {
        return queue;
    }
}
