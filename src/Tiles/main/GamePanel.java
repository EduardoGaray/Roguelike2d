package Tiles.main;

import Tiles.entity.Player;
import Tiles.tile.TileManager;

import javax.swing.*;
import java.awt.*;

public class GamePanel extends JPanel implements Runnable {

    //SCREEN SETTINGS
    public final int originalTileSize = 16; //16x16 tile
    public final int scale = 3; // scale to resolution

    public int tileSize = originalTileSize * scale; //48x48 tile
    public int maxScreenCol = 16; // ^ v
    public int maxScreenRow = 12; // <>
    public int screenWidth = tileSize * maxScreenCol; //768 px
    public int screenHeight = tileSize * maxScreenRow; //576 px

    //WORLD SETTINGS
    public final int maxWorldCol = 50;
    public final int maxWorldRow = 50;
    public final int worldWidth = tileSize * maxWorldCol;
    public final int worldHeight = tileSize * maxWorldRow;

    //FPS
    int fps = 60;

    TileManager tileM = new TileManager(this);
    KeyHandler keyH = new KeyHandler(this);
    Thread gameThread; //When we call this thread, it automatically calls its run() method
    public CollisionChecker cChecker = new CollisionChecker(this);
    public Player player = new Player(this, keyH);


    public GamePanel() {
        this.setPreferredSize(new Dimension(screenWidth, screenHeight));
        this.setBackground(Color.BLACK);
        this.setDoubleBuffered(true);
        this.addKeyListener(keyH);
        this.setFocusable(true);
    }

    public void zoomInOut(int i) {

        int oldWorldWidth = tileSize * maxWorldCol;
        tileSize += i;
        int newWorldWidth = tileSize * maxWorldCol;

        player.speed = (double)newWorldWidth/600;
        double multiplier = (double) newWorldWidth / oldWorldWidth;
        double newPlayerWorldX = player.worldX * multiplier;
        double newPlayerWorldY = player.worldY * multiplier;

        player.worldX = newPlayerWorldX;
        player.worldY = newPlayerWorldY;

    }

    public void startGameThread() {
        gameThread = new Thread(this);
        gameThread.start();
    }

    @Override
    public void run() {
        double drawInterval = 1000000000 / fps;
        double delta = 0;
        long lastTime = System.nanoTime();
        long currentTime;
        long timer = 0;
        int drawCount = 0;
        while (gameThread != null) {

            currentTime = System.nanoTime();
            delta += (currentTime - lastTime) / drawInterval;
            timer += (currentTime - lastTime);
            lastTime = currentTime;

            if (delta > 1) {
                update();
                repaint();
                delta--;
                drawCount++;
            }

            if (timer >= 1000000000) {
                //System.out.println("FPS:" + drawCount);
                drawCount = 0;
                timer = 0;
            }
        }
    }

    public void update() {
        player.update();
    }

    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D) g;
        tileM.draw(g2);
        player.draw(g2);
        g2.dispose();
    }

}
