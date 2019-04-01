package com.crescentflare.jsoninflatorexample.viewlets;

import android.content.Context;
import android.graphics.Typeface;
import android.text.InputType;
import android.util.TypedValue;
import android.view.View;
import android.widget.EditText;

import com.crescentflare.jsoninflator.JsonInflatable;
import com.crescentflare.jsoninflator.binder.InflatorBinder;
import com.crescentflare.jsoninflator.utility.InflatorMapUtil;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Map;

/**
 * Basic viewlet: editable text
 * Creation of an editable text view through parsed JSON
 */
public class EditTextViewlet implements JsonInflatable
{
    @NotNull
    @Override
    public View create(@NotNull Context context)
    {
        return new EditText(context);
    }

    @Override
    public boolean update(InflatorMapUtil mapUtil, @NotNull Object object, @NotNull Map<String, Object> attributes, @Nullable Object parent, @Nullable InflatorBinder binder)
    {
        if (object instanceof EditText)
        {
            // Pre-filled text and hint
            EditText editText = (EditText)object;
            editText.setText(ViewViewlet.translatedText(editText.getContext(), mapUtil.optionalString(attributes, "text", "")));
            editText.setHint(ViewViewlet.translatedText(editText.getContext(), mapUtil.optionalString(attributes, "hint", "")));

            // Set input mode
            String inputType = mapUtil.optionalString(attributes, "inputType", "");
            if (inputType.equals("email"))
            {
                editText.setInputType(InputType.TYPE_TEXT_VARIATION_EMAIL_ADDRESS);
            }
            else if (inputType.equals("url"))
            {
                editText.setInputType(InputType.TYPE_TEXT_VARIATION_URI);
            }
            else
            {
                editText.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_FLAG_MULTI_LINE);
            }

            // Text style
            String typeface = mapUtil.optionalString(attributes, "typeface", "");
            int defaultSize = (int)(editText.getContext().getResources().getDisplayMetrics().scaledDensity * 17);
            editText.setTextSize(TypedValue.COMPLEX_UNIT_PX, mapUtil.optionalDimension(attributes, "textSize", defaultSize));
            if (typeface.equals("bold"))
            {
                editText.setTypeface(null, Typeface.BOLD);
            }
            else if (typeface.equals("italics"))
            {
                editText.setTypeface(null, Typeface.ITALIC);
            }
            else
            {
                editText.setTypeface(Typeface.DEFAULT);
            }
            editText.setTextColor(mapUtil.optionalColor(attributes, "textColor", 0xFF101010));

            // Standard view attributes
            ViewViewlet.applyDefaultAttributes(mapUtil, editText, attributes);
            return true;
        }
        return false;
    }

    @Override
    public boolean canRecycle(InflatorMapUtil mapUtil, @NotNull Object object, @NotNull Map<String, Object> attributes)
    {
        return object instanceof EditText;
    }
}
