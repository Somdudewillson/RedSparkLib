package somdudewillson.redsparklib.platform.forge.common.utils;

//? forge {
import java.util.stream.IntStream;

import org.jetbrains.annotations.NotNull;

import lombok.val;
import net.minecraft.world.Container;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.common.capabilities.ForgeCapabilities;

public interface ItemHandlerContainer extends Container, ICapabilityProvider {

	public default @NotNull LazyOptional<IItemHandler> getItemHandler() {
		return getCapability(ForgeCapabilities.ITEM_HANDLER);
	}

	@Override
	public default boolean isEmpty() {
		return getItemHandler()
				.map(h->IntStream.range(0, h.getSlots()).allMatch(slot->h.getStackInSlot(slot).isEmpty()))
				.orElse(true);
	}

	@Override
	public default void clearContent() {
		getItemHandler().ifPresent(h->{
			boolean anyRemoved = false;
			boolean itemRemoved;
			do {
				itemRemoved = false;
				for (int slot=0;slot<h.getSlots();slot++) {
					itemRemoved |= !h.extractItem(slot, Integer.MAX_VALUE, false).isEmpty();
				}
				anyRemoved |= itemRemoved;
			} while (itemRemoved);
			
			if (anyRemoved) { this.setChanged(); }
		});
	}

	@Override
	public default int getContainerSize() {
		return getItemHandler()
				.map(h->h.getSlots())
				.orElse(0);
	}

	@Override
	public default ItemStack getItem(int pSlot) {
		return getItemHandler()
				.map(h->h.getStackInSlot(pSlot))
				.orElse(ItemStack.EMPTY);
	}

	@Override
	public default ItemStack removeItem(int pSlot, int pAmount) {
		val resultStack = getItemHandler()
				.map(h->h.extractItem(pSlot, pAmount, false))
				.orElse(ItemStack.EMPTY);
		if (!resultStack.isEmpty()) { this.setChanged(); }
		return resultStack;
	}

	@Override
	public default ItemStack removeItemNoUpdate(int pSlot) {
		val resultStack = getItemHandler()
				.map(h->h.extractItem(pSlot, Integer.MAX_VALUE, false))
				.orElse(ItemStack.EMPTY);
		if (!resultStack.isEmpty()) { this.setChanged(); }
		return resultStack;
	}

	@Override
	public default void setItem(int pSlot, ItemStack pStack) {
		getItemHandler().ifPresent(h->{
			val extractResult = h.extractItem(pSlot, Integer.MAX_VALUE, false);
			val insertResult = h.insertItem(pSlot, pStack, false);

			if (!extractResult.isEmpty() || !insertResult.equals(pStack, true)) { this.setChanged(); }
		});
	}

	@Override
	public default int getMaxStackSize() {
		return getItemHandler()
				.map(h->
					IntStream.range(0, h.getSlots()).map(slot->h.getSlotLimit(slot)).max().orElse(0)
				)
				.orElse(0);
	}

	@Override
	public default boolean canPlaceItem(int pIndex, ItemStack pStack) {
		return getItemHandler()
				.map(h->h.isItemValid(pIndex, pStack))
				.orElse(false);
	}

	@Override
	public default boolean canTakeItem(Container pTarget, int pIndex, ItemStack pStack) {
		return getItemHandler()
				.map(h->h.extractItem(pIndex, pStack.getCount(), true))
				.map(s->s.equals(pStack, true))
				.orElse(false);
	}

}
//?}
