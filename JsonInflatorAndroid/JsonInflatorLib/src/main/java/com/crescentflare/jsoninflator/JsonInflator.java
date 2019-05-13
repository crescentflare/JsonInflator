package com.crescentflare.jsoninflator;

import android.content.Context;

import com.crescentflare.jsoninflator.binder.InflatorBinder;
import com.crescentflare.jsoninflator.utility.InflatorColorLookup;
import com.crescentflare.jsoninflator.utility.InflatorDimensionLookup;
import com.crescentflare.jsoninflator.utility.InflatorMapUtil;
import com.crescentflare.jsoninflator.utility.InflatorNestedResult;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Json inflator: manages inflatables
 * The main interface to register and inflate new objects
 */
@SuppressWarnings({"unused", "WeakerAccess"})
public class JsonInflator
{
    // --
    // Singleton instance
    // --

    public static JsonInflator instance = new JsonInflator("inflator", "attributeSet");


    // --
    // Members
    // --

    private Map<String, InflatableItem> registeredInflators = new HashMap<>();
    private List<String> mergeSubAttributes = new ArrayList<>();
    private List<String> excludeAttributes = new ArrayList<>();
    private InflatorMapUtil mapUtil = new InflatorMapUtil();
    private InflatorColorLookup colorLookup = null;
    private InflatorDimensionLookup dimensionLookup = null;
    private String inflatableKey;
    private String attributeSetKey;


    // --
    // Initialization
    // --

    public JsonInflator(@NotNull String inflatableKey)
    {
        this(inflatableKey, "attributeSet");
    }

    public JsonInflator(@NotNull String inflatableKey, @NotNull String attributeSetKey)
    {
        this.inflatableKey = inflatableKey;
        this.attributeSetKey = attributeSetKey;
    }


    // --
    // Access conversion properties
    // --

    @NotNull
    public InflatorMapUtil getMapUtil()
    {
        return mapUtil;
    }

    public void setColorLookup(@Nullable InflatorColorLookup colorLookup)
    {
        this.colorLookup = colorLookup;
        mapUtil = new InflatorMapUtil(colorLookup, dimensionLookup);
    }

    public void setDimensionLookup(@Nullable InflatorDimensionLookup dimensionLookup)
    {
        this.dimensionLookup = dimensionLookup;
        mapUtil = new InflatorMapUtil(colorLookup, dimensionLookup);
    }


    // --
    // Attribute inclusion/exclusion
    // --

    public void setMergeSubAttributes(@NotNull List<String> attributeNames)
    {
        mergeSubAttributes = attributeNames;
    }

    public void setExcludeAttributes(@NotNull List<String> attributeNames)
    {
        excludeAttributes = attributeNames;
    }


    // --
    // Inflatable registry
    // --

    public void register(@NotNull String name, @Nullable JsonInflatable inflatable)
    {
        obtainInflator(name).setInflatable(inflatable);
    }

    public void registerAttributeSet(@NotNull String inflatableName, @NotNull String setName, @Nullable Map<String, Object> setAttributes)
    {
        InflatableItem inflator = obtainInflator(inflatableName);
        if (setAttributes != null)
        {
            inflator.getAttributeSets().put(setName, setAttributes);
        }
        else
        {
            inflator.getAttributeSets().remove(setName);
        }
    }

    @NotNull
    public List<String> registeredInflatableNames()
    {
        return new ArrayList<>(registeredInflators.keySet());
    }

    private InflatableItem obtainInflator(@NotNull String name)
    {
        InflatableItem item = registeredInflators.get(name);
        if (item == null)
        {
            item = new InflatableItem();
            registeredInflators.put(name, item);
        }
        return item;
    }


    // --
    // Create and update
    // --

    @Nullable
    public Object inflate(@NotNull Context context, @Nullable Map<String, Object> attributes, @Nullable Object parent)
    {
        return inflate(context, attributes, parent, null);
    }

    @Nullable
    public Object inflate(@NotNull Context context, @Nullable Map<String, Object> attributes, @Nullable Object parent, @Nullable InflatorBinder binder)
    {
        String inflatableName = findInflatableNameInAttributes(attributes);
        if (inflatableName != null)
        {
            JsonInflatable inflatable = findInflatableInAttributes(attributes);
            if (inflatable != null)
            {
                Object object = inflatable.create(context);
                Map<String, Object> mergedAttributes = processAttributes(attributes, attributesForSet(inflatableName, mapUtil.optionalString(attributes, attributeSetKey, null)), mergeSubAttributes, excludeAttributes);
                inflatable.update(mapUtil, object, mergedAttributes, parent, binder);
                return object;
            }
        }
        return null;
    }

