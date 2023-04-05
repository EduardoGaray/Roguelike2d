package Tiles.entity;

import Tiles.main.GamePanel;
import Tiles.main.KeyHandler;

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

        solidArea = new Rectangle(8, 16, 32, 32);


        setDefaultValues();
        getPlayerImage();
    }

    public void setDefaultValues() {
        worldX = gp.tileSize * 23;
        worldY = gp.tileSize * 20;
        speed = gp.worldWidth / 600;
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
        if (keyH.upPressed == true || keyH.downPressed == true || keyH.leftPressed == true || keyH.rightPressed == true){
            if (keyH.upPressed) {
                direction = "up";
            } else if (keyH.downPressed) {
                direction = "down";
            } else if (keyH.leftPressed) {
                direction = "left";
            } else if (keyH.rightPressed) {
                direction = "right";
            }
            // CHECK FOR COLLISION
            collisionOn = false;
            gp.cChecker.CheckTile(this);

            //IF COLLISION IS FALSE, PLAYER CAN MOVE
            if (!collisionOn) {
                switch (direction) {
                    case "up":
                        worldY -= speed;
                        break;
                    case "down":
                        worldY += speed;
                        break;
                    case "left":
                        worldX -= speed;
                        break;
                    case "right":
                        worldX += speed;
                        break;
                }
            }
        }
        spriteCounter++;
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
        if (direction.equals("left") || direction.equals("up")) {
            g2.drawImage(image, screenX, screenY, gp.tileSize, gp.tileSize, null);
        } else {
            g2.drawImage(image, screenX + gp.tileSize, screenY, -gp.tileSize, gp.tileSize, null);
        }
    }
}
