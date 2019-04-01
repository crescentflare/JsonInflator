package com.crescentflare.jsoninflator;

import android.content.Context;
import android.util.SparseArray;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.jetbrains.annotations.Nullable;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.lang.reflect.Type;
import java.util.Map;

/**
 * Json inflator: loading json properties
 * Load JSON and parses it to a map with attributes (to be used for inflation)
 */
@SuppressWarnings({"unused", "WeakerAccess"})
public class JsonLoader
{
    // --
    // Singleton instance
    // --

    public static JsonLoader instance = new JsonLoader();


    // --
    // Cached loaded json properties
    // --

    private SparseArray<Map<String, Object>> loadedAttributes = new SparseArray<>();


    // --
    // Loading
    // --

    @Nullable
    public Map<String, Object> loadAttributes(Context context, int rawResourceId)
    {
        // Obtain cached item if possible
        Map<String, Object> item = loadedAttributes.get(rawResourceId);
        if (item != null)
        {
            return item;
        }

        // Not cached, try to load it
        try
        {
            InputStream stream = context.getResources().openRawResource(rawResourceId);
            Map<String, Object> loadedItem = null;
            String jsonString = readFromInputStream(stream);
            if (jsonString != null)
            {
                Type type = new TypeToken<Map<String, Object>>(){}.getType();
                loadedItem = new Gson().fromJson(jsonString, type);
                if (loadedItem != null)
                {
                    loadedAttributes.put(rawResourceId, loadedItem);
                }
            }
            stream.close();
            return loadedItem;
        }
        catch (IOException ignored)
        {
        }
        return null;
    }


    // --
    // Helper
    // --

    @SuppressWarnings("CharsetObjectCanBeUsed")
    private static String readFromInputStream(InputStream stream)
    {
        final int bufferSize = 1024;
        final char[] buffer = new char[bufferSize];
        final StringBuilder out = new StringBuilder();
        String result = null;
        try
        {
            Reader in = new InputStreamReader(stream, "UTF-8");
            for ( ; ; )
            {
                int rsz = in.read(buffer, 0, buffer.length);
                if (rsz < 0)
                {
                    break;
                }
                out.append(buffer, 0, rsz);
            }
            result = out.toString();
            stream.close();
        }
        catch (Exception ignored)
        {
        }
        return result;
    }
}
