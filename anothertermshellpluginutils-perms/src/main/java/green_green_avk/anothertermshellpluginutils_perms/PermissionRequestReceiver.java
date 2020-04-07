package green_green_avk.anothertermshellpluginutils_perms;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;

/**
 * Not supposed to be used directly.
 * It's in this module manifest.
 * To be accessed via an explicit intent with an URI in the form:
 * <code>package:&lt;package_to_set_trusted_by_this_plugin&gt;</code>.
 */
public final class PermissionRequestReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(final Context context, final Intent intent) {
        final Uri uri = intent.getData();
        if (uri == null || !"package".equals(uri.getScheme())) {
            return;
        }
        final String pkgName = uri.getSchemeSpecificPart();
        try {
            if (pkgName == null || uri.getFragment() != null
                    || Permissions.verify(context, pkgName)) {
                return;
            }
        } catch (final PackageManager.NameNotFoundException e) {
            return;
        }
        context.startActivity(intent.setClass(context, PermissionRequestActivity.class)
                .addFlags(Intent.FLAG_ACTIVITY_NEW_TASK
                        | Intent.FLAG_ACTIVITY_EXCLUDE_FROM_RECENTS
                        | Intent.FLAG_ACTIVITY_NO_HISTORY));
    }
}
