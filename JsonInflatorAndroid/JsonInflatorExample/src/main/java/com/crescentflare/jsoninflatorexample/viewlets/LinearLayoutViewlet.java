package com.crescentflare.jsoninflatorexample.viewlets;

import android.content.Context;
import android.view.View;
import android.widget.LinearLayout;

import com.crescentflare.jsoninflator.JsonInflatable;
import com.crescentflare.jsoninflator.binder.InflatorBinder;
import com.crescentflare.jsoninflator.utility.InflatorMapUtil;
import com.crescentflare.jsoninflatorexample.ViewletCreator;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

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
            // Add or recycle children
            LinearLayout container = (LinearLayout)object;
            int childCount = container.getChildCount();
            int currentViewChild = 0;
            List<Map<String, Object>> children = ViewletCreator.instance.attributesForNestedInflatableList(attributes.get("children"));
            for (int i = 0; i < children.size(); i++)
            {
                Map<String, Object> child = children.get(i);
                if (currentViewChild < childCount && ViewletCreator.instance.canRecycle(container.getChildAt(currentViewChild), child))
                {
                    ViewletCreator.instance.inflateOn(container.getChildAt(currentViewChild), child, container, binder);
                    ViewViewlet.applyLayoutAttributes(mapUtil, container.getChildAt(currentViewChild), child);
                    if (binder != null)
                    {
                        String refId = mapUtil.optionalString(child, "refId", null);
                        if (refId != null)
                        {
                            binder.onBind(refId, container.getChildAt(currentViewChild));
                        }
                    }
                    currentViewChild++;
                }
                else
                {
                    if (currentViewChild < childCount)
                    {
                        container.removeViewAt(currentViewChild);
                        childCount--;
                    }
                    Object createdObject = ViewletCreator.instance.inflate(container.getContext(), child, container);
                    if (createdObject instanceof View)
                    {
                        View createdView = (View)createdObject;
                        container.addView(createdView, currentViewChild);
                        ViewViewlet.applyLayoutAttributes(mapUtil, createdView, child);
                        if (binder != null)
                        {
                            String refId = mapUtil.optionalString(child, "refId", null);
                            if (refId != null)
                            {
                                binder.onBind(refId, createdView);
                            }
                        }
                        currentViewChild++;
                        childCount++;
                    }
                }
            }
            for (int i = childCount - 1; i >= currentViewChild; i--)
            {
                container.removeViewAt(i);
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
