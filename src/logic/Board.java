package logic;

import characters.Ghost;
import characters.Pacman;
import file.Sound;

import java.awt.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;
import java.util.*;

public class Board extends BoardFactory {

    private boolean update = true, movePac = true;

    public Board(byte[][] map) {
        super(map);
    }

    public boolean isUpdating() {
        return update;
    }

    public void updateRules() {
        setWin(getFood().getApples().isEmpty() && getFood().getCherries().isEmpty());
        if (isWin() || Pacman.getLIFE() == 0) {
            Sound.stopBackgroundSound();
            Game.END();
            return;
        }
        for (int i = 0; i < getGhosts().size(); i++) {
            Ghost ghost = getGhosts().get(i);
            if (!Ghost.isSCARED() && ghost.isKilled())
                ghost.revive();
            if (getPacman().collision(ghost)) {
                if (Ghost.isSCARED()) {
                    if (!ghost.isKilled()) {
                        new Thread(() -> {
                            ghost.kill();
                            try {
                                Thread.sleep(110);
                            } catch (InterruptedException ignored) {
                            }
                            getFood().addToScore(20);
                            Sound.playEatingGhostSound();
                            ghost.reset();
                        }).start();
                    }
                } else {
                    Sound.playEatingPacmanSound();
                    getPacman().getSpriteBounds().x = 48;
                    update = false;
                    new Thread(() -> {
                        try {
                            Thread.sleep(2200);
                        } catch (InterruptedException ignored) {
                        }
                        for (Ghost g : getGhosts()) {
                            g.reset();
                        }
                        getPacman().kill();
                        update = true;
                        movePac = true;
                    }).start();
                    return;
                }
            }
        }
    }

    public void updateFood() {
        getFood().interactWith(getPacman());
    }

    public void canPacMove() {
        if (!Ghost.isSCARED()) {
            for (Ghost ghost : getGhosts()) {
                if (Ghost.getDELAY() == 6 && (getPacman().isNextTo(ghost) || getPacman().collision(ghost))) {
                    movePac = false;
                    return;
                }
            }
        }
    }

    public void movePacman() {
        if (movePac) {
            Point newPacLocation = new Point(
                    getPacman().getLocation().x + getPacman().getNewDirection().x,
                    getPacman().getLocation().y + getPacman().getNewDirection().y
            );
            Point curPacLocation = new Point(
                    getPacman().getLocation().x + getPacman().getCurrentDirection().x,
                    getPacman().getLocation().y + getPacman().getCurrentDirection().y
            );
            adjustToMap(newPacLocation);
            adjustToMap(curPacLocation);
            if (!getWalls().collision(newPacLocation)) {
                getPacman().setCurrentDirection(getPacman().getNewDirection());
                getPacman().setLocation(newPacLocation);
            } else if (!getWalls().collision(curPacLocation))
                getPacman().setLocation(curPacLocation);
        }
    }

    public void moveGhosts() {
        if (!getPacman().isStaying()) {
            if (Ghost.getDELAY() == 7) {
                for (Ghost ghost : getGhosts()) {
                    moveGhost(ghost);
                }
            }
            Ghost.setDELAY(Ghost.getDELAY() + 1);
            if (Ghost.getDELAY() > 7) Ghost.setDELAY(0);
        }
    }

    private void moveGhost(Ghost ghost) {
        if (!ghost.isKilled()) {
            go(ghost);
            ghost.setLocation(new Point(
                    ghost.getLocation().x + ghost.getCurrentDirection().x,
                    ghost.getLocation().y + ghost.getCurrentDirection().y)
            );
        }
    }


    private void go(Ghost ghost) {
        Point next;
        if (!Ghost.isSCARED()) {
            if (getPacman().isNextTo(ghost)) {
                ghost.goTo(getPacman().getLocation());
                return;
            }
            next = findShortestPath(ghost);
        } else {
            if (getPacman().isNextTo(ghost) || getPacman().collision(ghost)) {
                ghost.stay();
                return;
            }
            next = findFarthestMove(ghost);
        }
        if (next == null) {
            ArrayList<Point> possibleMoves = getNeighbors(ghost.getLocation(), ghost);
            if (possibleMoves.isEmpty())
                ghost.stay();
            else {
                Collections.shuffle(possibleMoves);
                ghost.goTo(possibleMoves.getFirst());
            }
        } else
            ghost.goTo(next);
    }

    /// Greedy search algorithm
    private Point findFarthestMove(Ghost ghost) {
        ArrayList<Point> neighbors = getNeighbors(ghost.getLocation(), ghost);
        Point farthestPoint = null;
        double maxDistance = -1;
        for (Point neighbor : neighbors) {
            double distance = neighbor.distance(getPacman().getLocation());
            if (distance > maxDistance) {
                maxDistance = distance;
                farthestPoint = neighbor;
            }
        }
        return farthestPoint;
    }

    /// BFS algorithm
    private Point findShortestPath(Ghost ghost) {
        Queue<Point> queue = new LinkedList<>();
        Map<Point, Point> cameFrom = new HashMap<>();
        Set<Point> visited = new HashSet<>();
        queue.add(ghost.getLocation());
        visited.add(ghost.getLocation());
        while (!queue.isEmpty()) {
            Point current = queue.poll();
            if (current.equals(getPacman().getLocation()))
                break;
            for (Point neighbor : getNeighbors(current, ghost)) {
                if (!visited.contains(neighbor)) {
                    queue.add(neighbor);
                    visited.add(neighbor);
                    cameFrom.put(neighbor, current);
                }
            }
        }
        Point current = getPacman().getLocation();
        while (cameFrom.containsKey(current) && !cameFrom.get(current).equals(ghost.getLocation()))
            current = cameFrom.get(current);
        if (current.equals(ghost.getLocation()) || current.equals(getPacman().getLocation()))
            return null;
        return current;
    }

    private ArrayList<Point> getNeighbors(Point point, Ghost ghost) {
        ArrayList<Point> neighbors = new ArrayList<>();
        neighbors.add(new Point(point.x + 1, point.y));
        neighbors.add(new Point(point.x - 1, point.y));
        neighbors.add(new Point(point.x, point.y + 1));
        neighbors.add(new Point(point.x, point.y - 1));
        for (int i = 0; i < neighbors.size(); i++) {
            Point neighbor = neighbors.get(i);
            if (getWalls().collision(neighbor) || ghost.collision(getGhosts(), neighbor) || outOfMap(neighbor)) {
                neighbors.remove(i);
                i--;
            }
        }
        return neighbors;
    }
}
