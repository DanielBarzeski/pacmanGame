package app;

import file.Picture;

import javax.swing.*;
import java.awt.*;

public class AppFrame extends JFrame {
    public AppFrame() {
        setTitle("PACMAN GAME");
        setIconImage(Picture.PLAIN_GHOST);
        getContentPane().setBackground(Color.yellow);
        setLayout(new FlowLayout());
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setResizable(false);
        add(new AppPanel());
        pack();
        setLocationRelativeTo(null);
        setVisible(true);
    }
}
