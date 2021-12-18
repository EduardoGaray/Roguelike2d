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

    KeyHandler keyH = new KeyHandler();
    Thread gameThread; //When we call this thread, it automatically calls its run() method

    //set the player default position
    int playerX = 100;
    int playerY = 100;
    int playerSpeed = 4;

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
        while(gameThread != null){
            //System.out.println("The game loop is running");

            // 1 UPDATE: Information such as character position
            update();
            //2 DRAW: draw the screen with the updated information
            repaint();

        }
    }
    public void update(){
        if(keyH.upPressed == true){
            playerY -= playerSpeed;
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
