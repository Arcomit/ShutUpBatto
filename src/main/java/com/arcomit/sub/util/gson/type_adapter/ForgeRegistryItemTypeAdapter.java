package com.arcomit.sub.util.gson.type_adapter;

import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonToken;
import com.google.gson.stream.JsonWriter;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.registries.IForgeRegistry;

import javax.annotation.Nullable;
import java.io.IOException;

/**
 * @author til
 */
public class ForgeRegistryItemTypeAdapter<E> extends TypeAdapter<E> {

    public final IForgeRegistry<E> forgeRegistry;


    public ForgeRegistryItemTypeAdapter(IForgeRegistry<E> forgeRegistry) {
        this.forgeRegistry = forgeRegistry;
    }

    @Override
    public void write(JsonWriter out, E value) throws IOException {
        if (value == null) {
            out.nullValue();
            return;
        }
        ResourceLocation resourceLocation = forgeRegistry.getKey(value);
        if (resourceLocation == null) {
            out.nullValue();
            return;
        }
        out.value(resourceLocation.toString());
    }

    @Override
    public E read(JsonReader in) throws IOException {
        if (in.peek() == JsonToken.NULL) {
            return null;
        }
        ResourceLocation resourceLocation = new ResourceLocation(in.nextString());
        return forgeRegistry.getValue(resourceLocation);
    }
}
