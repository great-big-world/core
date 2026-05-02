package dev.creoii.greatbigworld.util;

import java.util.Arrays;
import java.util.stream.Collectors;

public final class StringUtils {
    public static String toTitleCase(String input, String delimiter) {
        return Arrays.stream(input.split(delimiter)).map(word -> word.isEmpty() ? word : Character.toUpperCase(word.charAt(0)) + word.substring(1).toLowerCase()).collect(Collectors.joining(delimiter));
    }
}
