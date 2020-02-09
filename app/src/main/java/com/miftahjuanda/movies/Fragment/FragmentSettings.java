package com.miftahjuanda.movies.Fragment;


import android.annotation.TargetApi;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.preference.Preference;
import android.preference.PreferenceFragment;
import android.preference.SwitchPreference;
import android.provider.Settings;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.miftahjuanda.movies.Model.Movie;
import com.miftahjuanda.movies.Model.MovieData;
import com.miftahjuanda.movies.R;
import com.miftahjuanda.movies.Reminder.DailyAlarmReceiver;
import com.miftahjuanda.movies.Reminder.ReleaseTodayReminder;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

/**
 * A simple {@link Fragment} subclass.
 */
public class FragmentSettings extends PreferenceFragment implements Preference.OnPreferenceClickListener, Preference.OnPreferenceChangeListener {

    private DailyAlarmReceiver dailyAlarmReceiver = new DailyAlarmReceiver();
    private ReleaseTodayReminder releaseTodayReminder = new ReleaseTodayReminder();
    public static CharSequence CHANNEL_NAME = "NOTIFICATION";
    private Movie movie;
    private List<MovieData> ReleaseToday = new ArrayList<>();


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.preferences);

        SwitchPreference switchDailyReminder = (SwitchPreference) findPreference(getString(R.string.key_daily_reminder));
        SwitchPreference switchUpcomingReminder = (SwitchPreference) findPreference(getString(R.string.key_release_reminder));

        switchDailyReminder.setOnPreferenceChangeListener(this);
        switchUpcomingReminder.setOnPreferenceChangeListener(this);
        findPreference(getString(R.string.key_setting_language)).setOnPreferenceClickListener(this);
    }

    @TargetApi(Build.VERSION_CODES.M)
    @Override
    public boolean onPreferenceChange(Preference preference, Object newValue) {
        String key = preference.getKey();
        boolean isSet = (boolean) newValue;
        if (key.equals(getString(R.string.key_daily_reminder))) {
            if (isSet) {
                dailyAlarmReceiver.setRepeatingAlarm(getActivity());
            } else {
                dailyAlarmReceiver.cancelAlarm(getActivity());
            }
        }else{
            if (isSet) {
                releaseTodayReminder.setRepeatingAlarm(getActivity());
            } else {
                releaseTodayReminder.cancelAlarm(getActivity());
            }
        }
        return true;
    }

    @Override
    public boolean onPreferenceClick(Preference preference) {
        String key = preference.getKey();

        if (key.equals(getString(R.string.key_setting_language))) {
            Intent languageIntent = new Intent(Settings.ACTION_LOCALE_SETTINGS);
            startActivity(languageIntent);
        }
        return true;
    }

    private Date dateFormatter(String movieDate) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
        Date date = null;
        try {
            date = sdf.parse(movieDate);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return date;
    }
}
