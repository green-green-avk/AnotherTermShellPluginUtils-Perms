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
 */
public final class PermissionSettingsFragment extends ListFragment {
    @Override
    public View onCreateView(final LayoutInflater inflater, final ViewGroup container,
                             final Bundle savedInstanceState) {
        return inflater.inflate(R.layout.content_permissions_settings,
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
                pkgNames =
                        Permissions.getPackageNamesAsSet(view.getContext()).toArray(new String[0]);
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
                        .inflate(R.layout.entry_permission, parent, false)
                        : convertView;
                Utils.setAppEntry(v.findViewById(R.id.app), pkgNames[position]);
                final CompoundButton wGranted = v.findViewById(R.id.granted);
                wGranted.setChecked(pkgGranted[position]);
                wGranted.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(final CompoundButton v,
                                                 final boolean isChecked) {
                        try {
                            if (isChecked) Permissions.grant(v.getContext(), pkgNames[position]);
                            else Permissions.revoke(v.getContext(), pkgNames[position]);
                        } catch (final PackageManager.NameNotFoundException ignored) {
                            v.setChecked(false);
                            pkgGranted[position] = false;
                            return;
                        }
                        pkgGranted[position] = isChecked;
                    }
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
