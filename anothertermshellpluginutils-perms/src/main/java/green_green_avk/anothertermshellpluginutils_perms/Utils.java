package green_green_avk.anothertermshellpluginutils_perms;

import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;

final class Utils {
    private Utils() {
    }

    static void setAppEntry(@NonNull final View root, @NonNull final String packageName) {
        final ImageView wIcon = root.findViewById(R.id.app_icon);
        final TextView wTitle = root.findViewById(R.id.app_title);
        final TextView wPkg = root.findViewById(R.id.app_package);
        wPkg.setText(packageName);
        final Context ctx = root.getContext();
        final PackageManager pm = ctx.getPackageManager();
        final ApplicationInfo app;
        try {
            app = pm.getApplicationInfo(packageName, 0);
        } catch (final PackageManager.NameNotFoundException e) {
            wIcon.setImageResource(android.R.mipmap.sym_def_app_icon);
            wTitle.setText(android.R.string.unknownName);
            return;
        }
        wIcon.setImageDrawable(pm.getApplicationIcon(app));
        wTitle.setText(pm.getApplicationLabel(app));
    }
}
