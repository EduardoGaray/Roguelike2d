package entity;

import main.GamePanel;
import main.KeyHandler;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;

public class Player extends Entity {
    GamePanel gp;
    KeyHandler keyH;

    public final int screenX;
    public final int screenY;

    public Player(GamePanel gp, KeyHandler keyH) {
        this.gp = gp;
        this.keyH = keyH;

        screenX = gp.screenWidth / 2 - (gp.tileSize / 2);
        screenY = gp.screenHeight / 2 - (gp.tileSize / 2);

        solidArea = new Rectangle(8,16,32,32);


        setDefaultValues();
        getPlayerImage();
    }

    public void setDefaultValues() {
        worldX = gp.tileSize * 5;
        worldY = gp.tileSize * 5;
        speed = gp.worldWidth /600;
        direction = "down";
    }

    public void getPlayerImage() {
        try {
            frame1 = ImageIO.read(getClass().getResourceAsStream("/player/human1.png"));
            frame2 = ImageIO.read(getClass().getResourceAsStream("/player/human2.png"));

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void update() {
        spriteCounter++;
        if (keyH.upPressed) {
            direction = "up";
            worldY -= speed;
        } else if (keyH.downPressed) {
            direction = "down";
            worldY += speed;
        } else if (keyH.leftPressed) {
            direction = "left";
            worldX -= speed;
            this.x -=1;
            if(this.x <0){this.y = 0;}
        } else if (keyH.rightPressed) {
            direction = "right";
            worldX += speed;
        }

        collisionOn = false;
        gp.cChecker.CheckTile(this);

        if (spriteCounter > 8) {
            if (spriteNum == 1) {
                spriteNum = 2;
            } else if (spriteNum == 2) {
                spriteNum = 1;
            }
            spriteCounter = 0;
        }
    }

    public void draw(Graphics2D g2) {
//        g2.setColor(Color.white);
//        g2.fillRect(x, y, gp.tileSize, gp.tileSize);
        BufferedImage image = null;
        switch (direction) {
            case "up":
                if (spriteNum == 1) {
                    image = frame1;
                }
                if (spriteNum == 2) {
                    image = frame2;
                }
                break;
            case "down":
                if (spriteNum == 1) {
                    image = frame1;
                }
                if (spriteNum == 2) {
                    image = frame2;
                }
                break;
            case "left":
                if (spriteNum == 1) {
                    image = frame1;
                }
                if (spriteNum == 2) {
                    image = frame2;
                }
                break;
            case "right":
                if (spriteNum == 1) {
                    image = frame1;
                }
                if (spriteNum == 2) {
                    image = frame2;
                }
                break;
        }
        if (direction.equals("left") || direction.equals("up")){
            g2.drawImage(image, screenX, screenY, gp.tileSize, gp.tileSize, null);
        } else {
            g2.drawImage(image, screenX + gp.tileSize, screenY, -gp.tileSize, gp.tileSize, null);
        }
    }
}
