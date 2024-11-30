package com.example.dacn.Model;

import java.text.Normalizer;
import java.util.regex.Pattern;

public class StringUtils {
    public static String removeDiacritics(String text) {
        String normalized = Normalizer.normalize(text, Normalizer.Form.NFD);
        Pattern pattern = Pattern.compile("\\p{InCombiningDiacriticalMarks}+");
        return pattern.matcher(normalized).replaceAll("");
    }
}