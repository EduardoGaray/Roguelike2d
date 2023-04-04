package New.Screens;

import java.awt.Color;
import java.awt.event.KeyEvent;
import java.util.List;
import java.util.Random;
import java.util.ArrayList;

import asciiPanel.AsciiPanel;
import New.Main.Creature;
import New.Main.EntityFactory;
import New.Main.FieldOfView;
import New.Main.Item;
import New.Main.Tile;
import New.Main.World;
import New.Main.WorldBuilder;

public class PlayScreen implements Screen {

	private World world;
	private Creature player;
	private FieldOfView fov;
	private int screenWidth;
	private int screenHeight;

	private Screen subscreen;

	private List<String> messages;

	public void displayOutput(AsciiPanel terminal) {

		int left = getPlayerX();
		int top = getPlayerY();

		displayTiles(terminal, left, top);
		displayMessages(terminal, messages);

		terminal.write(player.glyph(), player.x - left, player.y - top, player.color());

		String stats = String.format(" %3d/%3d hp  %d/%d mana  %8s", 
			    player.maxHp(), player.hp(), player.maxMana(), player.mana(), hunger());  
		terminal.write(stats, 1, 21);
		String level = String.format(" Floor: %3d", player.z + 1);
		terminal.write(level, 1, 22);
		if (subscreen != null)
			subscreen.displayOutput(terminal);

	}

	private String hunger() {
		if (player.food() < player.maxFood() * 0.1)
			return "Starving";
		else if (player.food() < player.maxFood() * 0.2)
			return "Hungry";
		else if (player.food() > player.maxFood() * 0.9)
			return "Stuffed";
		else if (player.food() > player.maxFood() * 0.8)
			return "Full";
		else
			return "";
	}

	public Screen respondToUserInput(KeyEvent key) {
		int left = getPlayerX();
		int top = getPlayerY();
		int level = player.level();
		if (player.level() > level)
			subscreen = new LevelUpScreen(player, player.level() - level);
		if (subscreen != null) {
			subscreen = subscreen.respondToUserInput(key);
		} else {
			switch (key.getKeyCode()) {
			case KeyEvent.VK_LEFT:
				player.moveBy(player, -1, 0, 0);
				break;
			case KeyEvent.VK_RIGHT:
				player.moveBy(player, 1, 0, 0);
				break;
			case KeyEvent.VK_UP:
				player.moveBy(player, 0, -1, 0);
				break;
			case KeyEvent.VK_DOWN:
				player.moveBy(player, 0, 1, 0);
				break;
			case KeyEvent.VK_Y:
				player.moveBy(player, -1, -1, 0);
				break;
			case KeyEvent.VK_U:
				player.moveBy(player, 1, -1, 0);
				break;
			case KeyEvent.VK_B:
				player.moveBy(player, -1, 1, 0);
				break;
			case KeyEvent.VK_N:
				player.moveBy(player, 1, 1, 0);
				break;
			case KeyEvent.VK_D:
				subscreen = new DropScreen(player);
				break;
			case KeyEvent.VK_E:
				subscreen = new EatScreen(player);
				break;
			case KeyEvent.VK_W:
				subscreen = new EquipScreen(player);
				break;
			case KeyEvent.VK_H:
				subscreen = new HelpScreen();
				break;
			case KeyEvent.VK_T:
				subscreen = new ThrowScreen(player, player.x - left, player.y - top);
				break;
			case KeyEvent.VK_X:
				subscreen = new ExamineScreen(player);
				break;
			case KeyEvent.VK_L:
				subscreen = new LookScreen(player, "Look Around", player.x - left, player.y - top);
				break;
			case KeyEvent.VK_G:
				player.pickup();
				break;
			case KeyEvent.VK_0:
				if (userIsTryingToExit())
					return userExits();
				else
					player.moveBy(player, 0, 0, -1);
				break;
			case KeyEvent.VK_1:
				player.moveBy(player, 0, 0, 1);
				break;
			case KeyEvent.VK_F:
				if (player.weapon() == null || player.weapon().rangedAttackValue() == 0)
					player.notify("You don't have a ranged weapon equiped.");
				else
					subscreen = new FireWeaponScreen(player, player.x - left, player.y - top);
				break;
			case KeyEvent.VK_Q:
				subscreen = new QuaffScreen(player);
				break;
			case KeyEvent.VK_R:
				subscreen = new ReadScreen(player, player.x - left, player.y - top);
				break;
			}
		}

		if (subscreen == null)
			world.update();
		System.out.println("Player x: " + player.maxHp());
		System.out.println("Player y: " + player.hp());

		if (player.hp() < 1)
			return new LoseScreen();

		return this;
	}

