package green_green_avk.anothertermshellpluginutils_perms;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.os.Binder;

import androidx.annotation.CheckResult;
import androidx.annotation.NonNull;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import green_green_avk.anothertermshellpluginutils.Auth;

@SuppressWarnings("WeakerAccess,unused")
public final class Permissions {
    private Permissions() {
    }

    private static final String JAVA_PKG_NAME =
            Permissions.class.getName().replaceFirst("\\..*?$", "");

    private static final String SP_NAME = JAVA_PKG_NAME + ".perms";

    @SuppressLint("PackageManagerGetSignatures")
    @NonNull
    private static Set<String> getSignaturesStr(@NonNull final Context context,
                                                @NonNull final String packageName)
            throws PackageManager.NameNotFoundException {
        final PackageManager pm = context.getPackageManager();
        final PackageInfo info =
                pm.getPackageInfo(packageName, PackageManager.GET_SIGNATURES);
        if (info.signatures == null)
            return Collections.emptySet();
        final Set<String> r = new HashSet<>();
        for (final Signature sign : info.signatures)
            r.add(Auth.getFingerprint(sign));
        return r;
    }

    /**
     * Verifies a caller in the context of a current {@link Binder} transaction
     * by {@link Binder#getCallingUid()}.
     * All the packages with the specified UID must match.
     *
     * @param context Application context.
     * @return {@code true} on success.
     */
    @CheckResult
    public static boolean verifyByBinder(@NonNull final Context context) {
        final PackageManager pm = context.getPackageManager();
        final String[] pp = pm.getPackagesForUid(Binder.getCallingUid());
        if (pp == null || pp.length <= 0)
            return false;
        for (final String p : pp)
            try {
                if (!verify(context, p))
                    return false;
            } catch (final PackageManager.NameNotFoundException e) {
                return false;
            }
        return true;
    }

    /**
     * Verifies an application by its package name.
     * The whole signature set must match.
     *
     * @param context     Application context.
     * @param packageName Client application package name.
     * @return {@code true} on success.
     * @throws PackageManager.NameNotFoundException If the package was not found.
     */
    @CheckResult
    public static boolean verify(@NonNull final Context context, @NonNull final String packageName)
            throws PackageManager.NameNotFoundException {
        final Set<String> pss = getSignaturesStr(context, packageName);
        if (pss.isEmpty())
            return false;
        final SharedPreferences sp =
                context.getSharedPreferences(SP_NAME, Context.MODE_PRIVATE);
        final Set<String> signs =
                sp.getStringSet(packageName, Collections.emptySet());
        return pss.equals(signs);
    }

    /**
     * Trusts an application by its package name.
     *
     * @param context     Application context.
     * @param packageName Client application package name.
     * @throws PackageManager.NameNotFoundException If the package was not found.
     */
    public static void grant(@NonNull final Context context, @NonNull final String packageName)
            throws PackageManager.NameNotFoundException {
        final Set<String> pss = getSignaturesStr(context, packageName);
        if (pss.isEmpty())
            return;
        final SharedPreferences sp =
                context.getSharedPreferences(SP_NAME, Context.MODE_PRIVATE);
        final SharedPreferences.Editor ed = sp.edit();
        ed.putStringSet(packageName, pss);
        ed.apply();
    }

    /**
     * Revokes a trusted application by its package name.
     *
     * @param context     Application context.
     * @param packageName Client application package name.
     */
    public static void revoke(@NonNull final Context context, @NonNull final String packageName) {
        final SharedPreferences sp =
                context.getSharedPreferences(SP_NAME, Context.MODE_PRIVATE);
        final SharedPreferences.Editor ed = sp.edit();
        ed.remove(packageName);
        ed.apply();
    }

    static Set<String> getPackageNamesAsSet(@NonNull final Context context) {
        final SharedPreferences sp =
                context.getSharedPreferences(SP_NAME, Context.MODE_PRIVATE);
        return sp.getAll().keySet();
    }
}
