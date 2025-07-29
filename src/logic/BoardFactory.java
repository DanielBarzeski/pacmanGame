package logic;

import characters.Food;
import characters.Ghost;
import characters.Pacman;
import characters.Walls;
import file.Picture;

import java.awt.*;
import java.util.ArrayList;

public class BoardFactory {
    private final byte[][] map;
    private final Pacman pacman;
    private final ArrayList<Ghost> ghosts;
    private final Walls walls;
    private final Food food;
    private boolean win;

    public BoardFactory(byte[][] map) {
        Ghost.setScaredTimer(-1);
        Ghost.setSCARED(false);
        this.map = map;
        this.walls = new Walls();
        this.ghosts = new ArrayList<>();
        this.food = new Food();
        this.win = false;
        Point point = new Point();
        scanMap(point);
        this.pacman = new Pacman(point.x, point.y);
    }

    private void scanMap(Point point) {
        for (int row = 0; row < map.length; row++) {
            for (int col = 0; col < map[0].length; col++) {
                if (map[row][col] == '0')      // every 0 represent an apple.
                    food.getApples().add(new Point(col, row));
                else if (map[row][col] == '1') // every 1 represent a wall.
                    walls.add(new Point(col, row));
                else if (map[row][col] == '2') // every 2 represent a ghost.
                    ghosts.add(new Ghost(col, row));
                else if (map[row][col] == '4') // 4 represent a pacman.
                    point.move(col, row);
                else if (map[row][col] == '5') // every 5 represent a cherry.
                    food.getCherries().add(new Point(col, row));
                // every 3 represent nothing.
            }
        }
    }

    public boolean outOfMap(Point point) {
        return (point.x < 0 || point.y < 0 || point.x >= map[0].length || point.y >= map.length);
    }

    public void adjustToMap(Point location) {
        if (location.x < 0)
            location.x = map[0].length - 1;
        if (location.y < 0)
            location.y = map.length - 1;
        if (location.x >= map[0].length)
            location.x = 0;
        if (location.y >= map.length)
            location.y = 0;
    }

    public void drawGame(Graphics g) {
        food.draw(g);
        if (Ghost.isSCARED()) {
            drawGhosts(g);
            pacman.draw(g);
        } else {
            pacman.draw(g);
            drawGhosts(g);
        }
        walls.draw(g);
        if (Game.isFINISHED()) {
            g.drawImage(win ? Picture.WINNING : Picture.LOSING,
                    (map[0].length * Constants.CELL_SIZE) / 2 - 50, (map.length * Constants.CELL_SIZE) / 2 - 60,
                    100, 100, null
            );
        }
    }

    private void drawGhosts(Graphics g) {
        for (Ghost ghost : ghosts) {
            if (!Ghost.isSCARED())
                ghost.changeSprite(Picture.GHOST);
            else if (pacman.isNextTo(ghost) || pacman.collision(ghost))
                ghost.changeSprite(Picture.GHOST_SCARED_WHITE);
            else
                ghost.changeSprite(Picture.GHOST_SCARED_BLUE);
            ghost.draw(g);
        }
    }

    public Pacman getPacman() {
        return pacman;
    }

    public ArrayList<Ghost> getGhosts() {
        return ghosts;
    }

    public Food getFood() {
        return food;
    }

    public Walls getWalls() {
        return walls;
    }

    public void setWin(boolean win) {
        this.win = win;
    }
}

