package New.Screens;

import java.util.ArrayList;

import asciiPanel.AsciiPanel;
import New.Main.Creature;
import New.Main.Item;

public class EquipScreen extends InventoryBasedScreen {

	  public EquipScreen(Creature player) {
	    super(player);
	  }

	  protected String getVerb() {
	    return "wear or wield";
	  }

	  protected boolean isAcceptable(Item item) {
	    return item.attackValue() > 0 || item.defenseValue() > 0;
	  }

	  protected Screen use(Item item) {
	    player.equip(item);
	    return null;
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
