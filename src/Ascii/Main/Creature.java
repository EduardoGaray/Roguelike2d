package Ascii.Main;

import java.awt.Color;
import java.util.List;
import java.util.ArrayList;

public class Creature {

	private World world;
	private FieldOfView fov;

	public int x;
	public int y;
	public int z;

	public String tag;

	private int xp;

	public int xp() {
		return xp;
	}

	private int level;

	public int level() {
		return level;
	}

	private char glyph;

	public char glyph() {
		return glyph;
	}

	private Color color;

	public Color color() {
		return color;
	}

	public String getTag() {
		return tag;
	}

	public void setTag(String tag) {
		this.tag = tag;
	}

	private CreatureAi ai;

	public void setCreatureAi(CreatureAi ai) {
		this.ai = ai;
	}

	public FieldOfView getFov() {
		return fov;
	}

	public void setFov(FieldOfView fov) {
		this.fov = fov;
	}

	private int maxHp;

	public int maxHp() {
		return maxHp;
	}

	private int hp;

	public int hp() {
		return hp;
	}

	private int attackValue;

	public int attackValue() {
		return attackValue + (weapon == null ? 0 : weapon.attackValue()) + (armor == null ? 0 : armor.attackValue());
	}

	private int defenseValue;

	public int defenseValue() {
		return defenseValue + (weapon == null ? 0 : weapon.defenseValue()) + (armor == null ? 0 : armor.defenseValue());
	}

	private int visionRadius;

	public int visionRadius() {
		return visionRadius;
	}

	private List<Effect> effects;

	public List<Effect> effects() {
		return effects;
	}

	private String name;

	public String name() {
		return name;
	}

	private Inventory inventory;

	public Inventory inventory() {
		return inventory;
	}

	public Creature(World world, char glyph, Color color, int maxHp, int hp, int maxMana, int mana, int attack,
			int defense, String tag, int visionRadius, int regenHpPer1000, int regenManaPer1000, String name,
			List<Effect> effects) {
		this.world = world;
		this.glyph = glyph;
		this.color = color;
		this.maxHp = maxHp;
		this.hp = hp;
		this.mana = mana;
		this.maxMana = maxMana;
		this.attackValue = attack;
		this.defenseValue = defense;
		this.regenHpPer1000 = regenHpPer1000;
		this.regenManaPer1000 = regenManaPer1000;
		this.tag = tag;
		this.visionRadius = visionRadius;
		this.name = name;
		this.inventory = new Inventory(20);
		this.maxFood = 1000;
		this.food = maxFood / 3 * 2;
		this.level = 1;
		this.effects = new ArrayList<Effect>();
	}

	public void dig(int wx, int wy, int wz) {
		modifyFood(-10);
		world.dig(wx, wy, wz);
		doAction("dig");
	}

	public void modifyHp(int amount) {
		hp += amount;
		if (hp < 1) {
			doAction("die");
			leaveCorpse();
			world.remove(this);
		}
	}

	private int regenHpCooldown;
	private int regenHpPer1000;

	public void modifyRegenHpPer1000(int amount) {
		regenHpPer1000 += amount;
	}

	private void regenerateHealth() {
		regenHpCooldown -= regenHpPer1000;
		if (regenHpCooldown < 0) {
			modifyHp(1);
			modifyFood(-1);
			regenHpCooldown += 1000;
		}
	}

	private int maxMana;

	public int maxMana() {
		return maxMana;
	}

	private int mana;

	public int mana() {
		return mana;
	}

	public void modifyMana(int amount) {
		mana = Math.max(0, Math.min(mana + amount, maxMana));
	}

	private int regenManaCooldown;
	private int regenManaPer1000;

	public void modifyRegenManaPer1000(int amount) {
		regenManaPer1000 += amount;
	}

	private void regenerateMana() {
		regenManaCooldown -= regenManaPer1000;
		if (regenManaCooldown < 0) {
			if (mana < maxMana) {
				modifyMana(1);
				modifyFood(-1);
			}
			regenManaCooldown += 1000;
		}
	}

	private void leaveCorpse() {
		List<Spell> writtenSpells = new ArrayList<Spell>();
		Item corpse = new Item('%', color, name + " corpse", writtenSpells);
		corpse.modifyFoodValue(maxHp);
		world.addAtEmptySpace(corpse, x, y, z);
		for (Item item : inventory.getItems()) {
			if (item != null)
				drop(item);
		}
	}

	public boolean canEnter(int wx, int wy, int wz) {
		return world.tile(wx, wy, wz).isGround() && world.creature(wx, wy, wz) == null;
	}

	public void update() {
		modifyFood(-1);
		regenerateHealth();
		updateEffects();
		ai.onUpdate();
	}

