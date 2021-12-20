package tile;

import java.awt.image.BufferedImage;

public class Tile {
    public BufferedImage image;
    public boolean collision = false;
    public String name;
    public String filename;
    public String type;
    public int x,y;
    public int screenx, screeny;
    public int neighbors;
    public int id;

}