    public boolean inflateOn(@NotNull Object object, @Nullable Map<String, Object> attributes, @Nullable Object parent)
    {
        return inflateOn(object, attributes, parent, null);
    }

    public boolean inflateOn(@NotNull Object object, @Nullable Map<String, Object> attributes, @Nullable Object parent, @Nullable InflatorBinder binder)
    {
        String inflatableName = findInflatableNameInAttributes(attributes);
        if (inflatableName != null)
        {
            JsonInflatable inflatable = findInflatableInAttributes(attributes);
            if (inflatable != null)
            {
                Map<String, Object> mergedAttributes = processAttributes(attributes, attributesForSet(inflatableName, mapUtil.optionalString(attributes, attributeSetKey, null)), mergeSubAttributes, excludeAttributes);
                return inflatable.update(mapUtil, object, mergedAttributes, parent, binder);
            }
        }
        return false;
    }

    public boolean canRecycle(@Nullable Object object, @Nullable Map<String, Object> attributes)
    {
        if (object != null && attributes != null)
        {
            JsonInflatable inflatable = findInflatableInAttributes(attributes);
            if (inflatable != null)
            {
                return inflatable.canRecycle(mapUtil, object, attributes);
            }
        }
        return false;
    }

    @Nullable
    public JsonInflatable findInflatableInAttributes(@Nullable Map<String, Object> attributes)
    {
        String inflatorName = mapUtil.optionalString(attributes, inflatableKey, null);
        if (inflatorName != null)
        {
            InflatableItem item = registeredInflators.get(inflatorName);
            if (item != null)
            {
                return item.inflatable;
            }
        }
        return null;
    }

    @Nullable
    public String findInflatableNameInAttributes(@Nullable Map<String, Object> attributes)
    {
        return mapUtil.optionalString(attributes, inflatableKey, null);
    }


    // --
    // Nested inflation utilities
    // --

    @NotNull
    public InflatorNestedResult inflateNestedItem(@NotNull Context context, @NotNull Object currentItem, @Nullable Object newItem, boolean enableRecycling, @Nullable Object parent, @Nullable InflatorBinder binder)
    {
        // Recycle or inflate new item
        InflatorNestedResult result = new InflatorNestedResult();
        Map<String, Object> processedNewItem = attributesForNestedInflatable(newItem);
        Object inflatedItem;
        if (enableRecycling && canRecycle(currentItem, processedNewItem))
        {
            inflateOn(currentItem, processedNewItem, parent, binder);
            inflatedItem = currentItem;
        }
        else
        {
            result.addRemovedItem(currentItem);
            inflatedItem = inflate(context, processedNewItem, parent, binder);
        }

        // Add item to result
        if (inflatedItem != null && processedNewItem != null)
        {
            result.addItem(inflatedItem, processedNewItem, inflatedItem == currentItem);
        }
        return result;
    }

    @NotNull
    public InflatorNestedResult inflateNestedItemList(@NotNull Context context, List<Object> currentItems, @Nullable Object newItems, boolean enableRecycling, @Nullable Object parent, @Nullable InflatorBinder binder)
    {
        InflatorNestedResult result = new InflatorNestedResult();
        List<Map<String, Object>> processedNewItems = attributesForNestedInflatableList(newItems);
        if (enableRecycling)
        {
            // Add or recycle items
            int recycleIndex = 0;
            for (Map<String, Object> newItem : processedNewItems)
            {
                // Search for a current item to recycle (use an index to maintain ordering)
                boolean recycled = false;
                Object inflatedItem = null;
                for (int index = recycleIndex; index < currentItems.size(); index++)
                {
                    if (canRecycle(currentItems.get(index), newItem))
                    {
                        for (int removeIndex = recycleIndex; removeIndex < index; removeIndex++)
                        {
                            result.addRemovedItem(currentItems.get(removeIndex));
                        }
                        recycleIndex = index + 1;
                        inflatedItem = currentItems.get(index);
                        inflateOn(currentItems.get(index), newItem, parent, binder);
                        recycled = true;
                        break;
                    }
                }

                // If no candidate was found, create a new item
                if (!recycled)
                {
                    inflatedItem = inflate(context, newItem, parent, binder);
                }
                if (inflatedItem != null)
                {
                    result.addItem(inflatedItem, newItem, recycled);
                }
            }

            // Set remaining items for removal
            for (int index = recycleIndex; index < currentItems.size(); index++)
            {
                result.addRemovedItem(currentItems.get(index));
            }
        }
        else
        {
            // First mark all current items as removed
            for (Object item : currentItems)
            {
                result.addRemovedItem(item);
            }

            // Create new items
            for (Map<String, Object> newItem : processedNewItems)
            {
                Object inflatedItem = inflate(context, newItem, parent, binder);
                if (inflatedItem != null)
                {
                    result.addItem(inflatedItem, newItem, false);
                }
            }
        }
        return result;
    }

