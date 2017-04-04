package com.rackspacecloud.blueflood.utils;

import org.junit.Test;

import static org.junit.Assert.*;

/**
 */
public class NettyUtilsTest {

    private static final String OS_NAME = "os.name";

    @Test
    public void notOnLinux_shouldNotUseEpoll() throws Exception {
        String realOs = System.getProperty(OS_NAME);
        try {
            System.setProperty(OS_NAME, "bogus");
            NettyUtils nettyUtils = NettyUtils.get();

            assertTrue("non-Linux should not use Epoll", !nettyUtils.isEpollAvailable());
        } finally {
            System.setProperty(OS_NAME, realOs);
        }
    }

    @Test
    public void onLinux_epollNotAvailable_shouldNotUseEpoll() throws Exception {
        String realOs = System.getProperty(OS_NAME);
        try {
            System.setProperty(OS_NAME, "linux");
            NettyUtils nettyUtils = NettyUtils.get();

            assertTrue("epoll not available, should not use Epoll", !nettyUtils.isEpollAvailable());
        } finally {
            System.setProperty(OS_NAME, realOs);
        }
    }
}
