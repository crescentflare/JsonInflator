package com.crescentflare.jsoninflator.binder;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.HashMap;
import java.util.Map;

/**
 * Json inflator binder: object map
 * An inflator binder implementation which contains a map of all referenced objects
 */
@SuppressWarnings({"unused", "WeakerAccess"})
public class InflatorMapBinder implements InflatorBinder
{
    private Map<String, Object> boundObjects = new HashMap<>();

    @Override
    public void onBind(@NotNull String refId, @Nullable Object object)
    {
        if (object != null)
        {
            boundObjects.put(refId, object);
        }
    }

    @Nullable
    public Object findByReference(@NotNull String refId)
    {
        return boundObjects.get(refId);
    }
}
