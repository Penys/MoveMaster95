package com.penys.fi.movemaster95

import android.os.Bundle
import android.preference.PreferenceFragment

@Suppress("DEPRECATION")
class SettingsFragment : PreferenceFragment() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        addPreferencesFromResource(R.xml.preferences)
    }
}