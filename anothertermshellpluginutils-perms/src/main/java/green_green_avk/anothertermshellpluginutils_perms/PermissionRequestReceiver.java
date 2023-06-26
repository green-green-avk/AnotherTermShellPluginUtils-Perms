package green_green_avk.anothertermshellpluginutils_perms;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;

import androidx.annotation.DeprecatedSinceApi;
import androidx.annotation.NonNull;

/**
 * Asks the user to trust an application by this plugin.
 * <p>
 * It is in this module's manifest and
 * supposed to be accessed via an explicit intent with the URI in the form:
 * <p>{@code package:{app_id_to_be_trusted_by_this_plugin}}</p>
 * with the suffix {@code #revoke} to revoke.
 * <p><b>Note:</b> Consider calling {@link PermissionRequestActivity} if target API &gt;= 29.
 * (It was used mostly to prevent a screen blinking artifact
 * when the permission is already granted and the plugin process is not already started.)</p>
 * Don't forget to remove it from the final manifest if unused
 * (<a target="_blank" href="https://developer.android.com/studio/build/manifest-merge#merge_rule_markers">
 * {@code tools:node="remove"}
 * </a>).
 */
@DeprecatedSinceApi(api = 29, message =
        "https://developer.android.com/about/versions/12/behavior-changes-12#notification-trampolines")
public final class PermissionRequestReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(final Context context, @NonNull final Intent intent) {
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
        context.startActivity(intent
                .setClass(context, PermissionRequestActivity.class)
                .addFlags(Intent.FLAG_ACTIVITY_NEW_TASK
                        | Intent.FLAG_ACTIVITY_EXCLUDE_FROM_RECENTS
                        | Intent.FLAG_ACTIVITY_NO_HISTORY));
    }
}
