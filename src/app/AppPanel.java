package app;

import file.Picture;

import javax.swing.*;
import java.awt.*;

public class AppPanel extends JPanel {
    private GamePanel gamePanel;
    private MenuPanel menuPanel;
    private DisplayPanel displayPanel;

    public AppPanel() {
        setLayout(new FlowLayout());
        setPreferredSize(new Dimension(710, 600));
        setBackground(Color.cyan.darker().darker());
        JButton start = new JButton("START   ");

        start.setBackground(Color.cyan);
        start.setIcon(new ImageIcon(Picture.PLAIN_PACMAN));
        start.addActionListener(_ -> {
            remove(start);
            this.displayPanel = new DisplayPanel();
            add(this.displayPanel);
            this.gamePanel = new GamePanel();
            JPanel panel = new JPanel();
            panel.setLayout(new GridBagLayout());
            panel.setPreferredSize(new Dimension(700, 505));
            panel.setBackground(Color.blue.darker().darker());
            panel.add(this.gamePanel, new GridBagConstraints());
            add(panel);
            this.menuPanel = new MenuPanel();
            add(this.menuPanel);
            revalidate();
            run();
        });
        add(start);
    }

    private void run() {
        final int[] timeCounter = {0};
        new Timer(70, _ -> {
            this.gamePanel.update(timeCounter);
            this.menuPanel.update();
            this.displayPanel.update();
        }).start();
    }
}
