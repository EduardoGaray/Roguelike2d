package New.Main;

import java.awt.Color;
import java.util.ArrayList;
import java.util.List;

public class Item {

	private char glyph;

	public char glyph() {
		return glyph;
	}

	private Color color;

	public Color color() {
		return color;
	}

	private String name;

	public String name() {
		return name;
	}

	private int foodValue;

	public int foodValue() {
		return foodValue;
	}

	public void modifyFoodValue(int amount) {
		foodValue += amount;
	}

	private int attackValue;

	public int attackValue() {
		return attackValue;
	}
	
	public void modifyAttackValue(int amount) {
		attackValue += amount;
	}
	
	private int thrownAttackValue;
    public int thrownAttackValue() { return thrownAttackValue; }
    public void modifyThrownAttackValue(int amount) { thrownAttackValue += amount; }
    
    private int rangedAttackValue;
    public int rangedAttackValue() { return rangedAttackValue; }
    public void modifyRangedAttackValue(int amount) { rangedAttackValue += amount; }
    
    private Effect quaffEffect;
    public Effect quaffEffect() { return quaffEffect; }
    public void setQuaffEffect(Effect effect) { this.quaffEffect = effect; }

	private int defenseValue;

	public int defenseValue() {
		return defenseValue;
	}

	public void modifyDefenseValue(int amount) {
		defenseValue += amount;
	}

	public Item(char glyph, Color color, String name, List<Spell> writtenSpells) {
		this.glyph = glyph;
		this.color = color;
		this.name = name;
		this.writtenSpells = new ArrayList<Spell>();
	}

	public String details() {
		String details = "";

		if (attackValue != 0)
			details += "     melee:" + attackValue;
		
		if (thrownAttackValue != 0)
			details += "     thrown:" + thrownAttackValue;

		if (defenseValue != 0)
			details += "     armor:" + defenseValue;

		if (foodValue != 0)
			details += "     food:" + foodValue;

		return details;
	}
	
	private List<Spell> writtenSpells;
    public List<Spell> writtenSpells() { return writtenSpells; }

    public void addWrittenSpell(String name, int manaCost, Effect effect){
        writtenSpells.add(new Spell(name, manaCost, effect));
    }
    
    private String appearance;
    public String appearance() { 
        if (appearance == null)
            return name;

        return appearance;
    }
}
