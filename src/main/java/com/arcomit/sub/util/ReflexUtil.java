package com.arcomit.sub.util;

import java.lang.reflect.*;
import java.util.ArrayList;
import java.util.List;

/**
 * @author til
 */
public class ReflexUtil {

    /***
     * 判断一个类是有效的
     */
    public static boolean isEffective(Class<?> clazz) {
        if (Modifier.isAbstract(clazz.getModifiers())) {
            return false;
        }
        if (clazz.getAnnotation(Deprecated.class) != null) {
            return false;
        }
        return true;
    }

    public static boolean isEffective(Field field) {
        if (Modifier.isAbstract(field.getModifiers())) {
            return false;
        }
        if (field.getAnnotation(Deprecated.class) != null) {
            return false;
        }
        return true;
    }

    public static boolean isEffective(Method method) {
        if (Modifier.isAbstract(method.getModifiers())) {
            return false;
        }
        if (method.getAnnotation(Deprecated.class) != null) {
            return false;
        }
        return true;
    }

    public static List<Field> getAllFields(Class<?> clazz, boolean isStatic) {
        List<Field> fieldList = new ArrayList<>();
        while (clazz != null) {
            for (Field declaredField : clazz.getDeclaredFields()) {
                if (!isEffective(declaredField)) {
                    continue;
                }
                if (Modifier.isStatic(declaredField.getModifiers()) != isStatic) {
                    continue;
                }
                fieldList.add(declaredField);
            }
            clazz = clazz.getSuperclass();
        }
        return fieldList;
    }

    /***
     * 获取所有继承类和接口
     */
    public static List<Class<?>> getAllExtends(Class<?> c) {
        return getAllExtends(c, new ArrayList<>(8));
    }

    public static List<Class<?>> getAllExtends(Class<?> c, List<Class<?>> list) {
        if (!list.contains(c)) {
            list.add(c);
        }
        for (Class<?> aClass : c.getInterfaces()) {
            getAllExtends(aClass, list);
        }
        Class<?> basics = c.getSuperclass();
        if (basics != null) {
            getAllExtends(basics, list);
        }
        return list;
    }

    public static Class<?> asClass(Type type) {
        if (type instanceof Class<?>) {
            Class<?> clazz = (Class<?>) type;
            return clazz;
        } else if (type instanceof ParameterizedType) {
            ParameterizedType parameterizedType = (ParameterizedType) type;
            return (Class<?>) parameterizedType.getRawType();
        } else if (type instanceof TypeVariable) {
            TypeVariable<?> typeVariable = (TypeVariable<?>) type;
            return (Class<?>) typeVariable.getGenericDeclaration();
        }
        return null;
    }


    public static Field getFieldModifiers() throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        Method getDeclaredFields0 = Class.class.getDeclaredMethod("getDeclaredFields0", boolean.class);
        getDeclaredFields0.setAccessible(true);
        Field[] fields = (Field[]) getDeclaredFields0.invoke(Field.class, false);
        Field modifiers = null;
        for (Field each : fields) {
            if ("modifiers".equals(each.getName())) {
                modifiers = each;
            }
        }
        return modifiers;
    }
}





