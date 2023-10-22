package com.arcomit.sub.util.gson.type_adapter.factory;

import com.arcomit.sub.util.Util;
import com.arcomit.sub.util.gson.type_adapter.NBTTypeAdapter;
import com.google.gson.Gson;
import com.google.gson.TypeAdapter;
import com.google.gson.TypeAdapterFactory;
import com.google.gson.reflect.TypeToken;
import net.minecraft.nbt.Tag;

/**
 * @author til
 */
public class NBTTypeAdapterFactory implements TypeAdapterFactory {
    @Override
    public <T> TypeAdapter<T> create(Gson gson, TypeToken<T> type) {
        if (Tag.class.isAssignableFrom(type.getRawType())) {
            return Util.forcedConversion(new NBTTypeAdapter());
        }
        return null;
    }
}
