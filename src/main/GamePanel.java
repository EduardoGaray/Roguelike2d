package main;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyListener;

public class GamePanel extends JPanel implements Runnable {

    //SCREEN SETTINGS
    final int originalTileSize = 16; //16x16 tile
    final int scale = 3; // scale to resolution

    final int tileSize = originalTileSize * scale; //48x48 tile
    final int maxScreenCol = 16; // ^ v
    final int maxScreenRow = 12; // <>
    final int screenWidth = tileSize * maxScreenCol; //768 px
    final int screenHeight = tileSize * maxScreenRow; //576 px

    //FPS
    int fps = 60;

    KeyHandler keyH = new KeyHandler();
    Thread gameThread; //When we call this thread, it automatically calls its run() method

    //set the player default position
    int playerX = 100;
    int playerY = 100;
    int playerSpeed = 1;

    public GamePanel(){
        this.setPreferredSize( new Dimension(screenWidth,screenHeight));
        this.setBackground(Color.BLACK);
        this.setDoubleBuffered(true);
        this.addKeyListener(keyH);
        this.setFocusable(true);
    }

    public void startGameThread(){
        gameThread = new Thread(this);
        gameThread.start();
    }

    @Override
    public void run() {
        double drawInterval = 1000000000/fps;
        double delta = 0;
        long lastTime = System.nanoTime();
        long currentTime;
        while(gameThread != null){

            currentTime = System.nanoTime();
            delta += (currentTime - lastTime) / drawInterval;
            lastTime = currentTime;

            if (delta > 1){
                update();
                repaint();
                delta--;
            }
        }
    }
    public void update(){
        if(keyH.upPressed){
            playerY -= playerSpeed;
            //if(playerY <0){playerY = 0;}
        }
        else if(keyH.downPressed) {
            playerY += playerSpeed;
        }
        else if(keyH.leftPressed) {
            playerX -= playerSpeed;
        }
        else if(keyH.rightPressed) {
            playerX += playerSpeed;
        }

    }
    public void paintComponent(Graphics g){
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D)g;
        g2.setColor(Color.white);
        g2.fillRect(playerX,playerY,tileSize,tileSize);
        g2.dispose();
    }

}
