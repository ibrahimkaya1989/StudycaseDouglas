package utils;

import framework.MyTestNGBaseClass;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import oshi.SystemInfo;
import oshi.hardware.*;
import oshi.software.os.FileSystem;
import oshi.software.os.OSFileStore;
import oshi.software.os.OperatingSystem;
import oshi.util.FormatUtil;

import java.util.List;

public class SystemInfoUtil {
    private static final Logger logger = LogManager.getLogger(SystemInfoUtil.class);
    public static SystemInfo systemInfo = new SystemInfo();
    public static HardwareAbstractionLayer hardware = systemInfo.getHardware();
    public static CentralProcessor processor = hardware.getProcessor();
    public static CentralProcessor.ProcessorIdentifier processorIdentifier = processor.getProcessorIdentifier();
    public static GlobalMemory globalMemory = hardware.getMemory();
    public static OperatingSystem operatingSystem = systemInfo.getOperatingSystem();
    public static OperatingSystem.OSVersionInfo versionInfo = operatingSystem.getVersionInfo();
    public static FileSystem fileSystem = operatingSystem.getFileSystem();

    public static void allInfos(){
        printOperatingSystemInfos();
        //printCpuInfos();
        //printRamInfos();
        //printFileSystemInfos();
        //printBatteryInfos();
        //printUsbInfos();
        //printNetworkInfos();
    }

    public static void printOperatingSystemInfos(){
        logger.info("---------------------------------------------");
        logger.info(" Operating System Infos");
        logger.info("---------------------------------------------");

        //logger.info("Operating System: " + operatingSystem.toString());

        logger.info("Family: " + operatingSystem.getFamily());
        logger.info("Manufacturer: " + operatingSystem.getManufacturer());
        logger.info("Number of bits supported by the OS (32 or 64): " + operatingSystem.getBitness());

        logger.info("Version: " + versionInfo.getVersion());
        logger.info("CodeName: " + versionInfo.getCodeName());
        logger.info("Build Number: " + versionInfo.getBuildNumber());

        logger.warn("os.name: " + System.getProperty("os.name"));
        logger.info("user.name: " + System.getProperty("user.name"));
        logger.info("java.version: " + System.getProperty("java.version"));
        logger.info("java.home: " + System.getProperty("java.home"));

        logger.info("");
    }

    public static void printCpuInfos(){
        logger.info("---------------------------------------------");
        logger.info(" CPU Infos");
        logger.info("---------------------------------------------");

        //logger.info(processor.toString());

        logger.info("Processor Name: " + processorIdentifier.getName());
        logger.info("Identifier: " + processorIdentifier.getIdentifier());
        logger.info("Frequency (GHz): " + processorIdentifier.getVendorFreq() / 1000000000.0);
        logger.info("Number of physical packages: " + processor.getPhysicalPackageCount());
        logger.info("Number of physical CPUs: " + processor.getPhysicalProcessorCount());
        logger.info("Number of logical CPUs: " + processor.getLogicalProcessorCount());
        logger.info("Processor Vendor: " + processorIdentifier.getVendor());
        logger.info("Micro Architecture: " + processorIdentifier.getMicroarchitecture());
        logger.info("Processor ID: " + processorIdentifier.getProcessorID());
        logger.info("");
    }

    public static void printRamInfos(){
        logger.info("---------------------------------------------");
        logger.info(" RAM Infos");
        logger.info("---------------------------------------------");

        long usedMemory = globalMemory.getTotal() - globalMemory.getAvailable();
        logger.info("Total memory: " + FormatUtil.formatBytes(globalMemory.getTotal()));
        logger.info("Available memory: " + FormatUtil.formatBytes(globalMemory.getAvailable()));
        logger.info("Used memory: " + FormatUtil.formatBytes(usedMemory));

        List<PhysicalMemory> physicalMemories = globalMemory.getPhysicalMemory();
        for (PhysicalMemory physicalMemory : physicalMemories) {
            logger.info("Manufacturer: " + physicalMemory.getManufacturer());
            logger.info("Memory type: " + physicalMemory.getMemoryType());
            logger.info("Bank/slot label: " + physicalMemory.getBankLabel());
            logger.info("Capacity: " + FormatUtil.formatBytes(physicalMemory.getCapacity()));
            logger.info("Clock speed: " + FormatUtil.formatHertz(physicalMemory.getClockSpeed()));
        }
        logger.info("");
    }

    public static void printFileSystemInfos(){

        List<OSFileStore> osFileStores = fileSystem.getFileStores();

        logger.info("---------------------------------------------");
        logger.info(" File System Infos");
        logger.info("---------------------------------------------");

        for(OSFileStore fileStore : osFileStores) {
            logger.info("Description: " + fileStore.getDescription());
            logger.info("Label: " + fileStore.getLabel());
            logger.info("Logical Volume: " + fileStore.getLogicalVolume());
            logger.info("Mount: " + fileStore.getMount());
            logger.info("Name: " + fileStore.getName());
            logger.info("Options: " + fileStore.getOptions());
            logger.info("Type: " + fileStore.getType());
            logger.info("UUID: " + fileStore.getUUID());
            logger.info("Volume: " + fileStore.getVolume());
            logger.info("Free Space: " + FormatUtil.formatBytes(fileStore.getFreeSpace()));
            logger.info("Total Space: " + FormatUtil.formatBytes(fileStore.getTotalSpace()));
            logger.info("Usable Space: " + FormatUtil.formatBytes(fileStore.getUsableSpace()));
            logger.info("");
        }
    }

    public static void printBatteryInfos(){
        logger.info("---------------------------------------------");
        logger.info(" Battery Infos");
        logger.info("---------------------------------------------");

        List<PowerSource> powerSources = hardware.getPowerSources();
        for (PowerSource powerSource: powerSources) {
            logger.info(powerSource);
            logger.info("");
        }
    }

    public static void printUsbInfos(){
        logger.info("---------------------------------------------");
        logger.info(" USB Infos");
        logger.info("---------------------------------------------");

        List<UsbDevice> usbDevices = hardware.getUsbDevices(false);
        for ( UsbDevice usbDevice: usbDevices ) {
            logger.info(usbDevice);

        }
        logger.info("");
    }

    public static void printNetworkInfos(){
        logger.info("---------------------------------------------");
        logger.info(" Network Infos");
        logger.info("---------------------------------------------");

        List<NetworkIF> networkIFList = hardware.getNetworkIFs();
        for (NetworkIF n: networkIFList ) {
            logger.info("Network " + n.getIfAlias() + "\n" + n);
            /* Only for Ethernet and Wifi
            if (n.getIfAlias().contains("Ethernet") || n.getIfAlias().contains("Wi-Fi")) {
                logger.info(n.getIfAlias());
                logger.info("Name : " + n.getName());
                logger.info("IPV4 : " + Arrays.toString(n.getIPv4addr()));
                logger.info("IPV6 : " + Arrays.toString(n.getIPv6addr()));
                logger.info("Speed : " + n.getSpeed());
                logger.info("MAC : " + n.getMacaddr());
            }*/
        }
        logger.info("");
    }
}
