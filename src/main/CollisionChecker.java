package main;

import entity.Entity;
import entity.Player;

public class CollisionChecker {

    GamePanel gp;
    Player player;

    public CollisionChecker(GamePanel gp) {
        this.gp = gp;
    }

    public void CheckTile(Entity entity){

        int entityLeftWorldX = (int) (entity.worldX + entity.solidArea.x);
        int entityRightWorldX = (int) (entity.worldX + entity.solidArea.x + entity.solidArea.width);
        int entityTopWorldY = (int) (entity.worldY + entity.solidArea.y);
        int entityBottomWorldY = (int) (entity.worldY + entity.solidArea.y + entity.solidArea.height);

        int entityLeftCol = entityLeftWorldX/gp.tileSize;
        int entityRightCol = entityRightWorldX/gp.tileSize;
        int entityTopRow = entityTopWorldY/gp.tileSize;
        int entityBottomRow = entityBottomWorldY/gp.tileSize;

        int playerposx, playerposy, entityposx,entityposy;

        switch (entity.direction){
            case "up":
                break;
            case "down":
                break;
            case "left":
                break;
            case "right":
                break;
        }
    }
}
