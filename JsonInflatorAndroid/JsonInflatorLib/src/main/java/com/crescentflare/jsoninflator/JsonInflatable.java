package com.crescentflare.jsoninflator;

import android.content.Context;

import com.crescentflare.jsoninflator.binder.InflatorBinder;
import com.crescentflare.jsoninflator.utility.InflatorMapUtil;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Map;

/**
 * Json inflator: an inflatable object
 * A generic way to create and update an object (with optional recycling)
 * Add an implementation of this to the objects or classes that can be inflated through JSON
 */
public interface JsonInflatable
{
    @NotNull
    Object create(@NotNull Context context);

    boolean update(InflatorMapUtil convUtil, @NotNull Object object, @NotNull Map<String, Object> attributes, @Nullable Object parent, @Nullable InflatorBinder binder);
    boolean canRecycle(InflatorMapUtil convUtil, @NotNull Object object, @NotNull Map<String, Object> attributes);
}
