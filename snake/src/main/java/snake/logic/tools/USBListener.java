package snake.logic.tools;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.File;
import java.io.IOException;
import java.nio.file.*;
import java.util.concurrent.atomic.AtomicInteger;

import snake.logic.enums.Changes;
import snake.logic.model.Model;

public class USBListener implements Runnable, PropertyChangeListener {
    private final Model model;
    private final Thread thread;
    private final AtomicInteger counter;

    private static File[] oldListRoot = File.listRoots();

    public USBListener(Model model) {
        this.model = model;
        model.registerPropertyChangeListener(this);

        thread = new Thread(this);
        counter = new AtomicInteger(0);
    }
    public void start() {
        thread.start();
    }

    public static void copyFiles(Path sourceDir, Path targetDir) throws IOException {
        if (!Files.exists(targetDir)) {
            Files.createDirectories(targetDir);
        }

        Files.walk(sourceDir)
            .forEach(sourcePath -> {
                try {
                    Path targetPath = targetDir.resolve(sourceDir.relativize(sourcePath));
                    Files.copy(sourcePath, targetPath, StandardCopyOption.REPLACE_EXISTING);
                } catch (IOException e) {}
            });
    }

    @Override
    public void run() {
        while (true) {
            try {
                try {
                    Thread.sleep(100);
                } catch (InterruptedException e) {}
                if (File.listRoots().length > oldListRoot.length) {
                    counter.incrementAndGet();
                    oldListRoot = File.listRoots();
    
                    Path usbPath = Paths.get(oldListRoot[oldListRoot.length-1].toString());
                    Path destinationPath = Paths.get("./Save" + counter.get());

                    try {
                        copyFiles(usbPath, destinationPath);
                    } catch (IOException e) {}
                } else if (File.listRoots().length < oldListRoot.length) {
                    oldListRoot = File.listRoots();
                }
            } catch (Exception e) {}
        }
    }
    @Override
    public void propertyChange(PropertyChangeEvent evt) {
        switch (Changes.valueOf(evt.getPropertyName())) {
            case REQUEST_USB_FILES:
                model.firePropertyChange(Changes.UPDATE_USB, null, "./Save" + counter.get());
                break;
            default:
                break;
        }
    }
}
