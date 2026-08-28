package somdudewillson.redsparklib.common.data.task;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.slf4j.Logger;

import lombok.Value;
import lombok.val;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Tuple;
import net.minecraft.world.item.Item;
import net.minecraftforge.client.model.generators.ItemModelBuilder;
import net.minecraftforge.client.model.generators.ItemModelProvider;
import net.minecraftforge.registries.RegistryObject;

public class PropertyDependentItemModelDatagenTask<R extends Item> extends ItemModelDatagenTask<R> {
	private final OverridePredicateData[] predicates;

	public PropertyDependentItemModelDatagenTask(RegistryObject<R> targetRegistry, OverridePredicateData[] predicates) {
		super(targetRegistry);
		this.predicates = predicates;
	}

	@Override
	public void buildData(ItemModelProvider provider, Logger logger) {
		val itemResourceLocation = targetRegistry.getId();
		logger.debug("Generating property-dependent item model for {}...", itemResourceLocation);
		Map<ResourceLocation, List<OverridePredicateData>> keyValuesMap = Arrays.stream(predicates)
				.collect(Collectors.toUnmodifiableMap(
						OverridePredicateData::getKey, 
						d->new ArrayList<>(List.of(d)), 
						(a,b)->{
							a.addAll(b);
							return a;
						}));
		ItemModelBuilder itemModelBuilder = provider.getBuilder(itemResourceLocation.getPath())
				.parent(provider.getExistingFile(provider.mcLoc("item/generated")));
		
		List<ResourceLocation> propertyKeys = new ArrayList<>(keyValuesMap.keySet());
		propertyKeys.sort(null);
		if (propertyKeys.isEmpty()) {
			itemModelBuilder.texture("layer0", provider.modLoc("item/" + itemResourceLocation.getPath()));
			return;
		}
		
		List<List<OverridePredicateData>> propertyValues = propertyKeys.stream()
				.map(keyValuesMap::get)
				.collect(Collectors.toList());
		propertyValues.forEach(l->l.sort(null));

		int numProperties = propertyKeys.size();
		int[] combinationIndices = new int[numProperties];

		// Iterate through all combinations of predicate values
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
				itemModelBuilder.texture("layer0", provider.modLoc("item/" + modelName));
				isDefaultCombination = false;
			} else {
				// For all other combinations, we create a new model file & override
				provider.singleTexture(modelName, provider.mcLoc("item/generated"), "layer0", provider.modLoc("item/" + modelName));

				ItemModelBuilder.OverrideBuilder overrideBuilder = itemModelBuilder.override();
				for (OverridePredicateData predicate : currentCombination) {
					overrideBuilder.predicate(predicate.getKey(), predicate.getValue());
				}
				overrideBuilder.model(provider.getExistingFile(provider.modLoc("item/" + modelName))).end();
			}

			// Increment to the next combination
			int propertyToIncrement = numProperties - 1;
			while (propertyToIncrement >= 0) {
				combinationIndices[propertyToIncrement]++;
				if (combinationIndices[propertyToIncrement] < propertyValues.get(propertyToIncrement).size()) {
					break;
				}
				combinationIndices[propertyToIncrement] = 0;
				propertyToIncrement--;
			}

			if (propertyToIncrement < 0) {
				break;
			}
		}
	}

	@Override
	protected <L extends ItemModelProvider> void buildData(L dataProvider) { }

	
	@Value
	public static class OverridePredicateData implements Comparable<OverridePredicateData> {
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
}