	public PlayScreen() {
		screenWidth = 80;
		screenHeight = 21;
		messages = new ArrayList<String>();
		createWorld();
		fov = new FieldOfView(world);
		EntityFactory creatureFactory = new EntityFactory(world);
		createCreatures(creatureFactory);
		createItems(creatureFactory);
	}

	private void createWorld() {
		world = new WorldBuilder(90, 31, 3).makeCaves().build();
	}

	public int getPlayerX() {
		return Math.max(0, Math.min(player.x - screenWidth / 2, world.width() - screenWidth));
	}

	public int getPlayerY() {
		return Math.max(0, Math.min(player.y - screenHeight / 2, world.height() - screenHeight));
	}

	private void displayMessages(AsciiPanel terminal, List<String> messages) {
		int top = screenHeight - messages.size();
		for (int i = 0; i < messages.size(); i++) {
			terminal.writeCenter(messages.get(i), top + i);
		}
		messages.clear();
	}

	private void displayTiles(AsciiPanel terminal, int left, int top) {
		fov.update(player.x, player.y, player.z, player.visionRadius());
		for (int x = 0; x < screenWidth; x++) {
			for (int y = 0; y < screenHeight; y++) {
				int wx = x + left;
				int wy = y + top;
				int wz = player.z;

				if (player.canSee(wx, wy, wz)) {
					Creature creature = world.creature(wx, wy, wz);
					if (creature != null && creature.z == wz) {
						terminal.write(creature.glyph(), creature.x - left, creature.y - top, creature.color());
					} else {
						terminal.write(world.glyph(wx, wy, wz), x, y, world.color(wx, wy, wz));

					}

				} else {
					terminal.write(fov.tile(wx, wy, player.z).glyph(), x, y, Color.darkGray);
				}

			}
		}
	}

	private void createCreatures(EntityFactory creatureFactory) {
		player = creatureFactory.newPlayer(messages, fov);
		Random rand = new Random();
		int randomDepth;

		for (int i = 0; i < 10; i++) {
			randomDepth = rand.nextInt(world.depth());
			creatureFactory.newFungus(randomDepth);
		}

		for (int i = 0; i < 20; i++) {
			randomDepth = rand.nextInt(world.depth());
			creatureFactory.newBat(randomDepth);
		}

		for (int i = 0; i < 5; i++) {
			randomDepth = rand.nextInt(world.depth());
			creatureFactory.newZombie(player,randomDepth);
		}

		for (int i = 0; i < 3; i++) {
			randomDepth = rand.nextInt(world.depth());
			creatureFactory.newGoblin(player,randomDepth);
		}
	}

	private void createItems(EntityFactory factory) {
		
		Random rand = new Random();
		int randomDepth;
		
		for (int z = 0; z < world.depth(); z++) {
			for (int i = 0; i < world.width() * world.height() / 20; i++) {
				factory.newRock(z);
			}
		}

		for (int i = 0; i < 1; i++) {
			randomDepth = rand.nextInt(world.depth());
			factory.newVictoryItem(world.depth()-1);
		}
		
		for (int i = 0; i < 5; i++) {
			randomDepth = rand.nextInt(world.depth());
			factory.newEdibleWeapon(randomDepth);
		}
		
		for (int i = 0; i < 3; i++) {
			randomDepth = rand.nextInt(world.depth());
			factory.randomArmor(randomDepth);
		}
		
		for (int i = 0; i < 3; i++) {
			randomDepth = rand.nextInt(world.depth());
			factory.randomWeapon(randomDepth);
		}
		
		for (int i = 0; i < 10; i++) {
			randomDepth = rand.nextInt(world.depth());
			factory.randomPotion(randomDepth);
		}
		
	}

	private boolean userIsTryingToExit() {
		return player.z == 0 && world.tile(player.x, player.y, player.z) == Tile.STAIRS_UP;
	}

	private Screen userExits() {
		for (Item item : player.inventory().getItems()) {
			if (item != null && item.name().equals("teddy bear"))
				return new WinScreen();
		}
		return new LoseScreen();
	}

}
