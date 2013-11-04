package greymerk.roguelike;

import net.minecraft.src.Tuple;

public enum Cardinal {

	NORTH, EAST, WEST, SOUTH;
	
	private static final Tuple northT = new Tuple(0, -1); 
	private static final Tuple southT = new Tuple(0, 1);
	private static final Tuple westT = new Tuple(-1, 0);
	private static final Tuple eastT = new Tuple(1, 0);
	
	public static Tuple getTuple(Cardinal in){	
		switch(in){
		case NORTH: return northT;
		case EAST: return eastT;
		case WEST: return westT;
		case SOUTH: return southT;
		default: return null;
		}
	}
	
	public static int getBlockMeta(Cardinal in){
		switch(in){
		case NORTH: return 2;
		case EAST: return 1;
		case WEST: return 0;
		case SOUTH: return 3;
		default: return 0;
		}
	}
	
	
	public static Cardinal reverse(Cardinal in){
		switch(in){
		case NORTH: return SOUTH;
		case EAST: return WEST;
		case WEST: return EAST;
		case SOUTH: return NORTH;
		default: return null;
		}
	}

	public static Cardinal[] getOrthogonal(Cardinal dir) {
		if(dir == NORTH || dir == SOUTH){
			return new Cardinal[] {EAST, WEST};
		} else {
			return new Cardinal[] {NORTH, SOUTH};
		}
	}
}