package green_green_avk.anothertermshellpluginutils_perms;

import android.app.ListFragment;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CompoundButton;

import java.util.Arrays;

/**
 * To be used as a part of a plugin settings activity.
 * <p><b>Note:</b> Deprecated {@link ListFragment} is used purely to avoid application bloating
 * from <em>androidx.appcompat:appcompat</em> usage
 * (it will likely be changed in future though).</p>
 */
public final class PermissionSettingsFragment extends ListFragment {
    @Override
    public View onCreateView(final LayoutInflater inflater, final ViewGroup container,
                             final Bundle savedInstanceState) {
        return inflater.inflate(R.layout.plugin_utils_permissions_settings_content,
                container, false);
    }

    @Override
    public void onViewCreated(final View view, final Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        setEmptyText(getString(R.string.label_none));
        setListAdapter(new BaseAdapter() {
            private String[] pkgNames;
            private boolean[] pkgGranted;

            {
                init();
            }

            private void init() {
                pkgNames = Permissions
                        .getPackageNamesAsSet(view.getContext()).toArray(new String[0]);
                pkgGranted = new boolean[pkgNames.length];
                Arrays.fill(pkgGranted, true);
            }

            @Override
            public void notifyDataSetChanged() {
                init();
                super.notifyDataSetChanged();
            }

            @Override
            public int getCount() {
                return pkgNames.length;
            }

            @Override
            public Object getItem(final int position) {
                return pkgNames[position];
            }

            @Override
            public long getItemId(final int position) {
                return position;
            }

            @Override
            public View getView(final int position, final View convertView,
                                final ViewGroup parent) {
                final View v = convertView == null ? LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.plugin_utils_permission_entry,
                                parent, false)
                        : convertView;
                Utils.setAppEntry(v.findViewById(R.id.app), pkgNames[position]);
                final CompoundButton wGranted = v.findViewById(R.id.granted);
                wGranted.setChecked(pkgGranted[position]);
                wGranted.setOnCheckedChangeListener((_v, isChecked) -> {
                    try {
                        if (isChecked)
                            Permissions.grant(_v.getContext(),
                                    pkgNames[position]);
                        else
                            Permissions.revoke(_v.getContext(),
                                    pkgNames[position]);
                    } catch (final PackageManager.NameNotFoundException ignored) {
                        _v.setChecked(false);
                        pkgGranted[position] = false;
                        return;
                    }
                    pkgGranted[position] = isChecked;
                });
                return v;
            }
        });
    }

    @Override
    public void onStart() {
        super.onStart();
        ((BaseAdapter) getListAdapter()).notifyDataSetChanged();
    }
}
