package com.arcomit.sub.util.gson.type_adapter;

import com.arcomit.sub.util.NBTUtil;
import com.google.gson.TypeAdapter;
import com.google.gson.internal.Streams;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonToken;
import com.google.gson.stream.JsonWriter;
import net.minecraft.nbt.Tag;

import java.io.IOException;

/**
 * @author til
 */
public class NBTTypeAdapter extends TypeAdapter<Tag> {

    @Override
    public void write(JsonWriter out, Tag value) throws IOException {
        if (value == null) {
            out.nullValue();
            return;
        }
        Streams.write(NBTUtil.toJson(value, true), out);
    }

    @Override
    public Tag read(JsonReader in) throws IOException {
        if (in.peek().equals(JsonToken.NULL)) {
            return null;
        }
        return NBTUtil.toTag(Streams.parse(in));
    }
}
