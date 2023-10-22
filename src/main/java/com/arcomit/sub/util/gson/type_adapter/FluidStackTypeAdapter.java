package com.arcomit.sub.util.gson.type_adapter;

import com.arcomit.sub.util.NBTUtil;
import com.google.gson.JsonElement;
import com.google.gson.TypeAdapter;
import com.google.gson.internal.Streams;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonToken;
import com.google.gson.stream.JsonWriter;
import net.minecraft.nbt.CompoundTag;
import net.minecraftforge.fluids.FluidStack;

import java.io.IOException;

public class FluidStackTypeAdapter extends TypeAdapter<FluidStack> {

    @Override
    public void write(JsonWriter out, FluidStack value) throws IOException {
        if (value == null) {
            out.nullValue();
            return;
        }
        CompoundTag compoundNBT = value.writeToNBT(new CompoundTag());
        JsonElement jsonElement = NBTUtil.toJson(compoundNBT, true);
        Streams.write(jsonElement, out);
    }


    @Override
    public FluidStack read(JsonReader in) throws IOException {
        if (in.peek().equals(JsonToken.NULL)) {
            return null;
        }
        JsonElement jsonElement = Streams.parse(in);
        return FluidStack.loadFluidStackFromNBT((CompoundTag) NBTUtil.toTag(jsonElement));
    }
}
