package com.addressbook.server;

import org.apache.ignite.Ignition;
import org.apache.ignite.configuration.IgniteConfiguration;
import org.apache.ignite.spi.discovery.tcp.TcpDiscoverySpi;
import org.apache.ignite.spi.discovery.tcp.ipfinder.multicast.TcpDiscoveryMulticastIpFinder;

import java.util.Collections;

class Server {
    companion object {
        @JvmStatic
        fun main(args: Array<String>) {
            val igniteConfiguration = IgniteConfiguration();
            igniteConfiguration.isPeerClassLoadingEnabled = true;
            igniteConfiguration.setIncludeEventTypes(org.apache.ignite.events.EventType.EVT_TASK_STARTED,
                    org.apache.ignite.events.EventType.EVT_TASK_FINISHED,
                    org.apache.ignite.events.EventType.EVT_TASK_FAILED,
                    org.apache.ignite.events.EventType.EVT_TASK_TIMEDOUT,
                    org.apache.ignite.events.EventType.EVT_TASK_SESSION_ATTR_SET,
                    org.apache.ignite.events.EventType.EVT_TASK_REDUCED,
                    org.apache.ignite.events.EventType.EVT_CACHE_OBJECT_PUT,
                    org.apache.ignite.events.EventType.EVT_CACHE_OBJECT_READ,
                    org.apache.ignite.events.EventType.EVT_CACHE_OBJECT_REMOVED);

            val tcpDiscoverySpi = TcpDiscoverySpi();
            val tcpDiscoveryMulticastIpFinder = TcpDiscoveryMulticastIpFinder();
            tcpDiscoveryMulticastIpFinder.setAddresses(Collections.singleton("localhost:47500..47509"));
            tcpDiscoverySpi.ipFinder = tcpDiscoveryMulticastIpFinder;
            igniteConfiguration.discoverySpi = tcpDiscoverySpi;
            Ignition.start(igniteConfiguration);
        }
    }
}