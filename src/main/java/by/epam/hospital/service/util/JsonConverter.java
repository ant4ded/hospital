package by.epam.hospital.service.util;

import com.google.gson.Gson;

public final class JsonConverter {
    private static final String QUOTER_REPLACE_CHARACTER = "~";
    private static final String QUOTER = "\"";
    private static final Gson gson = new Gson();

    private JsonConverter() {
    }

    public static String toJson(Object object) {
        return gson.toJson(object);
    }

    public static <T> T fromJson(String json, Class<T> type) {
        return gson.fromJson(json, type);
    }

    public static String makeJsonValidForHtml(String json) {
        return json.replace(QUOTER, QUOTER_REPLACE_CHARACTER);
    }

    public static String recoverJson(String htmlJson) {
        return htmlJson.replace(QUOTER_REPLACE_CHARACTER, QUOTER);
    }
}
