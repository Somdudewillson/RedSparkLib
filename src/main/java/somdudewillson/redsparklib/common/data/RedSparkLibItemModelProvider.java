package somdudewillson.redsparklib.common.data;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;

import com.mojang.logging.LogUtils;

import lombok.Value;
import net.minecraft.data.PackOutput;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Tuple;
import net.minecraftforge.client.model.generators.ItemModelBuilder;
import net.minecraftforge.client.model.generators.ItemModelProvider;
import net.minecraftforge.common.data.ExistingFileHelper;
import somdudewillson.redsparklib.common.data.task.SimpleDatagenTask;

public class RedSparkLibItemModelProvider extends ItemModelProvider {
    protected static final Logger LOGGER = LogUtils.getLogger();
	private final RedSparkLibSimpleDatagenManager manager;

	public RedSparkLibItemModelProvider(RedSparkLibSimpleDatagenManager manager,
			PackOutput output, String modid, ExistingFileHelper existingFileHelper) {
		super(output, modid, existingFileHelper);
		this.manager = manager;
	}

	@Override
	protected void registerModels() {
		LOGGER.debug("Generating simple item models for {}...", manager.getModid());
		List<SimpleDatagenTask<Object, RedSparkLibItemModelProvider>> datagenTasks = manager.getFilteredDatagenTasks(RedSparkLibItemModelProvider.class);
		for (SimpleDatagenTask<?, RedSparkLibItemModelProvider> genTask : datagenTasks) {
			genTask.buildData(this);
		}
		LOGGER.debug("Generated {} simple item models for {}.", datagenTasks.size(), manager.getModid());
		manager.markDatagenRun();
	}
	
	protected void buildPropertyDependentModel(ResourceLocation itemResourceLocation, OverridePredicateData... predicates) {
		buildPropertyDependentModel(itemResourceLocation, Arrays.asList(predicates));
	}
	protected void buildPropertyDependentModel(ResourceLocation itemResourceLocation, List<OverridePredicateData> predicates) {
		LOGGER.debug("Generating property-dependent item model for {}...", itemResourceLocation);
		Map<ResourceLocation, List<OverridePredicateData>> keyValuesMap = predicates.stream()
				.collect(Collectors.toUnmodifiableMap(
						OverridePredicateData::getKey, 
						d->new ArrayList<>(List.of(d)), 
						(a,b)->{
							a.addAll(b);
							return a;
						}));
		ItemModelBuilder itemModelBuilder = getBuilder(itemResourceLocation.getPath())
				.parent(getExistingFile(mcLoc("item/generated")));
		
		List<ResourceLocation> propertyKeys = new ArrayList<>(keyValuesMap.keySet());
		propertyKeys.sort(null);
		if (propertyKeys.isEmpty()) {
			itemModelBuilder.texture("layer0", modLoc("item/" + itemResourceLocation.getPath()));
			return;
		}
		
		List<List<OverridePredicateData>> propertyValues = propertyKeys.stream()
				.map(keyValuesMap::get)
				.collect(Collectors.toList());
		propertyValues.forEach(l->l.sort(null));

		int numProperties = propertyKeys.size();
		int[] combinationIndices = new int[numProperties];

		// This loop iterates through all combinations of predicate values
		boolean isDefaultCombination = true;
		while (true) {
			List<OverridePredicateData> currentCombination = new ArrayList<>();
			List<Tuple<ResourceLocation, String>> overrideNameParts = new ArrayList<>();
			for (int i = 0; i < numProperties; i++) {
				OverridePredicateData predicateData = propertyValues.get(i).get(combinationIndices[i]);
				currentCombination.add(predicateData);
				overrideNameParts.add(new Tuple<>(predicateData.getKey(), predicateData.getOverrideName()));
			}

			String modelSuffix = overrideNameParts.stream()
					.sorted(Comparator.comparing(Tuple::getA))
					.map(Tuple::getB)
					.filter(s -> s != null && !s.isEmpty())
					.collect(Collectors.joining("_"));
			String modelName = itemResourceLocation.getPath() + (modelSuffix.isEmpty() ? "" : "_" + modelSuffix);

			if (isDefaultCombination) {
				// The first combination [0,0,...] sets the base texture for the main item model
				itemModelBuilder.texture("layer0", modLoc("item/" + modelName));
				isDefaultCombination = false;
			} else {
				// For all other combinations, we create a new model file & override
				singleTexture(modelName, mcLoc("item/generated"), "layer0", modLoc("item/" + modelName));

				ItemModelBuilder.OverrideBuilder overrideBuilder = itemModelBuilder.override();
				for (OverridePredicateData predicate : currentCombination) {
					overrideBuilder.predicate(predicate.getKey(), predicate.getValue());
				}
				overrideBuilder.model(getExistingFile(modLoc("item/" + modelName))).end();
			}

			// "Increment" to the next combination
			int propertyToIncrement = numProperties - 1;
			while (propertyToIncrement >= 0) {
				combinationIndices[propertyToIncrement]++;
				if (combinationIndices[propertyToIncrement] < propertyValues.get(propertyToIncrement).size()) {
					break; // Found next combination
				}
				combinationIndices[propertyToIncrement] = 0; // Reset and carry over
				propertyToIncrement--;
			}

			if (propertyToIncrement < 0) {
				break; // All combinations processed
			}
		}
	}
	
	@Value
	protected static class OverridePredicateData implements Comparable<OverridePredicateData> {
		private static final Comparator<OverridePredicateData> COMPARATOR = Comparator
				.<OverridePredicateData, ResourceLocation>comparing(OverridePredicateData::getKey)
				.thenComparingDouble(OverridePredicateData::getValue);
		
		String overrideName;
		ResourceLocation key;
		float value;
		
		@Override
		public int compareTo(OverridePredicateData o) {
			return COMPARATOR.compare(this, o);
		}
	}
	
	@Override
	public @NotNull String getName() {
        return "RedSparkLib Item Models: " + manager.getModid();
	}
}
