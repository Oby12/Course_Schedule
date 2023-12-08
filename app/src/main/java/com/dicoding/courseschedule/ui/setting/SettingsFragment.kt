package com.dicoding.courseschedule.ui.setting

import android.os.Bundle
import androidx.appcompat.app.AppCompatDelegate
import androidx.preference.ListPreference
import androidx.preference.PreferenceFragmentCompat
import androidx.preference.SwitchPreference
import com.dicoding.courseschedule.R
import com.dicoding.courseschedule.notification.DailyReminder

class SettingsFragment : PreferenceFragmentCompat() {

    private val reminder = DailyReminder()

    override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
        setPreferencesFromResource(R.xml.root_preferences, rootKey)
        //TODO 10 : Update theme based on value in ListPreference

        val switchMode = findPreference<ListPreference>(getString(R.string.pref_key_dark))
        switchMode?.setOnPreferenceChangeListener{_,newValue->
            when(newValue){
                getString(R.string.pref_dark_on) ->{
                    updateTheme(AppCompatDelegate.MODE_NIGHT_YES)
                }
                getString(R.string.pref_dark_off) ->{
                    updateTheme(AppCompatDelegate.MODE_NIGHT_NO)
                }
                getString(R.string.pref_dark_auto) ->{
                    updateTheme(AppCompatDelegate.MODE_NIGHT_AUTO_BATTERY)
                }

                else -> {
                    //nothing handler
                }
            }
            true
        }
        //TODO 11 : Schedule and cancel notification in DailyReminder based on SwitchPreference

        val switchNotifiy = findPreference<SwitchPreference>(getString(R.string.pref_key_notify))
        switchNotifiy?.setOnPreferenceChangeListener{_,newValue ->
             if (newValue as Boolean){
                 reminder.setDailyReminder(requireContext())
             }else{
                 reminder.cancelAlarm(requireContext())
             }
            true
        }
    }

    private fun updateTheme(nightMode: Int): Boolean {
        AppCompatDelegate.setDefaultNightMode(nightMode)
        requireActivity().recreate()
        return true
    }
}