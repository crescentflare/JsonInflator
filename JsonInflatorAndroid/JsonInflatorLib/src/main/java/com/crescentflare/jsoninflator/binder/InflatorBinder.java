package com.crescentflare.jsoninflator.binder;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * Json inflator binder: default interface
 * An interface which can be implemented to do custom inflatable object binding
 */
@SuppressWarnings({"unused", "WeakerAccess"})
public interface InflatorBinder
{
    void onBind(@NotNull String refId, @Nullable Object object);
}
