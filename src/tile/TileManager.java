package tile;

import main.GamePanel;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;

public class TileManager {
    GamePanel gp;
    Tile[] tile;


    public TileManager(GamePanel gp) {
        this.gp = gp;
        tile = new Tile[10];
        getTileImage();

    }
    public void getTileImage() {
        try{
            tile[0] = new Tile();
            tile[0].image =  ImageIO.read(getClass().getResourceAsStream("/tiles/grass00.png"));
            tile[1] = new Tile();
            tile[1].image =  ImageIO.read(getClass().getResourceAsStream("/tiles/wall.png"));
            tile[2] = new Tile();
            tile[2].image =  ImageIO.read(getClass().getResourceAsStream("/tiles/water00.png"));

        } catch (IOException e){
            e.printStackTrace();
        }
    }
    public void draw(Graphics2D g2){
        int xpos = 0;
        int ypos = 0;
        int id = 1;
        int counter = 0;
        for (int i = 0; i <gp.maxScreenCol * gp.maxScreenRow; i++, xpos += gp.tileSize, counter++){
            if(counter == gp.maxScreenCol){
                id = 0;
                ypos += gp.tileSize;
                xpos = 0;
                counter = 0;
            }
            g2.drawImage(tile[id].image,xpos,ypos,gp.tileSize,gp.tileSize,null);
        }
    }
}
