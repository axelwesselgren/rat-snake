package snake.ui;

import java.awt.Dimension;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.IOException;
import java.net.URISyntaxException;

import javax.swing.JFrame;

import snake.logic.Logic;
import snake.tools.FileTools;

public class Snake extends JFrame {
    private Logic logic;

    public Snake() throws IOException, URISyntaxException {
        logic = new Logic();

        setVisible(true);
        setPreferredSize(new Dimension(960, 540));
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
        setTitle("Snake");
        pack();

        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                try {
                    logic.start();
                } catch (Exception e1) {}
            }
        });
    }
}
