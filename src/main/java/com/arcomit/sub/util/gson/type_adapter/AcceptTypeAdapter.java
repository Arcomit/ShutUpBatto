package com.arcomit.sub.util.gson.type_adapter;

import com.arcomit.sub.ShutUpBattoMod;
import com.arcomit.sub.util.gson.GsonManage;
import com.arcomit.sub.util.Util;
import com.google.gson.*;
import com.google.gson.internal.Streams;
import com.google.gson.reflect.TypeToken;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonToken;
import com.google.gson.stream.JsonWriter;

import java.io.IOException;
import java.util.Map;

/**
 * @author til
 */
public class AcceptTypeAdapter<T> extends TypeAdapter<T> {
    public static final String TYPE = "$type";
    public static final String GENERIC = "$generic";
    public final TypeAdapter<T> typeAdapter;
    public final Gson gson;
    public final TypeToken<T> typeToken;

    public AcceptTypeAdapter(Gson gson, TypeToken<T> type, TypeAdapter<T> typeAdapter) {
        this.typeAdapter = typeAdapter;
        this.gson = gson;
        this.typeToken = type;
    }

    @Override
    public void write(JsonWriter out, T value) throws IOException {
        if (value == null) {
            out.nullValue();
            return;
        }
        JsonObject jsonObject = new JsonObject();
        jsonObject.add(TYPE, new JsonPrimitive(value.getClass().getName()));
        JsonElement vJson;
        if (typeToken.getRawType().equals(value.getClass())) {
            vJson = typeAdapter.toJsonTree(value);
        } else {
            vJson =  gson.getAdapter(value.getClass()).toJsonTree(Util.forcedConversion(value));
        }
        if (vJson.isJsonObject()) {
            for (Map.Entry<String, JsonElement> entry : vJson.getAsJsonObject().entrySet()) {
                jsonObject.add(entry.getKey(), entry.getValue());
            }
        } else {
            jsonObject.add(GsonManage.CONFIG, vJson);
        }
        Streams.write(jsonObject, out);
    }

    @Override
    public T read(JsonReader in) throws IOException {
        if (in.peek().equals(JsonToken.NULL)) {
            return null;
        }
        JsonObject jsonObject = Streams.parse(in).getAsJsonObject();
        Class<T> type;
        try {
            type = Util.forcedConversion(Class.forName(jsonObject.get(TYPE).getAsString()));
        } catch (ClassNotFoundException e) {
            ShutUpBattoMod.LOGGER_4.error(e);
            return null;
        }
        boolean isInertia = !jsonObject.has(GsonManage.CONFIG);
        JsonElement vJson = isInertia ? jsonObject : jsonObject.get(GsonManage.CONFIG);
        if (typeToken.getRawType().equals(type)) {
            return typeAdapter.fromJsonTree(vJson);
        } else {
            return gson.getAdapter(type).fromJsonTree(vJson);
        }
    }
}
