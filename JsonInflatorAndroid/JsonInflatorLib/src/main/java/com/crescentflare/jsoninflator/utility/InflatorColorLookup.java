package com.crescentflare.jsoninflator.utility;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * Json inflator utility: interface for color lookup
 * Integrates with InflatorMapUtil to look up custom color references
 */
@SuppressWarnings({"unused", "WeakerAccess"})
public interface InflatorColorLookup
{
    @Nullable
    Integer getColor(@NotNull String refId);
}
