package Ascii.Screens;

import java.awt.event.KeyEvent;
import java.util.ArrayList;

import Ascii.Main.Creature;
import Ascii.Main.Item;

public abstract class InventoryBasedScreen implements Screen {

	protected Creature player;
	private String letters;

	protected abstract String getVerb();

	protected abstract boolean isAcceptable(Item item);

	protected abstract Screen use(Item item);

	public InventoryBasedScreen(Creature player) {
		this.player = player;
		this.letters = "abcdefghijklmnopqrstuvwxyz";
	}

	public ArrayList<String> getList() {
		ArrayList<String> lines = new ArrayList<String>();
		Item[] inventory = player.inventory().getItems();

		for (int i = 0; i < inventory.length; i++) {
			Item item = inventory[i];

			if (item == null || !isAcceptable(item))
				continue;

			String line = letters.charAt(i) + " - " + item.glyph() + " " + item.name();

			if (item == player.weapon() || item == player.armor())
				line += " (equipped)";

			lines.add(line);
		}
		return lines;
	}

	public Screen respondToUserInput(KeyEvent key) {
		char c = key.getKeyChar();

		Item[] items = player.inventory().getItems();

		if (letters.indexOf(c) > -1 && items.length > letters.indexOf(c) && items[letters.indexOf(c)] != null
				&& isAcceptable(items[letters.indexOf(c)]))
			return use(items[letters.indexOf(c)]);
		else if (key.getKeyCode() == KeyEvent.VK_ESCAPE)
			return null;
		else
			return this;
	}

}
