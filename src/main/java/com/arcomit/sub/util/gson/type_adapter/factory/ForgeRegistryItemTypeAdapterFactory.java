package com.arcomit.sub.util.gson.type_adapter.factory;

import com.arcomit.sub.util.gson.type_adapter.ForgeRegistryItemTypeAdapter;
import com.google.gson.Gson;
import com.google.gson.TypeAdapter;
import com.google.gson.TypeAdapterFactory;
import com.google.gson.reflect.TypeToken;
import net.minecraftforge.registries.IForgeRegistry;
import net.minecraftforge.registries.RegistryManager;

import java.util.function.Supplier;

/**
 * @author til
 */
public class ForgeRegistryItemTypeAdapterFactory<E> implements TypeAdapterFactory {
    protected final Class<? extends E> aClass;
    protected final Supplier<IForgeRegistry<? extends E>> forgeRegistry;

    public ForgeRegistryItemTypeAdapterFactory(Class<? extends E> aClass, Supplier<IForgeRegistry<? extends E>> forgeRegistry) {
        this.aClass = aClass;
        this.forgeRegistry = forgeRegistry;
    }

    @Override
    public <T> TypeAdapter<T> create(Gson gson, TypeToken<T> type) {

        if (!aClass.isAssignableFrom(type.getRawType())) {
            return null;
        }
        return new ForgeRegistryItemTypeAdapter(forgeRegistry.get());
    }
}
