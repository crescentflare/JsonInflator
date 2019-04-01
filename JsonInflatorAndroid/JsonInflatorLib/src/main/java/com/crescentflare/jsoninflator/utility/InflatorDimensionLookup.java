package com.crescentflare.jsoninflator.utility;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * Json inflator utility: interface for dimension lookup
 * Integrates with InflatorMapUtil to look up custom dimension references
 */
@SuppressWarnings({"unused", "WeakerAccess"})
public interface InflatorDimensionLookup
{
    @Nullable
    Integer getDimension(@NotNull String refId);
}
