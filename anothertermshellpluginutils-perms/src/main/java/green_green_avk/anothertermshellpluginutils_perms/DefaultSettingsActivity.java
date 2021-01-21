package green_green_avk.anothertermshellpluginutils_perms;

import android.app.Activity;
import android.os.Bundle;

import androidx.annotation.Nullable;

/**
 * Trusted applications revocation activity.
 * To be used as a default plugin settings activity if no other settings exist.
 */
public final class DefaultSettingsActivity extends Activity {
    @Override
    protected void onCreate(@Nullable final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.plugin_utils_settings_activity);
    }
}
