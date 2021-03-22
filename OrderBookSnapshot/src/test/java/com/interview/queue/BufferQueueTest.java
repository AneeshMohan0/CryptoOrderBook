package com.interview.queue;

import com.interview.data.OrderVO;
import com.interview.workflow.Exchange;
import com.interview.util.Util;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

class BufferQueueTest {

    private final BufferQueue bufferQueue = new BufferQueue();

    @Test
    public void testQueue() throws InterruptedException {
        Exchange exchange = Util.getExchange();
        bufferQueue.getQueue().put(exchange.getOrderVO());

        Assertions.assertFalse(bufferQueue.getQueue().isEmpty());

        OrderVO take = bufferQueue.getQueue().take();

        Assertions.assertTrue(bufferQueue.getQueue().isEmpty());
        Assertions.assertEquals(exchange.getOrderVO(), take);
    }
}