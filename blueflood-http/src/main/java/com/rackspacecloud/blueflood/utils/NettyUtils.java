package com.rackspacecloud.blueflood.utils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Locale;

/**
 */
public class NettyUtils {

    private static final Logger LOG = LoggerFactory.getLogger(NettyUtils.class);
    private boolean useEpoll = false;

    private static NettyUtils INSTANCE = new NettyUtils();

    public static NettyUtils get() { return INSTANCE; }

    private NettyUtils() {}

    public boolean isEpollAvailable() {
        try {
            Class constructor = Class.forName("io.netty.channel.epoll.Epoll");
            if(!System.getProperty("os.name", "").toLowerCase(Locale.US).equals("linux")) {
                LOG.warn("Found Netty\'s native epoll transport, but not running on linux-based operating system. Using NIO instead.");
            } else if(!((Boolean)constructor.getMethod("isAvailable", new Class[0]).invoke((Object)null, new Object[0])).booleanValue()) {
                LOG.warn("Found Netty\'s native epoll transport in the classpath, but epoll is not available. Using NIO instead.",
                        (Throwable)constructor.getMethod("unavailabilityCause", new Class[0]).invoke((Object)null, new Object[0]));
            } else {
                LOG.info("Found Netty\'s native epoll transport in the classpath, using it");
                useEpoll = true;
            }
        } catch (ClassNotFoundException cnfe) {
            LOG.info("Did not find Netty\'s native epoll transport in the classpath, defaulting to NIO.");
        } catch (Exception ex) {
            LOG.warn("Unexpected error trying to find Netty\'s native epoll transport in the classpath, defaulting to NIO.", ex);
        }
        return useEpoll;
    }
}
