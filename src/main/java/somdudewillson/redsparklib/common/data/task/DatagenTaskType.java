package somdudewillson.redsparklib.common.data.task;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;

@RequiredArgsConstructor
@FieldDefaults(makeFinal=true, level=AccessLevel.PRIVATE)
@Getter
public enum DatagenTaskType {
	ITEM_MODEL,
	BLOCK_MODEL,
	ITEM_TAGS,
	BLOCK_TAGS,
	BLOCK_LOOT;
}