package com.app.weatherapp.mvvm.ui.SettingsFragment

import android.os.Bundle
import androidx.navigation.findNavController
import androidx.preference.Preference
import androidx.preference.PreferenceFragmentCompat
import androidx.preference.PreferenceManager
import androidx.preference.SwitchPreference
import com.app.weatherapp.R

class SettingsFragment : PreferenceFragmentCompat() {

    override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
        setPreferencesFromResource(R.xml.root_preferences, rootKey)
        val sp= PreferenceManager.getDefaultSharedPreferences(context)
        val mapFragment:SwitchPreference?=findPreference("MAP_LOCATION")
        mapFragment?.onPreferenceClickListener=Preference.OnPreferenceClickListener {

            if(sp.getBoolean("MAP_LOCATION", true))
            {
                view?.findNavController()
                    ?.navigate(R.id.action_settingsFragment_to_mapsFragment2)
            }
            true
        }



    }
}