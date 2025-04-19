package snake;

import snake.ui.Snake;

public class Main {

    public static void main(String[] args) {
        try {
            new Snake();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}