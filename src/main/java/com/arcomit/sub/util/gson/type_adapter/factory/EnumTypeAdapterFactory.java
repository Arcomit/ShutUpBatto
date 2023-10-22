package com.arcomit.sub.util.gson.type_adapter.factory;

import com.arcomit.sub.util.Util;
import com.arcomit.sub.util.gson.type_adapter.EnumTypeAdapter;
import com.google.gson.Gson;
import com.google.gson.TypeAdapter;
import com.google.gson.TypeAdapterFactory;
import com.google.gson.reflect.TypeToken;

public class EnumTypeAdapterFactory implements TypeAdapterFactory {
    @Override
    public <T> TypeAdapter<T> create(Gson gson, TypeToken<T> type) {
        Class<? super T> rawType = type.getRawType();
        if (!Enum.class.isAssignableFrom(rawType) || rawType == Enum.class) {
            return null;
        }
        if (!rawType.isEnum()) {
            rawType = rawType.getSuperclass(); // handle anonymous subclasses
        }
        return Util.forcedConversion(new EnumTypeAdapter(Util.forcedConversion(rawType)));
    }
}
