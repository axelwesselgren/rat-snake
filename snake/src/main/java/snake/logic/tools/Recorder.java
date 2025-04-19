package snake.logic.tools;

import java.awt.AWTException;
import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.RenderingHints;
import java.awt.Robot;
import java.awt.Toolkit;
import java.awt.image.BufferedImage;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

import snake.logic.enums.Changes;
import snake.logic.model.Model;

public class Recorder implements Runnable, PropertyChangeListener {
    private final Model model;
    private final Thread thread;
    private Rectangle screenRectangle;
    private Robot robot;
    private int fps, width, height;

    public static final Dimension P480 ,P720, P1080, P1440, P2160;

    static {
        P480 = new Dimension(640, 480);
        P720 = new Dimension(1280, 720);
        P1080 = new Dimension(1920, 1080);
        P1440 = new Dimension(2560, 1440);
        P2160 = new Dimension(3840, 2160);
    }

    public Recorder(int fps, Dimension downScale, Model model) {
        this.model = model;
        model.registerPropertyChangeListener(this);

        this.fps = fps;
        this.width = (int) downScale.getWidth();
        this.height = (int) downScale.getHeight();
        
        try {
            robot = new Robot();
        } catch (AWTException e) {
            e.printStackTrace();
        }

        screenRectangle = new Rectangle(Toolkit.getDefaultToolkit().getScreenSize());
        thread = new Thread(this);
    }

    public void start() {
        thread.start();
    }

    @Override
    public void run() {
        while (true) {
            try {
                BufferedImage image = robot.createScreenCapture(screenRectangle);

                BufferedImage resizedImage = new BufferedImage(width, height, BufferedImage.TYPE_BYTE_GRAY);
                Graphics2D g = resizedImage.createGraphics();

                g.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);
                g.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
                g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

                g.drawImage(image, 0, 0, width, height, null);
                g.dispose();

                model.firePropertyChange(Changes.UPDATE_IMAGE, image, resizedImage);

                Thread.sleep(1000 / fps);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void propertyChange(PropertyChangeEvent evt) {

    }
}
