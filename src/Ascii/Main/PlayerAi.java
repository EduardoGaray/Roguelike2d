package Ascii.Main;

import java.util.List;

public class PlayerAi extends CreatureAi {

	private List<String> messages;
	private FieldOfView fov;
	
	public void setFov(FieldOfView fov) {
		this.fov = fov;
	}
	public PlayerAi(Creature creature, World world, List<String> messages, FieldOfView fov) {
        super(creature);
        this.messages = messages;
        this.fov = fov;
    }

	public void onEnter(int x, int y, int z, Tile tile){
        if (tile.isGround()){
         creature.x = x;
         creature.y = y;
         creature.z = z;
        } else if (tile.isDiggable()) {
         creature.dig(x, y, z);
        }
    }
       
    public void onNotify(String message) {
		messages.add(message);
	}
    
    public void onGainLevel(){
    }
    
    public boolean canSee(int wx, int wy, int wz) {
        return fov.isVisible(wx, wy, wz);
    }
    
    public Tile rememberedTile(int wx, int wy, int wz) {
        return fov.tile(wx, wy, wz);
    }
             
}