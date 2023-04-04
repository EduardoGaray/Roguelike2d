package New.Main;

import java.util.List;

public class LevelUpController {

  private static LevelUpOption[] options = new LevelUpOption[]{
    new LevelUpOption("Increased hit points"){
      public void invoke(Creature creature) { creature.gainMaxHp(); }
    },
    new LevelUpOption("Increased attack value"){
      public void invoke(Creature creature) { creature.gainAttackValue(); }
    },
    new LevelUpOption("Increased defense value"){
      public void invoke(Creature creature) { creature.gainDefenseValue(); }
    },
    new LevelUpOption("Increased vision"){
      public void invoke(Creature creature) { creature.gainVision(); }
    },
    new LevelUpOption("Increased mana"){
        public void invoke(Creature creature) { creature.gainMaxMana(); }
    },
    new LevelUpOption("Increased mana regeneration"){
        public void invoke(Creature creature) { creature.gainRegenMana(); }
    }
  };
  
  public void autoLevelUp(Creature creature){
	    options[(int)(Math.random() * options.length)].invoke(creature);
	  }

public List<String> getLevelUpOptions() {
	return null;
}

public void getLevelUpOption(Creature creature) {
	 options[(int)(Math.random() * options.length)].invoke(creature);
	
}

}
