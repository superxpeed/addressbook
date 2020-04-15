package com.addressbook.dto

import java.io.File
import java.io.Serializable
import java.lang.management.ManagementFactory
import java.text.DecimalFormat

class JavaMetrics : Serializable {

    var runtimeTotalMemory: String? = null
    var runtimeFreeMemory: String? = null
    var runtimeMaxMemory: String? = null
    var availableProcessors: String? = null
    var systemLoadAverage: String? = null

    var heapMemoryUsed: String? = null
    var heapMemoryInit: String? = null
    var heapMemoryCommitted: String? = null
    var heapMemoryMax: String? = null

    var nonHeapMemoryUsed: String? = null
    var nonHeapMemoryInit: String? = null
    var nonHeapMemoryCommitted: String? = null
    var nonHeapMemoryMax: String? = null

    var threadCount: String? = null
    var totalStartedThreadCount: String? = null
    var peakThreadCount: String? = null
    var totalPhysicalMemory: String? = null
    var totalCpuLoad: String? = null
    var diskSize: String? = null

    var arch: String? = null
    var name: String? = null
    var version: String? = null

    var user: String? = null

    init {
        val byteToMb: Double = 1024.0 * 1024.0
        val df = DecimalFormat("#.##")
        runtimeTotalMemory = df.format(Runtime.getRuntime().totalMemory() / byteToMb) + " Mb"
        runtimeFreeMemory = df.format(Runtime.getRuntime().freeMemory() / byteToMb) + " Mb"
        runtimeMaxMemory = df.format(Runtime.getRuntime().maxMemory() / byteToMb) + " Mb"
        availableProcessors = Runtime.getRuntime().availableProcessors().toString()
        systemLoadAverage = df.format(ManagementFactory.getOperatingSystemMXBean().systemLoadAverage) + "%"

        heapMemoryUsed = df.format(ManagementFactory.getMemoryMXBean().heapMemoryUsage.used / byteToMb) + " Mb"
        heapMemoryInit = df.format(ManagementFactory.getMemoryMXBean().heapMemoryUsage.init / byteToMb) + " Mb"
        heapMemoryCommitted = df.format(ManagementFactory.getMemoryMXBean().heapMemoryUsage.committed / byteToMb) + " Mb"
        heapMemoryMax = df.format(ManagementFactory.getMemoryMXBean().heapMemoryUsage.max / byteToMb) + " Mb"

        nonHeapMemoryUsed = df.format(ManagementFactory.getMemoryMXBean().nonHeapMemoryUsage.used / byteToMb) + " Mb"
        nonHeapMemoryInit = df.format(ManagementFactory.getMemoryMXBean().nonHeapMemoryUsage.init / byteToMb) + " Mb"
        nonHeapMemoryCommitted = df.format(ManagementFactory.getMemoryMXBean().nonHeapMemoryUsage.committed / byteToMb) + " Mb"
        nonHeapMemoryMax = df.format(ManagementFactory.getMemoryMXBean().nonHeapMemoryUsage.max) + " Mb"
        if (nonHeapMemoryMax.equals("-1 Mb")) nonHeapMemoryMax = "Not limited"

        threadCount = ManagementFactory.getThreadMXBean().threadCount.toString()
        totalStartedThreadCount = ManagementFactory.getThreadMXBean().totalStartedThreadCount.toString()
        peakThreadCount = ManagementFactory.getThreadMXBean().peakThreadCount.toString()

        arch = ManagementFactory.getOperatingSystemMXBean().arch
        name = ManagementFactory.getOperatingSystemMXBean().name
        version = ManagementFactory.getOperatingSystemMXBean().version

        totalPhysicalMemory = df.format((ManagementFactory.getOperatingSystemMXBean() as com.sun.management.OperatingSystemMXBean).totalPhysicalMemorySize / byteToMb) + " Mb"
        totalCpuLoad = df.format((ManagementFactory.getOperatingSystemMXBean() as com.sun.management.OperatingSystemMXBean).systemCpuLoad * 100.0) + "%"
        diskSize = df.format(File("/").totalSpace / (1000.0 * 1000.0 * 1000.0)) + " Gb"
        user = System.getProperty("user.name")
    }
}