package Ascii.Screens;

import java.util.ArrayList;

import asciiPanel.AsciiPanel;
import Ascii.Main.Creature;
import Ascii.Main.Item;

public class DropScreen extends InventoryBasedScreen {

    public DropScreen(Creature player) {
        super(player);
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

	protected String getVerb() {
        return "drop";
    }

	protected boolean isAcceptable(Item item) {
        return true;
    }

	protected Screen use(Item item) {
        player.drop(item);
        return null;
    }
}
