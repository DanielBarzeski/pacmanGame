package characters;

import file.Picture;
import file.Sound;
import logic.Constants;
import logic.Game;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;

public class Food {
    private final ArrayList<Point> apples, cherries;
    private Timer timer;

    public Food() {
        this.apples = new ArrayList<>();
        this.cherries = new ArrayList<>();
        Game.setSCORE(0);
    }

    public void draw(Graphics g) {
        for (Point apple : apples) {
            g.drawImage(Picture.APPLE, apple.x * Constants.CELL_SIZE + Constants.CELL_SIZE / 4,
                    apple.y * Constants.CELL_SIZE + Constants.CELL_SIZE / 4,
                    Constants.CELL_SIZE / 2 + 1, Constants.CELL_SIZE / 2 + 1, null
            );
        }
        for (Point cherry : cherries) {
            g.drawImage(Picture.CHERRY, cherry.x * Constants.CELL_SIZE + 1,
                    cherry.y * Constants.CELL_SIZE + 1,
                    Constants.CELL_SIZE - 2, Constants.CELL_SIZE - 2, null
            );
        }
    }

    public void interactWith(Pacman pacman) {
        for (int i = 0; i < apples.size(); i++) {
            Point apple = apples.get(i);
            if (pacman.getLocation().equals(apple)) {
                apples.remove(apple);
                addToScore(10);
                Sound.playEatingAppleSound();
                return;
            }
        }
        for (int i = 0; i < cherries.size(); i++) {
            Point cherry = cherries.get(i);
            if (pacman.getLocation().equals(cherry)) {
                Ghost.setScaredTimer(Ghost.getScaredTime() + 11);
                Ghost.setSCARED(true);
                if (timer == null) {
                    timer = getTimer();
                    timer.start();
                }
                cherries.remove(cherry);
                addToScore(30);
                Sound.playEatingCherrySound();
                return;
            }
        }
    }

    private Timer getTimer() {
        return new Timer(1000, _ -> {
            if (!Game.isPAUSED() && Ghost.getScaredTime() >= 0)
                Ghost.setScaredTimer(Ghost.getScaredTime() - 1);
            if (Ghost.getScaredTime() < 0) {
                Ghost.setSCARED(false);
                timer.stop();
                timer = null;
            }
        });
    }

    public void addToScore(int amount) {
        Game.setSCORE(Game.getSCORE() + amount);
    }

    public ArrayList<Point> getApples() {
        return apples;
    }

    public ArrayList<Point> getCherries() {
        return cherries;
    }

}
