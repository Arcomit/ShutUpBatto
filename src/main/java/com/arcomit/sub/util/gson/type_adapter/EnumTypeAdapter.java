package com.arcomit.sub.util.gson.type_adapter;

import com.arcomit.sub.util.Util;
import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonToken;
import com.google.gson.stream.JsonWriter;

import java.io.IOException;

public class EnumTypeAdapter extends TypeAdapter<Enum<?>> {
    protected final Class<? extends Enum<?>> enumClass;

    public EnumTypeAdapter(Class<? extends Enum<?>> enumClass) {
        this.enumClass = enumClass;
    }

    @Override
    public void write(JsonWriter out, Enum<?> value) throws IOException {
        if (value == null) {
            return;
        }
        out.value(value.name());
    }

    @Override
    public Enum<?> read(JsonReader in) throws IOException {
        if (in.peek().equals(JsonToken.NULL)) {
            return null;
        }
        return Enum.valueOf(Util.forcedConversion(enumClass), in.nextString());
    }
}
