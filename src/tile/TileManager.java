package tile;

import main.GamePanel;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Objects;
import java.util.Random;

public class TileManager {
    GamePanel gp;
    //Right now this is the array that we use to create the current game map, later on we must create every map in a separate array and save/load them as needed
    Tile[] tile;
    int[][] mapTileNum;

    public TileManager(GamePanel gp) {
        //Here we initialize the process to create the map
        this.gp = gp;
        tile = new Tile[gp.maxWorldCol * gp.maxWorldRow];
        mapTileNum = new int[gp.maxWorldCol][gp.maxWorldRow];
        getTileImage();
        loadMap("/maps/world01.txt");
        //fillhole();
    }

    //this method generates basic tiles randomly, we can work on a more complex system for map generation from here
    public void getTileImage() {
        try {
            int col = 0;
            int row = 0;
            int x = 1;
            int y = 1;
            for (int i = 0; i < tile.length; i++) {
                tile[i] = new Tile();
                tile[i].id = i;
                tile[i].x = x;
                tile[i].y = y;
                final String[] name = {"floor00", "wall00"};
                Random random = new Random();
                int index = random.nextInt(name.length);
                tile[i].filename = name[index];
                if(tile[i].filename.equals("wall00")){ tile[i].collision = true;}
                tile[i].image = ImageIO.read(Objects.requireNonNull(getClass().getResourceAsStream("/tiles/" + tile[i].filename + ".png")));
                col++;
                x += 1;
                if (col == gp.maxScreenCol) {
                    col = 0;
                    x = 1;
                    row++;
                    y += 1;
                }
                //System.out.println(tile[i].filename + " " + tile[i].x + " " + tile[i].y + " " + tile[i].id);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void loadMap(String filepath){

        try{
            InputStream is = getClass().getResourceAsStream(filepath);
            BufferedReader br = new BufferedReader(new InputStreamReader(is));

            int col = 0;
            int row = 0;

            while (col < gp.maxWorldCol && row < gp.maxScreenRow){
                String line = br.readLine();
                while (col < gp.maxWorldCol){
                    String numbers[] = line.split(" ");
                    int num = Integer.parseInt(numbers[col]);
                    mapTileNum[col][row] = num;
                    col++;
                }
                if(col == gp.maxWorldCol){
                    col = 0;
                    row++;
                }

            }

        } catch(Exception e) {

        }

    }

    public void fillhole() {
        try {
            int cont = 0;
            int mod = 0;
            int north;
            int south;
            int west;
            int east;
            for (int y = 0; y <1; y++){
                for (int i = 0; i < tile.length; i++) {
                    north = tile[i].id - gp.maxWorldCol;
                    south = tile[i].id + gp.maxWorldCol;
                    west = tile[i].id + 1;
                    east = tile[i].id - 1;

                    if (north < 0) {
                        north = 0;
                    }
                    if (south > tile.length - 1) {
                        south = tile.length - 1;
                    }
                    if (east < 0) {
                        east = 0;
                    }
                    if (west > tile.length - 1) {
                        west = tile.length - 1;
                    }
                    if (tile[i].filename.equals("wall00")) {
                        if (tile[north].filename.equals("floor00")) {
                            cont++;
                        }
                        if (tile[south].filename.equals("floor00")) {
                            cont++;
                        }
                        if (tile[east].filename.equals("floor00")) {
                            cont++;
                        }
                        if (tile[west].filename.equals("floor00")) {
                            cont++;
                        }
                        if (cont >= 3) {
                            tile[i].image = ImageIO.read(Objects.requireNonNull(getClass().getResourceAsStream("/tiles/floor00.png")));
                            tile[i].filename = "floor00";
                        }
                        cont = 0;
                    }
                }
            }
            for (int y = 0; y <1; y++){
                for (int i = 0; i < tile.length; i++) {
                    north = tile[i].id - gp.maxWorldCol;
                    south = tile[i].id + gp.maxWorldCol;
                    west = tile[i].id + 1;
                    east = tile[i].id - 1;

                    if (north < 0) {
                        north = 0;
                    }
                    if (south > tile.length - 1) {
                        south = tile.length - 1;
                    }
                    if (east < 0) {
                        east = 0;
                    }
                    if (west > tile.length - 1) {
                        west = tile.length - 1;
                    }
                    if (tile[i].filename.equals("floor00")) {
                        if (tile[north].filename.equals("wall00")) {
                            cont++;
                        }
                        if (tile[south].filename.equals("wall00")) {
                            cont++;
                        }
                        if (tile[east].filename.equals("wall00")) {
                            cont++;
                        }
                        if (tile[west].filename.equals("wall00")) {
                            cont++;
                        }
                        if (cont >= 4) {
                            tile[i].image = ImageIO.read(Objects.requireNonNull(getClass().getResourceAsStream("/tiles/wall00.png")));
                            tile[i].filename = "wall00";
                        }
                        cont = 0;
                    }
                }
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

            int screenX = (int) (worldX - gp.player.worldX + gp.player.screenX);
            int screenY = (int) (worldY - gp.player.worldY + gp.player.screenY);

            int tileNum = mapTileNum[col][row];

            //this if is necessary to make sure we only render tiles currently on the screen
            if (worldX + gp.tileSize > gp.player.worldX - gp.player.screenX &&
                    worldX - gp.tileSize < gp.player.worldX + gp.player.screenX &&
                    worldY + gp.tileSize > gp.player.worldY - gp.player.screenY &&
                    worldY - gp.tileSize < gp.player.worldY + gp.player.screenY) {
                //g2.drawImage(tile[cont].image, screenX, screenY, gp.tileSize, gp.tileSize, null);
                g2.drawImage(tile[tileNum].image, screenX, screenY, gp.tileSize, gp.tileSize, null);
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
