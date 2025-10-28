package app;


import file.Sound;
import logic.Board;
import logic.Constants;
import logic.Game;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

public class GamePanel extends JPanel {
    private Board board;

    public GamePanel() {
        setBackground(Color.black);
        Game.setLEVEL(2);
        reset();
        setupKeyBindings();
    }

    public void update(int[] timeCounter) {
        if (Game.isRESTARTING()) {
            reset();
            revalidate();
            Game.setRESTARTING(false);
        }
        if (this.board.isUpdating()) {
            updateGame(timeCounter);
            repaint();
        }
    }

    private void updateGame(int[] timeCounter) {
        if (!Game.isFINISHED() && !Game.isPAUSED()) {
            this.board.updateRules();
            this.board.moveGhosts();
            this.board.canPacMove();
            if (timeCounter[0] == 4) {
                this.board.updateFood();
                this.board.movePacman();
                timeCounter[0] = 0;
            }
            timeCounter[0]++;
        }
    }

    private void reset() {
        Sound.rewindBackgroundSound();
        byte[][] map = readByteArrayFromFile();
        setPreferredSize(new Dimension(
                        map[0].length * Constants.CELL_SIZE,
                        map.length * Constants.CELL_SIZE
                )
        );
        this.board = new Board(map);
        Game.START();
        Sound.playBackgroundSound();
    }

    private void setupKeyBindings() {
        InputMap inputMap = getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW);
        ActionMap actionMap = getActionMap();
        Object[][] keyActions = {
                {"LEFT", "moveLeft", (Runnable) () -> board.getPacman().goLeft()},
                {"RIGHT", "moveRight", (Runnable) () -> board.getPacman().goRight()},
                {"UP", "moveUp", (Runnable) () -> board.getPacman().goUp()},
                {"DOWN", "moveDown", (Runnable) () -> board.getPacman().goDown()}
        };

        for (Object[] keyAction : keyActions) {
            String key = (String) keyAction[0];
            String actionName = (String) keyAction[1];
            Runnable action = (Runnable) keyAction[2];
            inputMap.put(KeyStroke.getKeyStroke(key), actionName);
            actionMap.put(actionName, new AbstractAction() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    if (!Game.isFINISHED() && !Game.isPAUSED()) {
                        action.run();
                    }
                }
            });
        }
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2d = (Graphics2D) g;
        g2d.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);
        this.board.drawGame(g);
    }

    private byte[][] readByteArrayFromFile() {
        ArrayList<byte[]> lines = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(
                new FileReader(
                        "levels/level_" + Game.getLEVEL()
                )
        )) {
            String line;
            while ((line = reader.readLine()) != null) {
                lines.add(line.getBytes());
            }
        } catch (IOException e) {
            Game.setLEVEL(0);
            return readByteArrayFromFile();
        }

        byte[][] byteArray = new byte[lines.size()][];
        for (int i = 0; i < lines.size(); i++) {
            byteArray[i] = lines.get(i);
        }

        return byteArray;
    }

}

