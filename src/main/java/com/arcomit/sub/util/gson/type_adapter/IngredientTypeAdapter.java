package com.arcomit.sub.util.gson.type_adapter;

import com.google.gson.JsonElement;
import com.google.gson.TypeAdapter;
import com.google.gson.internal.Streams;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonToken;
import com.google.gson.stream.JsonWriter;
import net.minecraft.world.item.crafting.Ingredient;

import java.io.IOException;

public class IngredientTypeAdapter extends TypeAdapter<Ingredient> {


    @Override
    public void write(JsonWriter out, Ingredient value) throws IOException {
        if (value == null) {
            out.nullValue();
            return;
        }
        JsonElement jsonElement = value.toJson();
        Streams.write(jsonElement, out);
    }

    @Override
    public Ingredient read(JsonReader in) throws IOException {
        if (in.peek().equals(JsonToken.NULL)) {
            return null;
        }
        return Ingredient.fromJson(Streams.parse(in));
    }
}
