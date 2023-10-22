package com.arcomit.sub.util;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.Property;

import javax.annotation.Nullable;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

/**
 * @author til
 */
public class BlockStateUtil {

    public static BlockState create(@Nullable Block block, Map<String, String> v) {
        assert block != null;
        BlockState blockState = block.defaultBlockState();
        if (v == null) {
            return blockState;
        }
        for (Map.Entry<Property<?>, Comparable<?>> entry : blockState.getValues().entrySet()) {
            if (!v.containsKey(entry.getKey().getName())) {
                continue;
            }
            Optional<?> vOptional = entry.getKey().getValue(v.get(entry.getKey().getName()));
            if (vOptional.isEmpty()) {
                continue;
            }
            blockState = blockState.setValue(entry.getKey(), Util.forcedConversion(vOptional.get()));
        }
        return blockState;
    }

    public static BlockState create(@Nullable Block block, JsonObject v) {
        assert block != null;
        if (v == null) {
            return block.defaultBlockState();
        }
        Map<String, String> map = new HashMap<>(0);
        for (Map.Entry<String, JsonElement> entry : v.entrySet()) {
            map.put(entry.getKey(), entry.getValue().getAsString());
        }
        return create(block, map);
    }


}
