package com.meros.playn.core;

import com.meros.playn.core.Constants.Direction;
import com.meros.playn.core.entities.BossFloor;
import com.meros.playn.core.entities.BossWall;
import com.meros.playn.core.entities.BreakingHookTile;
import com.meros.playn.core.entities.Button;
import com.meros.playn.core.entities.Coin;
import com.meros.playn.core.entities.Door;
import com.meros.playn.core.entities.GroundWalkingMonster;
import com.meros.playn.core.entities.Hero;
import com.meros.playn.core.entities.LavaSea;
import com.meros.playn.core.entities.MovingHookTile;
import com.meros.playn.core.entities.Reactor;
import com.meros.playn.core.entities.SimpleWalkingMonster;
import com.meros.playn.core.entities.SpawnPoint;
import com.meros.playn.core.entities.Spike;
import com.meros.playn.core.entities.WallOfDeath;
import com.meros.playn.core.entities.WallOfDeathStarter;

public class EntityFactory {

	public static Entity create(int id) {
		switch (id) {
		case 0:
			return new Hero();
		case 1:
			return new Reactor();
		case 2:
			return new Coin();
		case 3:
			return new Spike();
		case 4:
			return new SpawnPoint();
		case 5:
			return new
					GroundWalkingMonster(GroundWalkingMonster.Type.FLOOR);
		case 6:
			return new GroundWalkingMonster(GroundWalkingMonster.Type.ROOF);
		case 7:
			return new
					GroundWalkingMonster(GroundWalkingMonster.Type.LEFT_WALL);
		case 8:
			return new
					GroundWalkingMonster(GroundWalkingMonster.Type.RIGHT_WALL);
		case 9:
			return new SimpleWalkingMonster();
		case 10:
			return new LavaSea();
		case 32:
			return new BreakingHookTile();
		case 33:
			return new Coin();
		case 34:
			return new MovingHookTile();
		case 64:
			return new Button(1);
		case 65:
			return new Door(1);
		case 96:
			return new WallOfDeath();
		case 97:
			return new WallOfDeathStarter();
		case 128:
			return new Dialogue("data/dialogues/1-tutorial1.txt");
		case 129:
			return new Dialogue("data/dialogues/2-tutorial2.txt");
		case 160:
			return new BossFloor();
		case 161:
			return new BossWall(Direction.RIGHT);
		case 162:
			return new BossWall(Direction.LEFT);
		case 163:
			return new Boss();
		}

		return null;
	}

}
