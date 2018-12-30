package com.webapp.model;

import lombok.*;

import java.io.File;
import java.io.Serializable;
import java.lang.management.ManagementFactory;
import java.text.DecimalFormat;

@ToString
@Getter
@Setter
public class JVMState implements Serializable {

    private String runtimeTotalMemory;
    private String runtimeFreeMemory;
    private String runtimeMaxMemory;
    private String availableProcessors;
    private String systemLoadAverage;

    private String heapMemoryUsed;
    private String heapMemoryInit;
    private String heapMemoryCommitted;
    private String heapMemoryMax;

    private String nonHeapMemoryUsed;
    private String nonHeapMemoryInit;
    private String nonHeapMemoryCommitted;
    private String nonHeapMemoryMax;

    private String threadCount;
    private String totalStartedThreadCount;
    private String peakThreadCount;
    private String totalPhysicalMemory;
    private String totalCpuLoad;
    private String diskSize;

    private String arch;
    private String name;
    private String version;

    private String user;

    public JVMState() {
        double byteToMb = 1024.0 * 1024.0;
        DecimalFormat df = new DecimalFormat("#.##");
        runtimeTotalMemory = df.format(Runtime.getRuntime().totalMemory() / byteToMb) + " Mb";
        runtimeFreeMemory = df.format(Runtime.getRuntime().freeMemory() / byteToMb) + " Mb";
        runtimeMaxMemory = df.format(Runtime.getRuntime().maxMemory() / byteToMb) + " Mb";
        availableProcessors = String.valueOf(Runtime.getRuntime().availableProcessors());
        systemLoadAverage = df.format(ManagementFactory.getOperatingSystemMXBean().getSystemLoadAverage()) + "%";

        heapMemoryUsed = df.format(ManagementFactory.getMemoryMXBean().getHeapMemoryUsage().getUsed() / byteToMb) + " Mb";
        heapMemoryInit = df.format(ManagementFactory.getMemoryMXBean().getHeapMemoryUsage().getInit() / byteToMb) + " Mb";
        heapMemoryCommitted = df.format(ManagementFactory.getMemoryMXBean().getHeapMemoryUsage().getCommitted() / byteToMb) + " Mb";
        heapMemoryMax = df.format(ManagementFactory.getMemoryMXBean().getHeapMemoryUsage().getMax() / byteToMb) + " Mb";

        nonHeapMemoryUsed = df.format(ManagementFactory.getMemoryMXBean().getNonHeapMemoryUsage().getUsed() / byteToMb) + " Mb";
        nonHeapMemoryInit = df.format(ManagementFactory.getMemoryMXBean().getNonHeapMemoryUsage().getInit() / byteToMb) + " Mb";
        nonHeapMemoryCommitted = df.format(ManagementFactory.getMemoryMXBean().getNonHeapMemoryUsage().getCommitted() / byteToMb) + " Mb";
        nonHeapMemoryMax = df.format(ManagementFactory.getMemoryMXBean().getNonHeapMemoryUsage().getMax()) + " Mb";
        if(nonHeapMemoryMax.equals("-1 Mb")) nonHeapMemoryMax = "Not limited";

        threadCount = String.valueOf(ManagementFactory.getThreadMXBean().getThreadCount());
        totalStartedThreadCount = String.valueOf(ManagementFactory.getThreadMXBean().getTotalStartedThreadCount());
        peakThreadCount = String.valueOf(ManagementFactory.getThreadMXBean().getPeakThreadCount());

        arch = String.valueOf(ManagementFactory.getOperatingSystemMXBean().getArch());
        name = String.valueOf(ManagementFactory.getOperatingSystemMXBean().getName());
        version = String.valueOf(ManagementFactory.getOperatingSystemMXBean().getVersion());

        totalPhysicalMemory = df.format(((com.sun.management.OperatingSystemMXBean) ManagementFactory.getOperatingSystemMXBean()).getTotalPhysicalMemorySize() / byteToMb) + " Mb";
        totalCpuLoad =  df.format(((com.sun.management.OperatingSystemMXBean) ManagementFactory.getOperatingSystemMXBean()).getSystemCpuLoad() * 100.0) + "%";
        diskSize = df.format(new File("/").getTotalSpace() / (1000.0 * 1000.0 * 1000.0)) + " Gb";
        user = System.getProperty("user.name");
    }
}