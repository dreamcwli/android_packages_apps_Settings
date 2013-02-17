package com.android.settings;

import android.app.Activity;
import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.os.SystemProperties;
import android.preference.CheckBoxPreference;
import android.preference.Preference;
import android.preference.PreferenceFragment;
import android.preference.PreferenceScreen;

public class UserInterfaceSettings extends PreferenceFragment {
    private static final String KEY_SYSTEM_BAR = "use_system_bar";
    private static final String KEY_MOVE_NAVIGATION_BAR = "move_navigation_bar";

    private static final String PROPERTY_SYSTEM_BAR = "persist.sys.ui.sysbar";
    private static final String PROPERTY_MOVE_NAVIGATION_BAR = "persist.sys.ui.navbar.move";

    private static final String ACTION_SYSTEMUI_STOPPED = "com.android.systemui.SYSTEMUI_STOPPED";

    private CheckBoxPreference mUseSystemBar;
    private CheckBoxPreference mMoveNavigationBar;

    @Override
    public void onCreate(Bundle icicle) {
        super.onCreate(icicle);

        addPreferencesFromResource(R.xml.user_interface_settings);

        final Resources resources = getActivity().getResources();
        mUseSystemBar = (CheckBoxPreference)findPreference(KEY_SYSTEM_BAR);
        if (!resources.getBoolean(R.bool.config_show_system_bar_setting)) {
            getPreferenceScreen().removePreference(mUseSystemBar);
        }

        mMoveNavigationBar = (CheckBoxPreference)findPreference(KEY_MOVE_NAVIGATION_BAR);
        if (!resources.getBoolean(R.bool.config_show_navigation_bar_settings)) {
            getPreferenceScreen().removePreference(mMoveNavigationBar);
        }

        updateAllOptions();
    }

    @Override
    public boolean onPreferenceTreeClick(PreferenceScreen preferenceScreen, Preference preference) {
        if (preference == mUseSystemBar) {
            writeUseSystemBarOptions();
        } else if (preference == mMoveNavigationBar) {
            writeMoveNavigationBarOptions();
        }
        updateAllOptions();

        return false;
    }

    private void updateAllOptions() {
        updateUseSystemBarOptions();
        updateMoveNavigationBarOptions();
    }

    private void updateUseSystemBarOptions() {
        mUseSystemBar.setChecked(SystemProperties.getBoolean(PROPERTY_SYSTEM_BAR, false));
    }

    private void writeUseSystemBarOptions() {
        SystemProperties.set(PROPERTY_SYSTEM_BAR, mUseSystemBar.isChecked() ? "true" : "false");
        getActivity().sendBroadcast(new Intent("com.android.settings.SYSBAR_SETTING_CHANGED"));
    }

    private void updateMoveNavigationBarOptions() {
        mMoveNavigationBar.setChecked(
                SystemProperties.getBoolean(PROPERTY_MOVE_NAVIGATION_BAR, false));
    }

    private void writeMoveNavigationBarOptions() {
        SystemProperties.set(
                PROPERTY_MOVE_NAVIGATION_BAR, mMoveNavigationBar.isChecked() ? "true" : "false");
        getActivity().sendBroadcast(new Intent("com.android.settings.NAVBAR_SETTINGS_CHANGED"));
    }
}
