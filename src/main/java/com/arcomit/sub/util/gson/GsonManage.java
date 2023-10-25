package com.arcomit.sub.util.gson;

import com.arcomit.sub.ShutUpBattoMod;
import com.arcomit.sub.util.ReflexUtil;
import com.arcomit.sub.util.Util;
import com.arcomit.sub.util.gson.type_adapter.*;
import com.arcomit.sub.util.gson.type_adapter.factory.*;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.storage.loot.LootTable;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.IForgeRegistry;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.Random;
import java.util.function.Supplier;

/**
 * @author til
 */
public class GsonManage {

    public static final String TYPE = "$type";
    public static final String GENERIC = "$generic";
    public static final String CONFIG = "$config";

    protected static Gson gson;

    public static Gson getInstance() {
        if (gson == null) {
            Field gsonBuilder_excluder;
            Field gson_factories;
            try {
                gsonBuilder_excluder = GsonBuilder.class.getDeclaredField("excluder");
                gsonBuilder_excluder.setAccessible(true);
                gson_factories = Gson.class.getDeclaredField("factories");
                gson_factories.setAccessible(true);
            } catch (NoSuchFieldException e) {
                throw new RuntimeException(e);
            }

            GsonBuilder gsonBuilder = new GsonBuilder();
            gsonBuilder.setPrettyPrinting();

            gsonBuilder.registerTypeAdapter(Random.class, new RandomTypeAdapter());
            gsonBuilder.registerTypeAdapter(ResourceLocation.class, new ResourceLocationTypeAdapter());
            gsonBuilder.registerTypeAdapter(ItemStack.class, new ItemStackTypeAdapter());
            gsonBuilder.registerTypeAdapter(FluidStack.class, new FluidStackTypeAdapter());
            gsonBuilder.registerTypeAdapter(BlockState.class, new BlockStateTypeAdapter());
            gsonBuilder.registerTypeAdapter(Ingredient.class, new IngredientTypeAdapter());
            gsonBuilder.registerTypeAdapter(LootTable.class, new LootTable.Serializer());

            for (Field field : ForgeRegistries.class.getFields()) {
                if (Modifier.isInterface(field.getModifiers())) {
                    continue;
                }
                if (!Modifier.isFinal(field.getModifiers())) {
                    continue;
                }
                if (IForgeRegistry.class.isAssignableFrom(field.getType())) {
                    gsonBuilder.registerTypeAdapterFactory(new ForgeRegistryItemTypeAdapterFactory(field.getType(), () -> {
                        try {
                            return Util.forcedConversion(field.get(null));
                        } catch (IllegalAccessException e) {
                            throw new RuntimeException(e);
                        }
                    }));
                }
                if (Supplier.class.isAssignableFrom(field.getType())) {
                    Type type = field.getGenericType();
                    if (type instanceof Class) {
                        continue;
                    }
                    ParameterizedType parameterized = (ParameterizedType) type;
                    Type[] types = parameterized.getActualTypeArguments();
                    if (types.length != 1) {
                        continue;
                    }
                    Type forgeRegistryType = types[0];
                    Class<?> forgeRegistryClass = forgeRegistryType.getClass();
                    if (!IForgeRegistry.class.isAssignableFrom(forgeRegistryClass)) {
                        continue;
                    }
                    gsonBuilder.registerTypeAdapterFactory(new ForgeRegistryItemTypeAdapterFactory(forgeRegistryClass, () -> {
                        try {
                            return ((Supplier)field.get(null)).get();
                        } catch (IllegalAccessException e) {
                            throw new RuntimeException(e);
                        }
                    }));
                }
            }

            gsonBuilder.registerTypeAdapterFactory(new EnumTypeAdapterFactory());
            gsonBuilder.registerTypeAdapterFactory(new BlockStateTypeAdapterFactory());
            gsonBuilder.registerTypeAdapterFactory(new DelayedTypeAdapterFactory());
            gsonBuilder.registerTypeAdapterFactory(new AcceptTypeAdapterFactory());
            //gsonBuilder.registerTypeAdapterFactory(new RegisterBasicsAdapterFactory());
            //TODO
            gsonBuilder.registerTypeAdapterFactory(new NBTTypeAdapterFactory());
            gsonBuilder.registerTypeAdapterFactory(new MapTypeAdapterFactory());

            gson = gsonBuilder.create();
        }
        return gson;
    }
}
