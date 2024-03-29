package Ascii.Screens;

import java.util.ArrayList;

import asciiPanel.AsciiPanel;
import Ascii.Main.Creature;
import Ascii.Main.Item;

public class ReadScreen extends InventoryBasedScreen {

    private int sx;
    private int sy;
    
    public ReadScreen(Creature player, int sx, int sy) {
        super(player);
        this.sx = sx;
        this.sy = sy;
    }

    protected String getVerb() {
        return "read";
    }

    protected boolean isAcceptable(Item item) {
        return !item.writtenSpells().isEmpty();
    }

    protected Screen use(Item item) {
        return new ReadSpellScreen(player, sx, sy, item);
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
