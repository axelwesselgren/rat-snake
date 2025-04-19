package snake.logic.model;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;

import snake.logic.enums.Changes;
import snake.logic.util.CommandExecutor;
import snake.logic.util.SystemInformation;

public class Model {
    private final PropertyChangeSupport pcs;

    public Model() throws URISyntaxException {
        pcs = new PropertyChangeSupport(this);
    }

    public void firePropertyChange(Changes change, Object oldValue, Object newValue) {
        pcs.firePropertyChange(change.getChange(), oldValue, newValue);
    }
    public void registerPropertyChangeListener(PropertyChangeListener listener) {
        pcs.addPropertyChangeListener(listener);
    }
    public void removePropertyChangeListener(PropertyChangeListener listener) {
        pcs.removePropertyChangeListener(listener);
    }

    public void cleanup() throws IOException, URISyntaxException {
        deleteShortcut();
        deleteJAR();
    }

    private void deleteShortcut() {
        File startup = new File(SystemInformation.PERSONAL_SETUP + "/Snake.lnk");
        if (startup.exists()) {
            startup.delete();
        }
    }

    private void deleteJAR() throws IOException, URISyntaxException {
        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }

        File jarFile = new File(SystemInformation.JAR_PATH);
    
        if (jarFile.exists()) {
            jarFile.deleteOnExit();
            CommandExecutor.executeCommand("cmd", "/c", "del", "/f", "/q", jarFile.getAbsolutePath());
        }
    }
}
