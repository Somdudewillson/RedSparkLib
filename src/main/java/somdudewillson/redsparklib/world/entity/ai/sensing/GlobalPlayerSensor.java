package somdudewillson.redsparklib.world.entity.ai.sensing;

import java.util.Comparator;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import com.google.common.collect.ImmutableSet;

import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.EntitySelector;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.Brain;
import net.minecraft.world.entity.ai.memory.MemoryModuleType;
import net.minecraft.world.entity.ai.sensing.Sensor;
import net.minecraft.world.entity.player.Player;
import somdudewillson.redsparklib.common.RedSparkLibMemoryModules;

public class GlobalPlayerSensor extends Sensor<LivingEntity> {

	@Override
	protected void doTick(ServerLevel pLevel, LivingEntity pEntity) {
	      Brain<?> brain = pEntity.getBrain();
	      List<Player> playerList = pLevel.players().stream()
	    		  .filter(EntitySelector.NO_SPECTATORS)
	    		  .sorted(Comparator.comparingDouble(pEntity::distanceToSqr))
	    		  .collect(Collectors.toList());
	      
	      Player closestCreative = null;
	      for (Player player : playerList) {
	    	  if (!player.isCreative()) {
	    	      brain.setMemory(RedSparkLibMemoryModules.NEAREST_PLAYER_GLOBAL.get(), player);
	    		  return;
	    	  } else if (closestCreative == null) {
	    		  closestCreative = player;
	    	  }
	      }
	      brain.setMemory(RedSparkLibMemoryModules.NEAREST_PLAYER_GLOBAL.get(), closestCreative);
	}

	@Override
	public Set<MemoryModuleType<?>> requires() {
		return ImmutableSet.of(RedSparkLibMemoryModules.NEAREST_PLAYER_GLOBAL.get());
	}

}
