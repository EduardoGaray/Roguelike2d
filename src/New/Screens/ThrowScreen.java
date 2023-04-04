package New.Screens;

import java.util.ArrayList;

import asciiPanel.AsciiPanel;
import New.Main.Creature;
import New.Main.Item;

public class ThrowScreen extends InventoryBasedScreen {
    private int sx;
    private int sy;

    public ThrowScreen(Creature player, int sx, int sy) {
        super(player);
        this.sx = sx;
        this.sy = sy;
    }

    protected String getVerb() {
        return "throw";
    }

    protected boolean isAcceptable(Item item) {
        return true;
    }

    protected Screen use(Item item) {
        return new ThrowAtScreen(player, sx, sy, item);
    }

    public void displayOutput(AsciiPanel terminal) {
        ArrayList<String> lines = getList();
    
        int y = 23 - lines.size();
        int x = 4;

        if (lines.size() > 0)
            terminal.clear(' ', x, y, 20, lines.size());
    
        for (String line : lines){
            terminal.write(line, x, y++);
        }
    
        terminal.clear(' ', 0, 23, 80, 1);
        terminal.write("What would you like to " + getVerb() + "?", 2, 23);
    
        terminal.repaint();
    }
}
