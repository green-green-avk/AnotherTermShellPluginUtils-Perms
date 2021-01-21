package green_green_avk.anothertermshellpluginutils_perms;

import android.app.ActionBar;
import android.app.Activity;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

/**
 * Not supposed to be used directly.
 * It's in this module manifest.
 * To be accessed via an explicit intent with the URI in the form:
 * <p><code>package:&lt;package_to_be_trusted_by_this_plugin&gt;</code></p>
 * with the suffix <code>#revoke</code> to revoke.
 */
public final class PermissionRequestActivity extends Activity {
    private String packageName = null;

    @Override
    protected void onCreate(@Nullable final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        final ActionBar ab = getActionBar();
        if (ab != null) ab.hide();
        final Uri uri = getIntent().getData();
        if (uri == null || !"package".equals(uri.getScheme())) {
            finish();
            return;
        }
        final String pkgName = uri.getSchemeSpecificPart();
        try {
            if (pkgName == null || uri.getFragment() != null
                    || Permissions.verify(this, pkgName)) {
                finish();
                return;
            }
        } catch (final PackageManager.NameNotFoundException e) {
            finish();
            return;
        }
        packageName = pkgName;
        setContentView(R.layout.plugin_utils_permission_request_content);
        final ImageView wIcon = findViewById(R.id.icon);
        final TextView wTitle = findViewById(R.id.title);
        wIcon.setImageResource(getApplicationInfo().icon);
        wTitle.setText(getApplicationInfo().labelRes);
        Utils.setAppEntry(findViewById(R.id.app), packageName);
    }

    public void onOk(@NonNull final View v) {
        try {
            Permissions.grant(this, packageName);
        } catch (final PackageManager.NameNotFoundException ignored) {
        }
        finish();
    }

    public void onCancel(@NonNull final View v) {
        finish();
    }
}
