package com.crescentflare.jsoninflator.utility;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Json inflator utility: the result of inflating nested items
 * Contains information about the objects that were added, removed or recycled
 */
@SuppressWarnings({"unused", "WeakerAccess"})
public class InflatorNestedResult
{
    // --
    // Members
    // --

    private List<Object> internalItems = new ArrayList<>();
    private List<Object> internalRemovedItems = new ArrayList<>();
    private List<Boolean> itemRecycled = new ArrayList<>();
    private List<Map<String, Object>> itemAttributes = new ArrayList<>();


    // --
    // Access result
    // --

    @NotNull
    public List<Object> getItems()
    {
        return internalItems;
    }

    @NotNull
    public List<Object> getRemovedItems()
    {
        return internalRemovedItems;
    }

    public boolean isRecycled(@Nullable Object item)
    {
        return isRecycled(internalItems.indexOf(item));
    }

    public boolean isRecycled(int index)
    {
        if (index >= 0 && index < internalItems.size() && index < itemRecycled.size())
        {
            return itemRecycled.get(index);
        }
        return false;
    }

    @NotNull
    public Map<String, Object> getAttributes(@Nullable Object item)
    {
        return getAttributes(internalItems.indexOf(item));
    }

    @NotNull
    public Map<String, Object> getAttributes(int index)
    {
        if (index >= 0 && index < internalItems.size() && index < itemAttributes.size())
        {
            return itemAttributes.get(index);
        }
        return new HashMap<>();
    }


    // --
    // Modify items, used internally
    // --

    public void addItem(@NotNull Object item, @NotNull Map<String, Object> attributes, boolean recycled)
    {
        internalItems.add(item);
        itemAttributes.add(attributes);
        itemRecycled.add(recycled);
    }

    public void addRemovedItem(@NotNull Object item)
    {
        internalRemovedItems.add(item);
    }
}
