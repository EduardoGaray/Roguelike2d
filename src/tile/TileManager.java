package tile;

import main.GamePanel;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Arrays;
import java.util.Objects;
import java.util.Random;

public class TileManager {
    GamePanel gp;
    //Right now this is the array that we use to create the current game map, later on we must create every map in a separate array and save/load them as needed
    Tile[] tile;

    public TileManager(GamePanel gp) {
        //Here we initialize the process to create the map
        this.gp = gp;
        tile = new Tile[gp.maxScreenCol * gp.maxScreenRow];
        int area = gp.maxScreenCol * gp.maxScreenRow;
        System.out.println(area);
        getTileImage();
        loadMap();
    }

    //this method generates basic tiles randomly, we can work on a more complex system for map generation from here
    public void getTileImage() {
        try {
            int col = 0;
            int row = 0;
            int x = 1;
            int y = 1;
            for (int i = 0; i<tile.length; i++ ){
                tile[i] = new Tile();
                tile[i].x = x;
                tile[i].y = y;
                final String[] name = {"grass00","earth","water00","tree"};
                Random random = new Random();
                int index = random.nextInt(name.length);
                tile[i].name = name[index];
                tile[i].image = ImageIO.read(Objects.requireNonNull(getClass().getResourceAsStream("/tiles/"+tile[i].name+".png")));
                col++;
                x += 1;
                if (col == gp.maxScreenCol) {
                    col = 0;
                    x = 1;
                    row++;
                    y += 1;
                }
                System.out.println(tile[i].name+" "+tile[i].x+" "+tile[i].y);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void loadMap() {
        for (int i = 0; i < tile.length; i++) {

        }

    }
    //This method renders the tile array.image property
    public void draw(Graphics2D g2) {
        int col = 0;
        int row = 0;
        int x = 0;
        int y = 0;
        int cont = 0;
        while (col < gp.maxScreenCol && row < gp.maxScreenRow) {
            g2.drawImage(tile[cont].image, x, y, gp.tileSize, gp.tileSize, null);
            cont++;
            col++;
            x += gp.tileSize;
            if (col == gp.maxScreenCol) {
                col = 0;
                x = 0;
                row++;
                y += gp.tileSize;
            }
            if (cont >= tile.length) {
                cont = 0;
            }
        }
    }
}