	public void moveBy(Creature creature, int mx, int my, int mz) {
		Tile tile = world.tile(x + mx, y + my, z + mz);
		if (mx == 0 && my == 0 && mz == 0)
			return;
		if (mz == -1) {
			if (tile == Tile.STAIRS_DOWN) {
				doAction("walk up the stairs to level %d", z + mz - 1);
			} else {
				doAction("try to go up but are stopped by the cave ceiling");
				return;
			}
		} else if (mz == 1) {
			if (tile == Tile.STAIRS_UP) {
				doAction("walk down the stairs to level %d", z + mz + 1);
			} else {
				doAction("try to go down but are stopped by the cave floor");
				return;
			}
		}
		Creature other = world.creature(x + mx, y + my, z + mz);
		if (other == null || other.tag.equals(creature.tag) || other.z != creature.z) {
			ai.onEnter(x + mx, y + my, z + mz, tile);
		} else if (other.z == creature.z) {
			meleeAttack(other);
		}
	}

	public void notify(String message, Object... params) {
		ai.onNotify(String.format(message, params));
	}

	public void doAction(String message, Object... params) {
		int r = visionRadius;
		for (int ox = -r; ox < r + 1; ox++) {
			for (int oy = -r; oy < r + 1; oy++) {
				if (ox * ox + oy * oy > r * r)
					continue;

				Creature other = world.creature(x + ox, y + oy, z);

				if (other == null)
					continue;

				if (tag.equals("player"))
					other.notify("You " + message + ".", params);
				else if (other.canSee(x, y, z))
					other.notify(String.format("The %s %s.", name, makeSecondPerson(message)), params);
			}
		}
	}

	private String makeSecondPerson(String text) {
		String[] words = text.split(" ");
		words[0] = words[0] + "s";

		StringBuilder builder = new StringBuilder();
		for (String word : words) {
			builder.append(" ");
			builder.append(word);
		}

		return builder.toString().trim();
	}

	public boolean canSee(int wx, int wy, int wz) {
		return (detectCreatures > 0 && world.creature(wx, wy, wz) != null || ai.canSee(wx, wy, wz));
	}

	public void pickup() {
		Item item = world.item(x, y, z);

		if (inventory.isFull() || item == null) {
			doAction("grab at the ground");
		} else {
			doAction("pickup a %s", item.name());
			world.remove(x, y, z);
			inventory.add(item);
		}
	}

	public void drop(Item item) {
		doAction("drop a " + item.name());
		inventory.remove(item);
		unequip(item);
		world.addAtEmptySpace(item, x, y, z);
	}

	public Tile realTile(int wx, int wy, int wz) {
		return world.tile(wx, wy, wz);
	}

	public Tile tile(int wx, int wy, int wz) {
		if (canSee(wx, wy, wz))
			return world.tile(wx, wy, wz);
		else
			return ai.rememberedTile(wx, wy, wz);
	}

	public Creature creature(int wx, int wy, int wz) {
		if (canSee(wx, wy, wz))
			return world.creature(wx, wy, wz);
		else
			return null;
	}

	private int maxFood;

	public int maxFood() {
		return maxFood;
	}

	private int food;

	public int food() {
		return food;
	}

	public void modifyFood(int amount) {
		food += amount;

		if (food > maxFood) {
			maxFood = maxFood + food / 2;
			food = maxFood;
			notify("You can't believe your stomach can hold that much!");
			modifyHp(-1);
		} else if (food < 1 && isPlayer()) {
			modifyHp(-1000);
		}
	}

	public boolean isPlayer() {
		return glyph == '@';
	}

	private Item weapon;

	public Item weapon() {
		return weapon;
	}

	private Item armor;

	public Item armor() {
		return armor;
	}

	public void unequip(Item item) {
		if (item == null)
			return;

		if (item == armor) {
			doAction("remove a " + item.name());
			armor = null;
		} else if (item == weapon) {
			doAction("put away a " + item.name());
			weapon = null;
		}
	}

	public void equip(Item item) {
		if (!inventory.contains(item)) {
			if (inventory.isFull()) {
				notify("Can't equip %s since you're holding too much stuff.", item.name());
				return;
			} else {
				world.remove(item);
				inventory.add(item);
			}
		}

		if (item.attackValue() == 0 && item.rangedAttackValue() == 0 && item.defenseValue() == 0)
			return;

		if (item.attackValue() + item.rangedAttackValue() >= item.defenseValue()) {
			unequip(weapon);
			doAction("wield a " + item.name());
			weapon = item;
		} else {
			unequip(armor);
			doAction("put on a " + item.name());
			armor = item;
		}
	}

	public void modifyXp(int amount) {
		xp += amount;

		notify("You %s %d xp.", amount < 0 ? "lose" : "gain", amount);

		while (xp > (int) (Math.pow(level, 1.5) * 20)) {
			level++;
			doAction("advance to level %d", level);
			ai.onGainLevel();
			modifyHp(level * 2);
		}
	}

	public void gainXp(Creature other) {
		int amount = other.maxHp + other.attackValue() + other.defenseValue() - level * 2;

		if (amount > 0)
			modifyXp(amount);
	}

