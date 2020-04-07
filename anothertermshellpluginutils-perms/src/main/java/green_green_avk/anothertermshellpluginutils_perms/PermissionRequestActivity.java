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
        if (pkgName == null) {
            finish();
            return;
        }
        packageName = pkgName;
        setContentView(R.layout.content_permission_request);
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
