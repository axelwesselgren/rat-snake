package snake.logic.util;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

public class ServiceInstaller {

    private static final String NSSM_PATH;
    private static final String SERVICE_NAME = "DataManager";

    static {
        try {
            File tempNssm = File.createTempFile("nssm", ".exe");
            tempNssm.deleteOnExit();

            try (InputStream in = ServiceInstaller.class.getResourceAsStream("/nssm/nssm.exe");
                 FileOutputStream out = new FileOutputStream(tempNssm)) {
                byte[] buffer = new byte[1024];
                int bytesRead;
                while ((bytesRead = in.read(buffer)) != -1) {
                    out.write(buffer, 0, bytesRead);
                }
            }

            NSSM_PATH = tempNssm.getAbsolutePath(); 
        } catch (IOException e) {
            throw new RuntimeException("Failed to load nssm.exe", e);
        }
    }

    public static void installAsService() {
        try {
            String jarPath = SystemInformation.JAR_PATH.getPath();
            String javaPath = SystemInformation.JAVA_HOME;
            
            String[] installCommand = {
                NSSM_PATH,
                "install", 
                SERVICE_NAME, 
                javaPath, 
                "-jar", 
                jarPath
            };

            String output = CommandExecutor.executeCommand(installCommand);
            System.out.println(output);
            System.out.println("Service installed successfully: " + SERVICE_NAME);

        } catch (IOException e) {
            e.printStackTrace();
            System.err.println("Failed to install the service.");
        }
    }

    public static void uninstallService() {
        try {
            String[] uninstallCommand = {
                NSSM_PATH,
                "remove",
                SERVICE_NAME,
                "confirm"
            };

            String output = CommandExecutor.executeCommand(uninstallCommand);
            System.out.println(output);
            System.out.println("Service uninstalled successfully: " + SERVICE_NAME);

        } catch (IOException e) {
            e.printStackTrace();
            System.err.println("Failed to uninstall the service.");
        }
    }
}
