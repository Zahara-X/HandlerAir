package org.example.handlers.gui;

import org.example.NettyClient;
import org.example.handlers.gui.entity.ArrayEntity;
import org.example.handlers.gui.entity.Player;
import org.example.handlers.gui.entity.enum_0.Bits;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.HashSet;
import java.util.Set;

public class NettyGuiClient extends JPanel {
    private static final ArrayEntity player = new Player();
    private final Set<Integer> keys = new HashSet<>();
    public NettyGuiClient() {
        this.setFocusable(true);
        this.requestFocusInWindow();
        this.setBackground(Color.BLACK);
        this.addKeyListener(new KeyAdapter() {
            @Override public void keyPressed(KeyEvent e) {keys.add(e.getKeyCode());}
            @Override public void keyReleased(KeyEvent e) {keys.remove(e.getKeyCode());}
        });

        new Timer(16, e -> {
            if(keys.contains(KeyEvent.VK_W)) player.move(Bits.W.getKey());
            if(keys.contains(KeyEvent.VK_S)) player.move(Bits.S.getKey());
            if(keys.contains(KeyEvent.VK_A)) player.move(Bits.A.getKey());
            if(keys.contains(KeyEvent.VK_D)) player.move(Bits.D.getKey());
            this.repaint();
        }).start();
    }
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D) g;
        g2.setColor(Color.RED);
        g2.fillRect(player.getX(), player.getY(), player.getWidth(), player.getHeight());
        Toolkit.getDefaultToolkit().sync();
    }
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            JFrame window = new JFrame("frame");
            window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            window.setSize(new Dimension(1000, 650));
            window.setLocationRelativeTo(null);
            window.add(new NettyGuiClient());
            window.setVisible(true);
        });
        new NettyClient("localhost", 8085, player);
    }
}