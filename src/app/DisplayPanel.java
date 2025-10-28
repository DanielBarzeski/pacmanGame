package app;

import characters.Ghost;
import characters.Pacman;
import file.Picture;
import logic.Game;

import javax.swing.*;
import java.awt.*;

public class DisplayPanel extends JPanel {
    public DisplayPanel() {
        setPreferredSize(new Dimension(880, 40));
        setBackground(Color.blue);
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        g.setColor(Color.yellow);
        g.fillRoundRect(130, 5, 165, 30, 10, 10);
        g.fillRoundRect(430, 5, 110, 30, 10, 10);
        g.fillRoundRect(320, 5, 80, 30, 10, 10);
        g.setColor(Color.black);
        g.drawString("Level " + Game.getLEVEL(), 340, 25);
        g.drawString("Score: " + Game.getSCORE(), 150, 25);
        if (Ghost.getScaredTime() >= 0) {
            g.drawImage(Picture.GHOST_SCARED_BLUE.getSubimage(0, 0, 16, 16),
                    222, 10, 20, 20, null);
            g.drawString(" -> " + Ghost.getScaredTime() + "s", 242, 25);
        }
        for (int i = 0; i < Pacman.getLIFE(); i++) {
            g.drawImage(Picture.HEART, 450 + 24 * i, 12, 20, 20, null);
        }
    }

    public void update() {
        repaint();
    }
}
