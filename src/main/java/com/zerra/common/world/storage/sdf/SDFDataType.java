package com.zerra.common.world.storage.sdf;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

public class SDFDataType {

    private static final Map<Character, Function<String, ?>> charToFunction;
    private static final Map<Class, Character> typeToChar;

    static {
        charToFunction = new HashMap<>();
        typeToChar = new HashMap<>();

        put('B', Boolean.class, Boolean::parseBoolean);
        put('b', Byte.class, Byte::parseByte);
        put('s', Short.class, Short::parseShort);
        put('i', Integer.class, Integer::parseInt);
        put('l', Long.class, Long::parseLong);
        put('f', Float.class, Float::parseFloat);
        put('d', Double.class, Double::parseDouble);
        put('S', String.class, s -> s);
        put('D', SimpleDataFormat.class, null);
    }

    private static void put(char c, Class type, Function<String, ?> function) {
        charToFunction.put(c, function);
        typeToChar.put(type, c);
    }

    public static Character getCharFromClass(Class type) {
        return typeToChar.get(type);
    }

    public static Object getInstanceFromString(char character, String value) {
        Function<String, ?> function = charToFunction.get(character);
        return function == null ? null : function.apply(value);
    }


}
