package com.addressbook.server;

import org.apache.ignite.Ignition;
import org.apache.ignite.configuration.IgniteConfiguration;
import org.apache.ignite.spi.discovery.tcp.TcpDiscoverySpi;
import org.apache.ignite.spi.discovery.tcp.ipfinder.multicast.TcpDiscoveryMulticastIpFinder;

import java.util.Collections;

public class Server {
    public static void main(String[] args) {
        IgniteConfiguration igniteConfiguration = new IgniteConfiguration();
        igniteConfiguration.setPeerClassLoadingEnabled(true);
        igniteConfiguration.setIncludeEventTypes(org.apache.ignite.events.EventType.EVT_TASK_STARTED,
                org.apache.ignite.events.EventType.EVT_TASK_FINISHED,
                org.apache.ignite.events.EventType.EVT_TASK_FAILED,
                org.apache.ignite.events.EventType.EVT_TASK_TIMEDOUT,
                org.apache.ignite.events.EventType.EVT_TASK_SESSION_ATTR_SET,
                org.apache.ignite.events.EventType.EVT_TASK_REDUCED,
                org.apache.ignite.events.EventType.EVT_CACHE_OBJECT_PUT,
                org.apache.ignite.events.EventType.EVT_CACHE_OBJECT_READ,
                org.apache.ignite.events.EventType.EVT_CACHE_OBJECT_REMOVED);

        TcpDiscoverySpi tcpDiscoverySpi = new TcpDiscoverySpi();
        TcpDiscoveryMulticastIpFinder tcpDiscoveryMulticastIpFinder = new TcpDiscoveryMulticastIpFinder();
        tcpDiscoveryMulticastIpFinder.setAddresses(Collections.singleton(System.getenv("IGNITE_SERVER")));
        tcpDiscoverySpi.setIpFinder(tcpDiscoveryMulticastIpFinder);
        igniteConfiguration.setDiscoverySpi(tcpDiscoverySpi);
        Ignition.start(igniteConfiguration);
    }
}
