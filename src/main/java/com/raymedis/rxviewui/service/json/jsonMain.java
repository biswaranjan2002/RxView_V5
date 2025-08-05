package com.raymedis.rxviewui.service.json;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class jsonMain {

    public static void main1(String[] args) throws Exception {

        File jsonFile = new File("Raymedis Config.json");


        DetectorDetails d1 = new DetectorDetails(0, "ASDFKAJ", "INNO343423", FpdSDK.RAD_INNOCARE, "Wifi", "255.255.255.1");

        DetectorDetails d2 = new DetectorDetails(1, "FGAFG", "Pix345645654", FpdSDK.RAD_PIXGEN, "Wifi", "255.255.255.2");

        DetectorDetails d3 = new DetectorDetails(2, "DFJDFJ", "Care3446456546723", FpdSDK.RAD_CARERAY, "Wifi", "255.255.255.3");
        // Create a list of users

        ApplicationDetails a1 = new ApplicationDetails(ApplicationType.RADIOGRAPHY, 1, d1,d2,d3, new ArrayList<>(List.of(FpdSDK.RAD_PIXGEN, FpdSDK.RAD_INNOCARE, FpdSDK.RAD_CARERAY)));

        ApplicationDetails a2 = new ApplicationDetails(ApplicationType.FLUOROSCOPY, 2, d1,d2,d3, new ArrayList<>(List.of(FpdSDK.RAD_PIXGEN, FpdSDK.RAD_CARERAY)));


        RaymedisConfig user1 = new RaymedisConfig(new ArrayList<>(List.of(a1,a2)));

        JsonService.writeJson(List.of(user1), jsonFile.getAbsolutePath());



        // Read JSON into a List of Person objects
        List<RaymedisConfig> people = JsonService.readJson(jsonFile, RaymedisConfig.class);

        // Print each object
        people.forEach(System.out::println);

    }

    public static void main(String[] args) {
        String batFileName = "set_static_ip.bat";

        // Create the BAT file if it doesn't exist
        createBatchFileForSettingIp(batFileName, "192.168.3.10", "255.255.255.0", "192.168.3.1");


        createBatchFileForSettingAutomaticIp(batFileName);

        // Run the BAT file as administrator
        String projectPath = System.getProperty("user.dir");

        runAsAdmin(projectPath + "/" + batFileName);
    }


    private final static Logger logger = LoggerFactory.getLogger(jsonMain.class);

    private static void createBatchFileForSettingIp(String fileName, String IP_ADDRESS, String SUBNET_MASK, String GATEWAY) {
        try {
            File batFile = new File(fileName);
            FileWriter writer = new FileWriter(batFile);

            writer.write("@echo off\n");
            writer.write("echo Setting Static IP...\n");
            writer.write("netsh interface ipv4 set address name=\"Wi-Fi\" static " + IP_ADDRESS + " " + SUBNET_MASK + " " + GATEWAY + "\n");
            writer.write("echo Static IP set successfully!\n");
            writer.write("pause\n");
            writer.write("exit\n");

            writer.close();
            logger.info("Batch file created: {}" , fileName);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static void createBatchFileForSettingAutomaticIp(String fileName) {
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
            logger.info("Batch file created: {}" , fileName);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    private static void runAsAdmin(String batFile) {
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
