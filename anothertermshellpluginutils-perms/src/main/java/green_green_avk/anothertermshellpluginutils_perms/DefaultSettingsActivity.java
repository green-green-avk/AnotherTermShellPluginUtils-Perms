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
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
    }
}
