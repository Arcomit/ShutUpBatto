package com.arcomit.sub.util;

import com.google.gson.*;
import net.minecraft.nbt.*;
import net.minecraftforge.registries.tags.ITag;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * @author til
 */
public class NBTUtil {

    /***
     * 删除一个nbt里的所有元素
     */
    public static void clear(CompoundTag compoundTag) {
        Set<String> strings = compoundTag.getAllKeys();
        if (strings.isEmpty()) {
            return;
        }
        for (String string : strings) {
            compoundTag.remove(string);
        }
    }

    public static void clear(JsonObject jsonObject) {
        for (Map.Entry<String, JsonElement> entry : jsonObject.entrySet()) {
            jsonObject.remove(entry.getKey());
        }
    }

    /***
     * 复制一个CompoundTag
     */
    public static void copy(CompoundTag old, CompoundTag compoundTag) {
        clear(old);
        Set<String> strings = compoundTag.getAllKeys();
        if (strings.isEmpty()) {
            return;
        }
        for (String string : strings) {
            Tag tag = compoundTag.get(string);
            if (tag == null) {
                continue;
            }
            old.put(string, tag);
        }
    }

    public static Tag toTag(JsonElement jsonElement) {
        if (jsonElement.isJsonNull()) {
            return StringTag.valueOf("null");
        }
        if (jsonElement.isJsonPrimitive()) {
            JsonPrimitive jsonPrimitive = jsonElement.getAsJsonPrimitive();
            if (jsonPrimitive.isBoolean()) {
                return jsonPrimitive.getAsBoolean() ? StringTag.valueOf("true") : StringTag.valueOf("false");
            }
            if (jsonPrimitive.isString()) {
                String s = jsonElement.getAsJsonPrimitive().getAsString();
                if (s.isEmpty()) {
                    return StringTag.valueOf("");
                }
                char c = s.charAt(s.length() - 1);
                String ns = s.substring(0, s.length() - 1);
                if (StringUtil.checkStrIsNum(ns)) {
                    switch (c) {
                        case 'B':
                            return ByteTag.valueOf(Byte.parseByte(ns));
                        case 'S':
                            return ShortTag.valueOf(Short.parseShort(ns));
                        case 'I':
                            return IntTag.valueOf(Integer.parseInt(ns));
                        case 'L':
                            return LongTag.valueOf(Long.parseLong(ns));
                        case 'F':
                            return FloatTag.valueOf(Float.parseFloat(ns));
                        case 'D':
                            return DoubleTag.valueOf(Double.parseDouble(ns));
                    }
                }
                return StringTag.valueOf(jsonElement.getAsString());
            }
            if (jsonPrimitive.isNumber()) {
                return DoubleTag.valueOf(jsonPrimitive.getAsDouble());
            }
        }
        if (jsonElement.isJsonArray()) {
            ListTag listTag = new ListTag();
            for (JsonElement element : jsonElement.getAsJsonArray()) {
                listTag.add(toTag(element));
            }
            return listTag;
        }
        if (jsonElement.isJsonObject()) {
            JsonObject jsonObject = jsonElement.getAsJsonObject();
            if (jsonObject.size() == 2 && jsonObject.has(LIST_TAG) && jsonObject.has(LIST)) {
                String tag = jsonObject.get(LIST_TAG).getAsString();
                JsonArray arrayList = jsonObject.getAsJsonArray(LIST);
                List<JsonElement> list = new ArrayList<>(arrayList.size());
                for (JsonElement element : arrayList) {
                    list.add(element);
                }
                switch (tag) {
                    case "B":
                        return new ByteArrayTag(list.stream().map(JsonElement::getAsByte).collect(Collectors.toList()));
                    case "I":
                        return new IntArrayTag(list.stream().map(JsonElement::getAsInt).collect(Collectors.toList()));
                    case "L":
                        return new LongArrayTag(list.stream().map(JsonElement::getAsLong).collect(Collectors.toList()));
                }
            }
            CompoundTag compoundTag = new CompoundTag();
            for (Map.Entry<String, JsonElement> entry : jsonElement.getAsJsonObject().entrySet()) {
                compoundTag.put(entry.getKey(), toTag(entry.getValue()));
            }
            return compoundTag;
        }
        return new CompoundTag();
    }

    public static final String LIST_TAG = "$list_tag";
    public static final String LIST = "$list";

    public static JsonElement toJson(Tag tag, boolean hasType) {
        if (tag == null) {
            return JsonNull.INSTANCE;
        }
        if (tag instanceof StringTag) {
            StringTag stringTag = (StringTag) tag;
            if (stringTag.getAsString().equals("null")) {
                return JsonNull.INSTANCE;
            }
            return new JsonPrimitive(stringTag.getAsString());
        }
        if (tag instanceof NumericTag) {
            if (hasType) {
                if (tag instanceof IntTag) {
                    return new JsonPrimitive(((IntTag) tag).getAsInt() + "I");
                }
                if (tag instanceof ByteTag) {
                    return new JsonPrimitive(((ByteTag) tag).getAsByte() + "B");
                }
                if (tag instanceof ShortTag) {
                    return new JsonPrimitive(((ShortTag) tag).getAsShort() + "S");
                }
                if (tag instanceof LongTag) {
                    return new JsonPrimitive(((LongTag) tag).getAsLong() + "L");
                }
                if (tag instanceof FloatTag) {
                    return new JsonPrimitive(((FloatTag) tag).getAsFloat() + "F");
                }
                if (tag instanceof DoubleTag) {
                    return new JsonPrimitive(((DoubleTag) tag).getAsDouble() + "D");
                }
            }
            return new JsonPrimitive(((NumericTag) tag).getAsDouble());
        }
        if (tag instanceof CollectionTag) {
            if (tag instanceof ListTag) {
                ListTag listTag = ((ListTag) tag);
                JsonArray arrayList = new JsonArray();
                for (Tag tag1 : listTag) {
                    arrayList.add(toJson(tag1, hasType));
                }
                return arrayList;
            }
            JsonObject jsonObject = new JsonObject();
            if (tag instanceof ByteArrayTag) {
                jsonObject.add(LIST_TAG, new JsonPrimitive("B"));
                JsonArray jsonArray = new JsonArray();
                for (ByteTag byteNBT : ((ByteArrayTag) tag)) {
                    jsonArray.add(new JsonPrimitive(byteNBT.getAsByte()));
                }
                jsonObject.add(LIST, jsonArray);
            }
            if (tag instanceof IntArrayTag) {
                jsonObject.add(LIST_TAG, new JsonPrimitive("I"));
                JsonArray jsonArray = new JsonArray();
                for (IntTag intNBT : ((IntArrayTag) tag)) {
                    jsonArray.add(new JsonPrimitive(intNBT.getAsInt()));
                }
                jsonObject.add(LIST, jsonArray);
            }
            if (tag instanceof LongArrayTag) {
                jsonObject.add(LIST_TAG, new JsonPrimitive("D"));
                JsonArray jsonArray = new JsonArray();
                for (LongTag longNBT : ((LongArrayTag) tag)) {
                    jsonArray.add(new JsonPrimitive(longNBT.getAsLong()));
                }
                jsonObject.add(LIST, jsonArray);
            }
            return jsonObject;
        }
        if (tag instanceof CompoundTag) {
            CompoundTag compoundTag = (CompoundTag) tag;
            JsonObject jsonObject = new JsonObject();
            for (String allKey : compoundTag.getAllKeys()) {
                jsonObject.add(allKey, toJson(compoundTag.get(allKey), hasType));
            }
            return jsonObject;
        }
        return new JsonObject();
    }
}
