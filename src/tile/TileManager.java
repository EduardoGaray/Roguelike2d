package tile;

import main.GamePanel;

import javax.imageio.ImageIO;
import java.awt.*;
import java.io.IOException;
import java.util.Objects;
import java.util.Random;

public class TileManager {
    GamePanel gp;
    //Right now this is the array that we use to create the current game map, later on we must create every map in a separate array and save/load them as needed
    Tile[] tile;

    public TileManager(GamePanel gp) {
        //Here we initialize the process to create the map
        this.gp = gp;
        tile = new Tile[gp.maxWorldCol * gp.maxWorldRow];
        getTileImage();
        smoothMap();
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
                tile[i].id = i;
                tile[i].x = x;
                tile[i].y = y;
                final String[] name = {"earth","wall"};
                Random random = new Random();
                int index = random.nextInt(name.length);
                tile[i].filename = name[index];
                tile[i].image = ImageIO.read(Objects.requireNonNull(getClass().getResourceAsStream("/tiles/"+tile[i].filename+".png")));
                col++;
                x += 1;
                if (col == gp.maxScreenCol) {
                    col = 0;
                    x = 1;
                    row++;
                    y += 1;
                }
                System.out.println(tile[i].filename+" "+tile[i].x+" "+tile[i].y+" "+tile[i].id);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void smoothMap() {
        try {
            int cont = 0;
            int north;
            int south;
            int west;
            int east;
            for (int i = 0; i<tile.length; i++ ){

               north = tile[i].id - gp.maxWorldCol;
               south = tile[i].id + gp.maxWorldCol;
               west = tile[i].id + 1;
               east = tile[i].id - 1;

               if(north <0){north = 0;}
               if(south > tile.length-1){ south = tile.length-1;}
                if(east <0){east = 0;}
                if(west > tile.length-1){ west = tile.length-1;}
               if(tile[i].filename.equals("wall") && tile[north].filename.equals("earth")){
                   cont++;
               }
                if(tile[i].filename.equals("wall") && tile[south].filename.equals("earth")){
                    cont++;
                }
                if(tile[i].filename.equals("wall") && tile[west].filename.equals("earth")){
                    cont++;
                }
                if(tile[i].filename.equals("wall") && tile[east].filename.equals("earth")){
                    cont++;
                }
                if(cont >=3){
                    tile[i].image = ImageIO.read(Objects.requireNonNull(getClass().getResourceAsStream("/tiles/earth.png")));
                }
                cont =0;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        }
    //This method renders the tile array.image property
    public void draw(Graphics2D g2) {
        int col = 0;
        int row = 0;
        int cont = 0;
        while (col < gp.maxWorldCol && row < gp.maxWorldRow) {

            int worldX = col * gp.tileSize;
            int worldY = row * gp.tileSize;

            int screenX = worldX -gp.player.worldX + gp.player.screenX;
            int screenY = worldY -gp.player.worldY + gp.player.screenY;

            //this if is necessary to make sure we only render tiles currently on the screen
            if(worldX + gp.tileSize > gp.player.worldX -gp.player.screenX &&
                    worldX -gp.tileSize < gp.player.worldX + gp.player.screenX &&
                    worldY + gp.tileSize > gp.player.worldY -gp.player.screenY &&
                    worldY - gp.tileSize < gp.player.worldY + gp.player.screenY){
                g2.drawImage(tile[cont].image, screenX, screenY, gp.tileSize, gp.tileSize, null);
            }
            cont++;
            col++;

            if (col == gp.maxWorldCol) {
                col = 0;

                row++;
            }
            if (cont >= tile.length) {
                cont = 0;
            }
        }
    }
}
