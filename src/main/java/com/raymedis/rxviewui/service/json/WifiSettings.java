package com.raymedis.rxviewui.service.json;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;

public class WifiSettings {

    private static final Logger logger = LoggerFactory.getLogger(WifiSettings.class);

    public static List<String> getWifiIpAddress() {
        List<String> ipList = new ArrayList<>();
        try {
            Enumeration<NetworkInterface> nets = NetworkInterface.getNetworkInterfaces();
            while (nets.hasMoreElements()) {
                NetworkInterface netInterface = nets.nextElement();
                String name = netInterface.getDisplayName().toLowerCase();

                if (netInterface.isUp() && !netInterface.isLoopback() &&
                        (name.contains("qualcomm atheros") || name.contains("tp-link") || name.contains("wi-fi") || name.contains("wireless") || name.contains("wlan"))) {

                    Enumeration<InetAddress> inetAddresses = netInterface.getInetAddresses();
                    while (inetAddresses.hasMoreElements()) {
                        InetAddress inetAddress = inetAddresses.nextElement();
                        if (inetAddress instanceof Inet4Address) {
                            ipList.add(inetAddress.getHostAddress());
                        }
                    }
                }
            }
        } catch (SocketException e) {
            e.printStackTrace();
        }
        return ipList;
    }

    public static void createBatchFileForSettingIp(String fileName, String IP_ADDRESS, String SUBNET_MASK, String GATEWAY) {
        try {
            File batFile = new File(fileName);
            FileWriter writer = new FileWriter(batFile);

            logger.info(IP_ADDRESS);

            writer.write("@echo off\n");
            writer.write("echo Setting Static IP...\n");
            writer.write("netsh interface ipv4 set address name=\"Wi-Fi\" static " + IP_ADDRESS + " " + SUBNET_MASK + " " + GATEWAY + "\n");
            writer.write("echo Static IP set successfully!\n");
            writer.write("pause\n");
            writer.write("exit\n");

            writer.close();
            logger.info("Batch file created: {} ",  fileName);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    public static void createBatchFileForSettingAutomaticIp(String fileName) {
        try {
            // Create a new batch file
            File batFile = new File(fileName);
            FileWriter writer = new FileWriter(batFile);

            // Write the commands to the batch file
            writer.write("@echo off\n");
            writer.write("echo Setting DHCP for " + "Wi-Fi" + "...\n");
            writer.write("netsh interface ipv4 set address name=\"Wi-Fi\" source=dhcp\n");
            writer.write("echo DHCP set successfully!\n");
            writer.write("pause\n");
            writer.write("exit\n");

            // Close the writer
            writer.close();
            logger.info("Batch file created: {}", fileName);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void runAsAdmin(String batFile) {
        try {
            String command = "powershell -Command \"Start-Process cmd -ArgumentList '/c start " + batFile + "' -Verb RunAs\"";
            ProcessBuilder pb = new ProcessBuilder("cmd", "/c", command);
            pb.start();
           logger.info("Running batch file as admin...");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }



}
