package app;

import logic.Game;

import javax.swing.*;
import java.awt.*;

public class MenuPanel extends JPanel {
    private final JButton pause;

    public MenuPanel() {
        setLayout(new FlowLayout());
        setPreferredSize(new Dimension(700, 36));
        setBackground(Color.green);
        pause = new JButton();
        pause.setBackground(Color.orange);
        pause.addActionListener(_ -> {
            Game.setPAUSED(!Game.isPAUSED());
        });
        JButton restart = new JButton("restart");
        restart.setBackground(Color.orange);
        restart.setFocusable(false);
        restart.addActionListener(_ -> {
            Game.setRESTARTING(true);
            pause.setVisible(true);
            revalidate();
            repaint();
        });
        JButton previous = new JButton("prev");
        previous.setFocusable(false);
        previous.setBackground(Color.orange);
        previous.addActionListener(_ -> {
            Game.setLEVEL(Game.getLEVEL() - 1);
            restart.doClick();
        });
        JButton next = new JButton("next");
        next.setFocusable(false);
        next.setBackground(Color.orange);
        next.addActionListener(_ -> {
            Game.setLEVEL(Game.getLEVEL() + 1);
            restart.doClick();
        });
        add(restart);
        add(previous);
        add(next);
        add(pause);
    }

    public void update() {
        if (Game.isFINISHED())
            pause.setVisible(false);
        else if (Game.isPAUSED()) {
            pause.setPreferredSize(new Dimension(90, 26));
            pause.setText("continue");
            pause.requestFocusInWindow();
        } else {
            pause.setPreferredSize(new Dimension(90, 26));
            pause.setText("pause");
            pause.requestFocusInWindow();
        }
        repaint();
    }
}

