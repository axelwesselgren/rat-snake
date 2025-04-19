package snake.logic.util;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.stream.Collectors;

public class CommandExecutor {

    public enum Terminal {
        CMD, POWERSHELL
    }
    
    public static String executeCommand(String... command) throws IOException {
        ProcessBuilder pb = new ProcessBuilder(command);
        pb.redirectErrorStream(true);
        Process process = pb.start();

        try (BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()))) {
            return reader.lines().collect(Collectors.joining(System.lineSeparator()));
        }
    }
}
