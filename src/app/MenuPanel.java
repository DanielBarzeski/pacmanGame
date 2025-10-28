package app;

import file.Sound;
import logic.Game;

import javax.swing.*;
import java.awt.*;

public class MenuPanel extends JPanel {
    private final JButton pause, sound;
    private final JPanel p;

    public MenuPanel() {
        setLayout(new FlowLayout());
        setPreferredSize(new Dimension(880, 36));
        setBackground(Color.blue);
        pause = new JButton();
        pause.setPreferredSize(new Dimension(90, 26));
        p = new JPanel();
        p.setBackground(getBackground());
        p.setPreferredSize(new Dimension(90, 26));
        p.setVisible(false);
        pause.setBackground(Color.cyan);
        pause.addActionListener(_ -> Game.setPAUSED(!Game.isPAUSED()));
        sound = new JButton(" mute ");
        sound.setFocusable(false);
        sound.setBackground(Color.cyan);
        sound.addActionListener(_ -> Game.setSOUND(!Game.isSOUND()));
        JButton restart = new JButton("restart");
        restart.setBackground(Color.cyan);
        restart.setFocusable(false);
        restart.addActionListener(_ -> {
            Game.setRESTARTING(true);
            pause.setVisible(true);
            revalidate();
            repaint();
        });
        JButton previous = new JButton("prev");
        previous.setFocusable(false);
        previous.setBackground(Color.cyan);
        previous.addActionListener(_ -> {
            Game.setLEVEL(Game.getLEVEL() - 1);
            restart.doClick();
        });
        JButton next = new JButton("next");
        next.setFocusable(false);
        next.setBackground(Color.cyan);
        next.addActionListener(_ -> {
            Game.setLEVEL(Game.getLEVEL() + 1);
            restart.doClick();
        });
        add(restart);
        add(sound);
        add(pause);
        add(p);
        add(previous);
        add(next);
    }

    public void update() {
        p.setVisible(Game.isFINISHED());
        pause.setVisible(!Game.isFINISHED());
        if (!Game.isFINISHED()) pause.setText(Game.isPAUSED() ? "continue" : "pause");
        if (!Game.isFINISHED()) {
            if (Game.isSOUND()) {
                sound.setText(" mute ");
                Sound.playBackgroundSound();
            } else {
                sound.setText("sound");
                Sound.stopBackgroundSound();
            }
        }
        pause.requestFocusInWindow();
        repaint();
    }
}

