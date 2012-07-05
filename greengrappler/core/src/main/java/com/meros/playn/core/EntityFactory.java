package com.meros.playn.core;

public class EntityFactory {

	public static Entity create(int id) {
		switch(id)
		{
		case 0:
			return new Hero();
		case 1:
			return new Reactor();
		case 2:
			return new Coin();
		case 3:
			return new Spike();
			//		case 4:
			//			return new SpawnPoint();
			//		case 5:
			//			return new GroundWalkingMonster(GroundWalkingMonster::Type_Floor);
			//		case 6:
			//			return new GroundWalkingMonster(GroundWalkingMonster::Type_Roof);
			//		case 7:
			//			return new GroundWalkingMonster(GroundWalkingMonster::Type_LeftWall);
			//		case 8:
			//			return new GroundWalkingMonster(GroundWalkingMonster::Type_RightWall);
		case 9:
			return new SimpleWalkingMonster();
			//		case 10:
			//			return new LavaSea();
			//
		case 32:
			return new BreakingHookTile();
			//		case 33:
			//			return new Coin();
			//		case 34:
			//			return new MovingHookTile();
			//		case 64:
			//			return new Button(1);
			//		case 65:
			//			return new Door(1);
			//
			//		case 96:
			//			return new WallOfDeath();
			//		case 97:
			//			return new WallOfDeathStarter();
			//
			//		case 128:
			//			return new Dialogue("data/dialogues/1-tutorial1.txt");
			//		case 129:
			//			return new Dialogue("data/dialogues/2-tutorial2.txt");
			//
			//		case 160:
			//			return new BossFloor();
			//		case 161:
			//			return new BossWall(Direction_Right);
			//		case 162:
			//			return new BossWall(Direction_Left);
			//		case 163:
			//			return new Boss();
		}

		return null;
	}

}
