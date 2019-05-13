package com.crescentflare.jsoninflatorexample.viewlets;

import android.content.Context;
import android.view.View;
import android.widget.LinearLayout;

import com.crescentflare.jsoninflator.JsonInflatable;
import com.crescentflare.jsoninflator.binder.InflatorBinder;
import com.crescentflare.jsoninflator.utility.InflatorMapUtil;
import com.crescentflare.jsoninflator.utility.InflatorNestedResult;
import com.crescentflare.jsoninflatorexample.ViewletCreator;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Container viewlet: linear layout
 * Creation of a linear layout through parsed JSON
 */
public class LinearLayoutViewlet implements JsonInflatable
{
    @NotNull
    @Override
    public View create(@NotNull Context context)
    {
        return new LinearLayout(context);
    }

    @Override
    public boolean update(InflatorMapUtil mapUtil, @NotNull Object object, @NotNull Map<String, Object> attributes, @Nullable Object parent, @Nullable InflatorBinder binder)
    {
        if (object instanceof LinearLayout)
        {
            // Collect child views from container
            LinearLayout container = (LinearLayout)object;
            List<Object> allChildren = new ArrayList<>();
            int childCount = container.getChildCount();
            for (int i = 0; i < childCount; i++)
            {
                allChildren.add(container.getChildAt(i));
            }

            // Inflate
            List<Map<String, Object>> newChildren = ViewletCreator.instance.attributesForNestedInflatableList(attributes.get("children"));
            InflatorNestedResult result = ViewletCreator.instance.inflateNestedItemList(container.getContext(), allChildren, newChildren, true, container, binder);

            // Remove views that could not be recycled
            for (Object removeItem : result.getRemovedItems())
            {
                if (removeItem instanceof View)
                {
                    container.removeView((View)removeItem);
                }
            }

            // Add or update views that are new or could be recycled
            for (int i = 0; i < result.getItems().size(); i++)
            {
                Object item = result.getItems().get(i);
                if (item instanceof View)
                {
                    if (!result.isRecycled(i))
                    {
                        container.addView((View)item, i);
                    }
                    ViewViewlet.applyLayoutAttributes(mapUtil, (View)item, result.getAttributes(i));
                    if (binder != null)
                    {
                        String refId = mapUtil.optionalString(result.getAttributes(i), "refId", null);
                        if (refId != null)
                        {
                            binder.onBind(refId, item);
                        }
                    }
                }
            }

            // Standard view attributes
            ViewViewlet.applyDefaultAttributes(mapUtil, container, attributes);
            return true;
        }
        return false;
    }

    @Override
    public boolean canRecycle(InflatorMapUtil mapUtil, @NotNull Object object, @NotNull Map<String, Object> attributes)
    {
        return object instanceof LinearLayout;
    }
}
