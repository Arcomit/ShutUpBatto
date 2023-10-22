package com.arcomit.sub.util.gson.type_adapter;

import com.arcomit.sub.util.Util;
import com.google.gson.Gson;
import com.google.gson.TypeAdapter;
import com.google.gson.reflect.TypeToken;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonToken;
import com.google.gson.stream.JsonWriter;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class MapTypeAdapter<K, V> extends TypeAdapter<Map<K, V>> {

    public final Gson gson;
    public final Class<Map<K, V>> mapClass;
    public final TypeToken<K> k;
    public final TypeToken<V> v;

    public static final String K = "k";
    public static final String V = "v";

    public MapTypeAdapter(Gson gson, Class<Map<K, V>> mapClass, TypeToken<K> k, TypeToken<V> v) {
        this.gson = gson;
        this.mapClass = mapClass;
        this.k = k;
        this.v = v;
    }

    @Override
    public void write(JsonWriter out, Map<K, V> value) throws IOException {
        if (value == null) {
            out.nullValue();
            return;
        }
        out.beginArray();
        for (Map.Entry<K, V> entry : value.entrySet()) {
            out.beginObject();
            out.name(K);
            gson.getAdapter(this.k).write(out, Util.forcedConversion(entry.getKey()));
            out.name(V);
            gson.getAdapter(this.v).write(out, Util.forcedConversion(entry.getValue()));
            out.endObject();
        }
        out.endArray();
    }

    @Override
    public Map<K, V> read(JsonReader in) throws IOException {
        if (in.peek().equals(JsonToken.NULL)) {
            return null;
        }
        Map<K, V> map;
        try {
            map = mapClass.newInstance();
        } catch (InstantiationException | IllegalAccessException e) {
            map = new HashMap<>();
        }
        in.beginArray();
        while (in.hasNext()) {
            in.beginObject();
            K k = null;
            V v = null;
            while (in.hasNext()) {
                String name = in.nextName();
                if (name.equals(K)) {
                    k = gson.getAdapter(this.k).read(in);
                }
                if (name.equals(V)) {
                    v = gson.getAdapter(this.v).read(in);
                }
            }
            if (k != null) {
                map.put(k, v);
            }
            in.endObject();
        }
        in.endArray();
        return map;
    }
}
