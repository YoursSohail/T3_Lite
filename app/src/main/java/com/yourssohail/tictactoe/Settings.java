package com.yourssohail.tictactoe;

import android.content.SharedPreferences;
import android.preference.PreferenceActivity;
import android.preference.PreferenceFragment;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class Settings extends PreferenceActivity implements SharedPreferences.OnSharedPreferenceChangeListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        PreferenceManager.getDefaultSharedPreferences(getBaseContext()).registerOnSharedPreferenceChangeListener(this);
        setTheme();
        super.onCreate(savedInstanceState);
        getFragmentManager().beginTransaction().replace(android.R.id.content,new PrefsFragment()).commit();
    }

    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
        if(key.equals("color_choices")){
            this.recreate();
        }
    }


    public static class PrefsFragment extends PreferenceFragment{
        @Override
        public void onCreate(@Nullable Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            addPreferencesFromResource(R.xml.preference);
        }
    }

    public void setTheme(){
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(getBaseContext());

        if(sp.getString("color_choices","Green").equals("Green")){
            setTheme(R.style.GreenTheme);

        }else if(sp.getString("color_choices","Orange").equals("Orange")){
            setTheme(R.style.OrangeTheme);
        }else if(sp.getString("color_choices","Red").equals("Red")){
            setTheme(R.style.RedTheme);
        }else if(sp.getString("color_choices","Blue").equals("Blue")){
            setTheme(R.style.BlueTheme);
        }else if(sp.getString("color_choices","Yellow").equals("Yellow")){
            setTheme(R.style.YellowTheme);
        }
    }
}