    @Nullable
    public Map<String, Object> attributesForNestedInflatable(@Nullable Object nestedInflatableItem)
    {
        Map<String, Object> attributes = mapUtil.asStringObjectMap(nestedInflatableItem);
        if (attributes != null)
        {
            String inflatableName = findInflatableNameInAttributes(attributes);
            if (inflatableName != null)
            {
                return processAttributes(attributes, attributesForSet(inflatableName, mapUtil.optionalString(attributes, attributeSetKey, null)), mergeSubAttributes, excludeAttributes);
            }
        }
        return null;
    }

    public List<Map<String, Object>> attributesForNestedInflatableList(@Nullable Object nestedInflatableItemList)
    {
        List<Map<String, Object>> inflatableItemList = new ArrayList<>();
        if (nestedInflatableItemList instanceof List<?>)
        {
            List<?> itemList = (List<?>)nestedInflatableItemList;
            for (Object item : itemList)
            {
                Map<String, Object> attributes = mapUtil.asStringObjectMap(item);
                if (attributes != null)
                {
                    String inflatableName = findInflatableNameInAttributes(attributes);
                    if (inflatableName != null)
                    {
                        inflatableItemList.add(processAttributes(attributes, attributesForSet(inflatableName, mapUtil.optionalString(attributes, attributeSetKey, null)), mergeSubAttributes, excludeAttributes));
                    }
                }
            }
        }
        return inflatableItemList;
    }


    // --
    // Attribute processing
    // --

    @Nullable
    private Map<String, Object> attributesForSet(@NotNull String inflatableName, @Nullable String setName)
    {
        InflatableItem inflator = registeredInflators.get(inflatableName);
        if (inflator != null)
        {
            String checkSetName = setName != null ? setName : "";
            if (checkSetName.equals("default"))
            {
                return inflator.getAttributeSets().get("default");
            }
            return mergeAttributes(inflator.getAttributeSets().get(setName), inflator.getAttributeSets().get("default"));
        }
        return null;
    }

    @NotNull
    private Map<String, Object> processAttributes(@Nullable Map<String, Object> givenAttributes, @Nullable Map<String, Object> fallbackAttributes, @NotNull List<String> mergeSubAttributes, @NotNull List<String> excludeAttributes)
    {
        Map<String, Object> result = mergeAttributes(givenAttributes, fallbackAttributes);
        for (String mergeSubAttribute : mergeSubAttributes)
        {
            Map<String, Object> item = mapUtil.asStringObjectMap(result.get(mergeSubAttribute));
            if (item != null)
            {
                result = mergeAttributes(item, result);
            }
        }
        for (String excludeAttribute : excludeAttributes)
        {
            result.remove(excludeAttribute);
        }
        return result;
    }

    @NotNull
    private static Map<String, Object> mergeAttributes(@Nullable Map<String, Object> givenAttributes, @Nullable Map<String, Object> fallbackAttributes)
    {
        // Just return one of the attributes if the other is null
        if (fallbackAttributes == null)
        {
            return givenAttributes != null ? givenAttributes : new HashMap<String, Object>();
        }
        else if (givenAttributes == null)
        {
            return fallbackAttributes;
        }

        // Merge and return without modifying the originals
        Map<String, Object> mergedAttributes = new HashMap<>();
        for (String key : givenAttributes.keySet())
        {
            Object addItem = givenAttributes.get(key);
            if (addItem != null)
            {
                mergedAttributes.put(key, addItem);
            }
        }
        for (String key : fallbackAttributes.keySet())
        {
            if (!mergedAttributes.containsKey(key))
            {
                Object addItem = fallbackAttributes.get(key);
                if (addItem != null)
                {
                    mergedAttributes.put(key, addItem);
                }
            }
        }
        return mergedAttributes;
    }


    // --
    // Helper class to group inflatable and its attribute sets
    // --

    private class InflatableItem
    {
        private JsonInflatable inflatable;
        private Map<String, Map<String, Object>> attributeSets = new HashMap<>();

        public JsonInflatable getInflatable()
        {
            return inflatable;
        }

        public void setInflatable(JsonInflatable inflatable)
        {
            this.inflatable = inflatable;
        }

        public Map<String, Map<String, Object>> getAttributeSets()
        {
            return attributeSets;
        }

        public void setAttributeSets(Map<String, Map<String, Object>> attributeSets)
        {
            this.attributeSets = attributeSets;
        }
    }
}
