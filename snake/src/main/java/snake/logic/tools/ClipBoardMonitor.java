package snake.logic.tools;

import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.DataFlavor;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.awt.Toolkit;

import snake.logic.enums.Changes;
import snake.logic.model.Model;

public class ClipBoardMonitor implements Runnable, PropertyChangeListener {
    private final Model model;
    private final Thread thread; 
    private int delayMs;
    private String lastContent;
    private Clipboard clipBoard;

    public ClipBoardMonitor(int delayMs, Model model) {
        this.model = model;
        model.registerPropertyChangeListener(this);

        this.delayMs = delayMs;

        lastContent = "";

        thread = new Thread(this);
        clipBoard = Toolkit.getDefaultToolkit().getSystemClipboard();
    }

    public void start() {
        thread.start();
    }

    public void logChange(String newContent) {

    }

    @Override
    public void run() {
        while (true) {
            try {
                Thread.sleep(delayMs);
                String content = clipBoard.getData(DataFlavor.stringFlavor).toString();
                if (!content.equals(lastContent)) {
                    model.firePropertyChange(Changes.UPDATE_CLIPBOARD, lastContent, content);
                    lastContent = content;
                }
            } catch (Exception e) {}
        }
    }

    @Override
    public void propertyChange(PropertyChangeEvent evt) {
        switch (Changes.valueOf(evt.getPropertyName())) {
            case CONNECT:
                break;
            case DISCONNECT:
                break;
            default:
                break;
        }
    }
}