	public void gainMaxMana() {
		maxMana += 5;
		mana += 5;
		doAction("look more magical");
	}

	public void gainRegenMana() {
		regenManaPer1000 += 5;
		doAction("look a little less tired");
	}

	public void gainMaxHp() {
		maxHp += 10;
		hp += 10;
		if (hp > maxHp) {
			hp = maxHp;
		}
		doAction("look healthier");
	}

	public void gainAttackValue() {
		attackValue += 2;
		doAction("look stronger");
	}

	public void gainDefenseValue() {
		defenseValue += 2;
		doAction("look tougher");
	}

	public void gainVision() {
		visionRadius += 1;
		doAction("look more aware");
	}

	public String details() {
		return String.format("     level:%d     attack:%d     defense:%d     hp:%d", level, attackValue(),
				defenseValue(), hp);
	}

	public Item item(int wx, int wy, int wz) {
		if (canSee(wx, wy, wz))
			return world.item(wx, wy, wz);
		else
			return null;
	}

	public void throwItem(Item item, int wx, int wy, int wz) {
		Point end = new Point(x, y, 0);

		for (Point p : new Line(x, y, wx, wy)) {
			if (!realTile(p.x, p.y, z).isGround())
				break;
			end = p;
		}

		wx = end.x;
		wy = end.y;

		Creature c = creature(wx, wy, wz);

		if (c != null)
			throwAttack(item, c);
		else
			doAction("throw a %s", item.name());

		unequip(item);
		inventory.remove(item);

		if (item.quaffEffect() != null && c != null) {
			world.remove(item);
			notify("The "+item.name()+" breaks!");
		} else {
			world.addAtEmptySpace(item, wx, wy, wz);
		}		
	}

	private void throwAttack(Item item, Creature other) {
		modifyFood(-1);

		int amount = Math.max(0, attackValue / 2 + item.thrownAttackValue() - other.defenseValue());

		amount = (int) (Math.random() * amount) + 1;

		commonAttack(other, amount / 2 + item.thrownAttackValue(), "throw a %s at the %s for %d damage",
				item.name(), other.name);
		other.addEffect(item.quaffEffect());
		if (other.hp < 1)
			gainXp(other);
	}

	public void rangedWeaponAttack(Creature other) {
		modifyFood(-1);

		int amount = Math.max(0, attackValue / 2 + weapon.rangedAttackValue() - other.defenseValue());

		amount = (int) (Math.random() * amount) + 1;

		doAction("fire a %s at the %s for %d damage", weapon.name(), other.name, amount);

		other.modifyHp(-amount);

		if (other.hp < 1)
			gainXp(other);
	}

	private void commonAttack(Creature other, int attack, String action, Object... params) {
		modifyFood(-2);

		int amount = Math.max(0, attack - other.defenseValue());

		amount = (int) (Math.random() * amount) + 1;

		Object[] params2 = new Object[params.length + 1];
		for (int i = 0; i < params.length; i++) {
			params2[i] = params[i];
		}
		params2[params2.length - 1] = amount;

		doAction(action, params2);

		other.modifyHp(-amount);

		if (other.hp < 1)
			gainXp(other);
	}

	public void meleeAttack(Creature other) {
		commonAttack(other, attackValue(), "attack the %s for %d damage", other.name);
	}

	private void getRidOf(Item item) {
		inventory.remove(item);
		unequip(item);
	}

	private void putAt(Item item, int wx, int wy, int wz) {
		inventory.remove(item);
		unequip(item);
		world.addAtEmptySpace(item, wx, wy, wz);
	}

	public void quaff(Item item) {
		doAction("quaff a " + item.name());
		consume(item);
	}

	public void eat(Item item) {
		doAction("eat a " + item.name());
		consume(item);
	}

	private void consume(Item item) {
		if (item.foodValue() < 0)
			notify("Gross!");

		addEffect(item.quaffEffect());

		modifyFood(item.foodValue());
		getRidOf(item);
	}

	private void addEffect(Effect effect) {
		if (effect == null)
			return;

		effect.start(this);
		effects.add(effect);
	}

	private void updateEffects() {
		List<Effect> done = new ArrayList<Effect>();

		for (Effect effect : effects) {
			effect.update(this);
			if (effect.isDone()) {
				effect.end(this);
				done.add(effect);
			}
		}

		effects.removeAll(done);
	}

	public void buffAttackValue(int i) {
		attackValue = attackValue + i;

	}

	public void summon(Creature other) {
		world.add(other);
	}

	private int detectCreatures;

	public void modifyDetectCreatures(int amount) {
		detectCreatures += amount;
	}

	public void castSpell(Spell spell, int x2, int y2) {
		Creature other = creature(x2, y2, z);

		if (spell.manaCost() > mana) {
			doAction("point and mumble but nothing happens");
			return;
		} else if (other == null) {
			doAction("point and mumble at nothing");
			return;
		}

		other.addEffect(spell.effect());
		modifyMana(-spell.manaCost());
	}

}
