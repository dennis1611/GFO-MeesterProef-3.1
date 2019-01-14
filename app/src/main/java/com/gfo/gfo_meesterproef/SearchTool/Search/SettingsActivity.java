package com.gfo.gfo_meesterproef.SearchTool.Search;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.gfo.gfo_meesterproef.R;
import com.gfo.gfo_meesterproef.SearchTool.Search.SearchMeterActivity;

public class SettingsActivity extends AppCompatActivity {

    Spinner volumeSpinner, pressureSpinner, temperatureSpinner;
    String volumeUnit, pressureUnit, temperatureUnit;
    TextView volumeTV, pressureTV, temperatureTV;

    public static Context contextOfSettings;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        //        needed to save options
        contextOfSettings = getApplicationContext();

//        get saved unit values
        SharedPreferences unitsPref = getSharedPreferences("unitsPreference", contextOfSettings.MODE_PRIVATE);
        volumeUnit = unitsPref.getString("volumeUnit", "Nm³/h");
        pressureUnit = unitsPref.getString("pressureUnit", "bar abs.");
        temperatureUnit = unitsPref.getString("temperatureUnit", "Degree Centigrade");
//        setup TextViews and display currently selected units
        volumeTV = (TextView) findViewById(R.id.volumeUnitTextView);
        volumeTV.append(" "+volumeUnit);
        pressureTV = (TextView) findViewById(R.id.pressureUnitTextView);
        pressureTV.append(" "+pressureUnit);
        temperatureTV = (TextView) findViewById(R.id.temperatureUnitTextView);
        temperatureTV.append(" "+temperatureUnit);

        createSpinners();
    }

    private void createSpinners() {
        //        create volumeSpinner
        volumeSpinner = (Spinner) findViewById(R.id.volumeSpinner);
        ArrayAdapter<CharSequence> volumeAdapter = ArrayAdapter.createFromResource(this, R.array.unit_Volume, R.layout.spinner_item);
        volumeAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        volumeSpinner.setAdapter(volumeAdapter);
//        create pressureSpinner
        pressureSpinner = (Spinner) findViewById(R.id.pressureSpinner);
        ArrayAdapter<CharSequence> pressureAdapter = ArrayAdapter.createFromResource(this, R.array.unit_pressure, R.layout.spinner_item);
        pressureAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        pressureSpinner.setAdapter(pressureAdapter);
//        create temperatureSpinner
        temperatureSpinner = (Spinner) findViewById(R.id.temperatureSpinner);
        ArrayAdapter<CharSequence> temperatureAdapter = ArrayAdapter.createFromResource(this, R.array.unit_temperature, R.layout.spinner_item);
        temperatureAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        temperatureSpinner.setAdapter(temperatureAdapter);
    }

    //    save settings
    public void confirm(View view) {
//        get selected values
        volumeUnit = volumeSpinner.getSelectedItem().toString();
        pressureUnit = pressureSpinner.getSelectedItem().toString();
        temperatureUnit = temperatureSpinner.getSelectedItem().toString();
//        save values
        SharedPreferences unitsPref = getSharedPreferences("unitsPreference", contextOfSettings.MODE_PRIVATE);
        unitsPref.edit().putString("volumeUnit", volumeUnit).apply();
        unitsPref.edit().putString("pressureUnit", pressureUnit).apply();
        unitsPref.edit().putString("temperatureUnit", temperatureUnit).apply();
//        confirmation
        Toast.makeText(contextOfSettings, "saved", Toast.LENGTH_SHORT).show();
//        close screen
        Intent i = new Intent(this, SearchMeterActivity.class);
        this.finish();
        startActivity(i);
    }

    @Override
    public void onBackPressed(){
        Intent i = new Intent(this, SearchMeterActivity.class);
        this.finish();
        startActivity(i);
    }
}
