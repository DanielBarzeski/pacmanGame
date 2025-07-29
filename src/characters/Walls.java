package characters;

import logic.Constants;

import java.awt.*;
import java.util.HashSet;

public class Walls {
    private final HashSet<Point> walls;

    public Walls() {
        this.walls = new HashSet<>();
    }

    public void add(Point wall) {
        walls.add(wall);
    }

    public boolean collision(Point location) {
        return walls.contains(location);
    }

    public void draw(Graphics g) {
        for (Point wall : walls) {
            g.setColor(Color.cyan.darker().darker());
            g.fillRoundRect(
                    wall.x * Constants.CELL_SIZE, wall.y * Constants.CELL_SIZE,
                    Constants.CELL_SIZE - 1, Constants.CELL_SIZE - 1, 5, 5);
            g.setColor(Color.cyan);
            g.drawRoundRect(
                    wall.x * Constants.CELL_SIZE, wall.y * Constants.CELL_SIZE,
                    Constants.CELL_SIZE - 1, Constants.CELL_SIZE - 1, 5, 5);
        }
    }
}
