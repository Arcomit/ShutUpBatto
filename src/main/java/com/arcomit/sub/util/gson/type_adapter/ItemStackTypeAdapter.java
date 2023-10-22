package com.arcomit.sub.util.gson.type_adapter;

import com.arcomit.sub.util.NBTUtil;
import com.google.gson.JsonElement;
import com.google.gson.TypeAdapter;
import com.google.gson.internal.Streams;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonToken;
import com.google.gson.stream.JsonWriter;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.item.ItemStack;

import java.io.IOException;

public class ItemStackTypeAdapter extends TypeAdapter<ItemStack> {

    @Override
    public void write(JsonWriter out, ItemStack value) throws IOException {
        if (value == null) {
            out.nullValue();
            return;
        }
        CompoundTag compoundNBT = value.serializeNBT();
        JsonElement jsonElement = NBTUtil.toJson(compoundNBT, true);
        Streams.write(jsonElement, out);
    }

    @Override
    public ItemStack read(JsonReader in) throws IOException {
        if (in.peek().equals(JsonToken.NULL)) {
            return null;
        }
        JsonElement jsonElement = Streams.parse(in);
        return ItemStack.of((CompoundTag) NBTUtil.toTag(jsonElement));
    }
}
