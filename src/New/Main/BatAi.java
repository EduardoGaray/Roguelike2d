package New.Main;

public class BatAi extends CreatureAi {

    public BatAi(Creature creature) {
        super(creature);
    }

    public void onUpdate(){
    	//this means the bat will move twice for every time you move, a time > move management system should be considered
        wander();
        wander();
    }
      
}
