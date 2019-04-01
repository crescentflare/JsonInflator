package com.crescentflare.jsoninflatorexample;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.SwitchCompat;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.TextView;

import com.crescentflare.jsoninflator.JsonLoader;
import com.crescentflare.jsoninflator.binder.InflatableRef;
import com.crescentflare.jsoninflator.binder.InflatorAnnotationBinder;

import java.util.Map;

/**
 * The main activity shows a small layout example, explanation and buttons to show other layout examples
 */
public class MainActivity extends AppCompatActivity implements LiveJson.LiveJsonListener
{
    // ---
    // View bindings
    // ---

    @InflatableRef("serverAddress")
    private EditText serverAddressField;

    @InflatableRef("serverSwitch")
    private SwitchCompat serverEnabledSwitch;

    @InflatableRef("pollingSwitch")
    private SwitchCompat pollingEnabledSwitch;

    @InflatableRef("playgroundButton")
    private Button playgroundButton;

    @InflatableRef("loadError")
    private TextView loadError;


    // ---
    // Members
    // ---

    private LiveJson liveLayout = null;


    // ---
    // Initialization
    // ---

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        // Configure activity
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setTitle(getString(R.string.main_title));

        // Live loading or file loading
        updateState();
    }


    // ---
    // State handling
    // ---

    @Override
    protected void onPause()
    {
        super.onPause();
        if (liveLayout != null)
        {
            liveLayout.setPollingEnabled(false);
        }
    }

    @Override
    protected void onResume()
    {
        super.onResume();
        if (liveLayout != null)
        {
            liveLayout.setPollingEnabled(Settings.instance.isAutoRefresh());
        }
    }


    // ---
    // Update state based on settings
    // ---

    private void updateState()
    {
        if (Settings.instance.isServerEnabled())
        {
            liveLayout = new LiveJson("layout_main.json");
            liveLayout.setPollingEnabled(Settings.instance.isAutoRefresh());
            liveLayout.setListener(this);
        }
        else
        {
            Map<String, Object> attributes = JsonLoader.instance.loadAttributes(this, R.raw.layout_main);
            if (liveLayout != null)
            {
                liveLayout.setListener(null);
            }
            liveLayout = null;
            if (attributes != null)
            {
                layoutLoaded(attributes);
                if (loadError != null)
                {
                    loadError.setVisibility(View.GONE);
                }
            }
        }
    }


    // ---
    // Inflation
    // ---

    private void layoutLoaded(Map<String, Object> attributes)
    {
        // First remove all listeners
        if (serverAddressField != null)
        {
            serverAddressField.setOnEditorActionListener(null);
        }
        if (serverEnabledSwitch != null)
        {
            serverEnabledSwitch.setOnCheckedChangeListener(null);
        }
        if (pollingEnabledSwitch != null)
        {
            pollingEnabledSwitch.setOnCheckedChangeListener(null);
        }

        // Inflation
        View view = findViewById(R.id.activity_main_container);
        ViewletCreator.instance.inflateOn(view, attributes, null, new InflatorAnnotationBinder(this));

        // Configure server address field
        if (serverAddressField != null)
        {
            serverAddressField.setText(Settings.instance.getServerAddress());
            serverAddressField.setOnEditorActionListener(new TextView.OnEditorActionListener()
            {
                @Override
                public boolean onEditorAction(TextView textView, int actionId, KeyEvent keyEvent)
                {
                    int currentAction = KeyEvent.ACTION_DOWN;
                    if (keyEvent != null)
                    {
                        currentAction = keyEvent.getAction();
                    }
                    if ((actionId == EditorInfo.IME_NULL || actionId == EditorInfo.IME_ACTION_DONE) && currentAction == KeyEvent.ACTION_DOWN)
                    {
                        Settings.instance.setServerAddress(serverAddressField.getText().toString());
                        InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
                        if (imm != null)
                        {
                            imm.hideSoftInputFromWindow(serverAddressField.getWindowToken(), 0);
                        }
                        updateState();
                    }
                    return true;
                }
            });
        }

        // Configure switches
        if (serverEnabledSwitch != null)
        {
            serverEnabledSwitch.setChecked(Settings.instance.isServerEnabled());
            serverEnabledSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener()
            {
                @Override
                public void onCheckedChanged(CompoundButton compoundButton, boolean checked)
                {
                    Settings.instance.setServerEnabled(checked);
                    updateState();
                }
            });
        }
        if (pollingEnabledSwitch != null)
        {
            pollingEnabledSwitch.setChecked(Settings.instance.isAutoRefresh());
            pollingEnabledSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener()
            {
                @Override
                public void onCheckedChanged(CompoundButton compoundButton, boolean checked)
                {
                    Settings.instance.setAutoRefresh(checked);
                    updateState();
                }
            });
        }

        // Button to open the playground activity
        if (playgroundButton != null)
        {
            playgroundButton.setOnClickListener(new View.OnClickListener()
            {
                @Override
                public void onClick(View view)
                {
                    Intent intent = new Intent(MainActivity.this, PlaygroundActivity.class);
                    startActivity(intent);
                }
            });
        }
    }


    // ---
    // Live viewlet handling
    // ---


    @Override
    public void jsonUpdated(LiveJson liveJson)
    {
        layoutLoaded(liveJson.getData());
        if (loadError != null)
        {
            loadError.setVisibility(View.GONE);
        }
    }

    @Override
    public void jsonFailed(LiveJson liveJson)
    {
        boolean errorShown = false;
        if (loadError != null)
        {
            errorShown = loadError.getVisibility() == View.VISIBLE;
        }
        if (!errorShown && Settings.instance.isServerEnabled())
        {
            Map<String, Object> attributes = JsonLoader.instance.loadAttributes(this, R.raw.layout_main);
            if (attributes != null)
            {
                layoutLoaded(attributes);
                if (loadError != null)
                {
                    loadError.setVisibility(View.VISIBLE);
                }
            }
        }
    }
}
