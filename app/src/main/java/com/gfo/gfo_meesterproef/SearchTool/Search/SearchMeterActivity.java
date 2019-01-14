package com.gfo.gfo_meesterproef.SearchTool.Search;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import com.gfo.gfo_meesterproef.R;
import com.gfo.gfo_meesterproef.SearchTool.ViewGRatingDiameter;
import com.gfo.gfo_meesterproef.User.UserActivity;

import java.util.concurrent.ExecutionException;

import static com.gfo.gfo_meesterproef.SearchTool.Search.SettingsActivity.contextOfSettings;

public class SearchMeterActivity extends AppCompatActivity {

    double[] values; // 0 volume, 1 linePressure, 2 lineTemp, 3baseTemp, 4 basePress, 5 (pipeDiameter), 6 QminATMAir, 7 densityAtQmax, 8 densityAtBase, 9 dpATMAir, 10 relDensity;
    EditText[] valuesEditText; // 0 volume, 1 linePressure, 2 lineTemp, 3baseTemp, 4 basePress, 5 pipeDiameter, 6 QminATMAir, 7 densityAtQmax, 8 densityAtBase, 9 dpATMAir, 10 relDensity;
    boolean[] isErrorInValues; // 0 volume, 1 linePressure, 2 lineTemp, 3baseTemp, 4 basePress, 5 pipeDiameter, 6 QminATMAir, 7 densityAtQmax, 8 densityAtBase, 9 dpATMAir, 10 relDensity;
    Spinner pipeDiameterSpinner;
    double lineCapacity, req_G_Rating, pipeVelocity, maxCapacity, minCapacity, maxDensityAtQLine, PMaxAtQLine, dpLineCond;
    int[] gRatings;
    double[][] gRating_Diameter;
    double maxPipeVelocity, maxPmaxAtQLine;
    boolean isPipeVelocityTooBig, isPmaxAtQLineTooBig;
    int spinnerNumber;
    int possibleMeter;
//
    Integer[] diameterArray;
    //    Units
    String volumeUnit, pressureUnit, temperatureUnit;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_meter);

        fillArrays();

//        get saved unit values
        SharedPreferences unitsPref = getSharedPreferences("unitsPreference", contextOfSettings.MODE_PRIVATE);
        volumeUnit = unitsPref.getString("volumeUnit", "Nm³/h");
        pressureUnit = unitsPref.getString("pressureUnit", "bar abs.");
        temperatureUnit = unitsPref.getString("temperatureUnit", "Degree Centigrade");

//        create diameterSpinner
        pipeDiameterSpinner = (Spinner) findViewById(R.id.pipeDiameterSpinner);

        ArrayAdapter<CharSequence> pipeDiameterAdapter = ArrayAdapter.createFromResource(this, R.array.spinner_pipeDiameter, R.layout.spinner_item);
        pipeDiameterAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        pipeDiameterSpinner.setAdapter(pipeDiameterAdapter);
        spinnerNumber = 5;

        fillGRating_diameter();
        maxPipeVelocity = 20.0;
        maxPmaxAtQLine = 1000.0; //???

        //ONLY FOR TESTING PURPOSES
        setText();

//        append units
        ((TextView) findViewById(R.id.volumeTextView)).append(" ("+volumeUnit+")");
        ((TextView) findViewById(R.id.linePressureTextView)).append(" ("+pressureUnit+")");
        ((TextView) findViewById(R.id.lineTempTextView)).append(" ("+temperatureUnit+")");
        ((TextView) findViewById(R.id.baseTempTextView)).append(" ("+temperatureUnit+")");
        ((TextView) findViewById(R.id.basePressureTextView)).append(" ("+pressureUnit+")");
        ((TextView) findViewById(R.id.pipeDiameterTextView)).append(" "+"(mm)");
        ((TextView) findViewById(R.id.QminATMAirTextView)).append(" "+"(m³/h)");
        ((TextView) findViewById(R.id.densityAtQMaxTextView)).append(" "+"(kg/m³)");
        ((TextView) findViewById(R.id.densityAtBaseTextView)).append(" "+"(kg/m³)");
        ((TextView) findViewById(R.id.dpATMAirTextView)).append(" "+"(mbar)");
    }

    public void fillArrays(){
        gRatings = new int[20]; //0, 10, 16, 25, 40, 65, 100, 160, 250, 400, 650, 1000, 1600, 2500, 4000, 6500, 10000, 16000, 25000, 40000
        gRatings[0] = 0;
        gRatings[1] = 10;
        gRatings[2] = 16;
        gRatings[3] = 25;
        gRatings[4] = 40;
        gRatings[5] = 65;
        gRatings[6] = 100;
        gRatings[7] = 160;
        gRatings[8] = 250;
        gRatings[9] = 400;
        gRatings[10] = 650;
        gRatings[11] = 1000;
        gRatings[12] = 1600;
        gRatings[13] = 2500;
        gRatings[14] = 4000;
        gRatings[15] = 6500;
        gRatings[16] = 10000;
        gRatings[17] = 16000;
        gRatings[18] = 25000;
        gRatings[19] = 40000;

//        get possible diameters
//        String diameters = null;
//        try {
//            diameters = new ViewGRatingDiameter(this).execute("diameter").get();
//        } catch (InterruptedException e) {
//            e.printStackTrace();
//        } catch (ExecutionException e) {
//            e.printStackTrace();}
//        convert diameters to int[]
//        String[] temporaryDiameter = diameters.split(",");
//        int[] diameterIntArray = new int[temporaryDiameter.length];
//        for (int i = 0; i < temporaryDiameter.length; i++) {
//            diameterIntArray[i] = Integer.parseInt(temporaryDiameter[i]);
//        }
//        diameterArray = new Integer[diameterIntArray.length];
//        for (int i = 0; i < diameterIntArray.length; i++) {
//            diameterArray[i] = Integer.valueOf(diameterIntArray[i]);
//        }
    }
    public void fillGRating_diameter(){
        gRating_Diameter = new double[37][2];
        gRating_Diameter[0][0] = 10;
        gRating_Diameter[1][0] = 10;
        gRating_Diameter[2][0] = 16;
        gRating_Diameter[3][0] = 16;
        gRating_Diameter[4][0] = 25;
        gRating_Diameter[5][0] = 25;
        gRating_Diameter[6][0] = 40;
        gRating_Diameter[7][0] = 40;
        gRating_Diameter[8][0] = 65;
        gRating_Diameter[9][0] = 100;
        gRating_Diameter[10][0] = 160;
        gRating_Diameter[11][0] = 160;
        gRating_Diameter[12][0] = 250;
        gRating_Diameter[13][0] = 250;
        gRating_Diameter[14][0] = 400;
        gRating_Diameter[15][0] = 400;
        gRating_Diameter[16][0] = 650;
        gRating_Diameter[17][0] = 650;
        gRating_Diameter[18][0] = 1000;
        gRating_Diameter[19][0] = 1000;
        gRating_Diameter[20][0] = 1000;
        gRating_Diameter[21][0] = 1600;
        gRating_Diameter[22][0] = 1600;
        gRating_Diameter[23][0] = 1600;
        gRating_Diameter[24][0] = 2500;
        gRating_Diameter[25][0] = 2500;
        gRating_Diameter[26][0] = 2500;
        gRating_Diameter[27][0] = 4000;
        gRating_Diameter[28][0] = 4000;
        gRating_Diameter[29][0] = 4000;
        gRating_Diameter[30][0] = 6500;
        gRating_Diameter[31][0] = 6500;
        gRating_Diameter[32][0] = 6500;
        gRating_Diameter[33][0] = 10000;
        gRating_Diameter[34][0] = 10000;
        gRating_Diameter[35][0] = 16000;
        gRating_Diameter[36][0] = 25000;

        gRating_Diameter[0][1] = 40;
        gRating_Diameter[1][1] = 50;
        gRating_Diameter[2][1] = 40;
        gRating_Diameter[3][1] = 50;
        gRating_Diameter[4][1] = 40;
        gRating_Diameter[5][1] = 50;
        gRating_Diameter[6][1] = 40;
        gRating_Diameter[7][1] = 50;
        gRating_Diameter[8][1] = 50;
        gRating_Diameter[9][1] = 80;
        gRating_Diameter[10][1] = 80;
        gRating_Diameter[11][1] = 100;
        gRating_Diameter[12][1] = 80;
        gRating_Diameter[13][1] = 100;
        gRating_Diameter[14][1] = 100;
        gRating_Diameter[15][1] = 150;
        gRating_Diameter[16][1] = 150;
        gRating_Diameter[17][1] = 200;
        gRating_Diameter[18][1] = 150;
        gRating_Diameter[19][1] = 200;
        gRating_Diameter[20][1] = 250;
        gRating_Diameter[21][1] = 200;
        gRating_Diameter[22][1] = 250;
        gRating_Diameter[23][1] = 300;
        gRating_Diameter[24][1] = 250;
        gRating_Diameter[25][1] = 300;
        gRating_Diameter[26][1] = 400;
        gRating_Diameter[27][1] = 300;
        gRating_Diameter[28][1] = 400;
        gRating_Diameter[29][1] = 500;
        gRating_Diameter[30][1] = 400;
        gRating_Diameter[31][1] = 500;
        gRating_Diameter[32][1] = 600;
        gRating_Diameter[33][1] = 500;
        gRating_Diameter[34][1] = 600;
        gRating_Diameter[35][1] = 600;
        gRating_Diameter[36][1] = 600;
    }

    public void search(View view){
        getText();
        for(int i = 0; i < valuesEditText.length; i++) {
            if(isErrorInValues[i]) {
                if(i==spinnerNumber) {
                    try {
                        pipeDiameterSpinner.setBackgroundColor(Color.parseColor("#ff0000")); //ColorError
                    }catch (Exception ex) {
                        //System.out.println("ErrorInSetColor");
                    }
                } else {
                    try {
                        valuesEditText[i].setBackgroundColor(Color.parseColor("#ff0000"));//ColorError
                    } catch (Exception ex) {
                        //System.out.println("ErrorInSetColor");
                    }
                }
            } else {
                if(i==spinnerNumber) {
                    try {
                        pipeDiameterSpinner.setBackgroundColor(0); //ColorError
                    } catch (Exception ex) {
                        //System.out.println("ErrorInSetColor");
                    }
                } else {
                    try {
                        valuesEditText[i].setBackgroundColor(0); //ColorError
                    } catch (Exception ex) {
                        //System.out.println("ErrorInSetColor");
                    }
                }
            }
        }
        convertUnits();
        calculate();
        testValues();
        searchPossibleMeter();
        toMeterChoosingScreen();
    }

    private void getText() {
        valuesEditText = new EditText[11];
        valuesEditText[0] = (EditText) findViewById(R.id.volumeEditText);
        valuesEditText[1] = (EditText) findViewById(R.id.linePressureEditText);
        valuesEditText[2] = (EditText) findViewById(R.id.lineTempEditText);
        valuesEditText[3] = (EditText) findViewById(R.id.baseTempEditText);
        valuesEditText[4] = (EditText) findViewById(R.id.basePressureEditText);
        valuesEditText[5] = null;
        valuesEditText[6] = (EditText) findViewById(R.id.QminATMAirEditText);
        valuesEditText[7] = (EditText) findViewById(R.id.densityAtQmaxEditText);
        valuesEditText[8] = (EditText) findViewById(R.id.densityAtBaseEditText);
        valuesEditText[9] = (EditText) findViewById(R.id.dpATMAirEditText);
        valuesEditText[10] = (EditText) findViewById(R.id.relDensityEditText);

        values = new double[valuesEditText.length];
        isErrorInValues = new boolean[valuesEditText.length];

        for(int i = 0; i < valuesEditText.length; i++) {
            isErrorInValues[i] = false;
            try {
                values[i] = Double.parseDouble(valuesEditText[i].getText().toString().replaceAll(",","."));
                //System.out.println("EditTextValue: " + i + "= " + values[i]);
            } catch (Exception ex) {
                //System.out.println("ErrorEditText " + i);
                isErrorInValues[i] = true;
            }
        }
        //System.out.println("Spinner");
        pipeDiameterSpinner = (Spinner) findViewById(R.id.pipeDiameterSpinner);
        isErrorInValues[spinnerNumber] = false;
        try {
            values[spinnerNumber] = Double.parseDouble(pipeDiameterSpinner.getSelectedItem().toString());
            //System.out.println("SpinnerValue = " + values[spinnerNumber]);
        }
        catch (Exception ex) {
            isErrorInValues[spinnerNumber] = true;
            //System.out.println("ErrorSpinner");
        }
    }

    private void convertUnits() {
//        convert mm to m (always)
        values[5] = values[5]/1000;
//        convert volumeUnit when needed
        if (volumeUnit.equals(getResources().getString(R.string.Volume_TJday))){
//            convert TJ/day to Nm³/h
            values[0] = values[0] / 1011.793333;}
//        else if (volumeUnit.equals(getResources().getString(R.string.Volume_GJSm3))){
//            convert GHV(GJ/Sm³) to Nm³/h
//        values[0] = values[0] ...;
//        }

//        convert pressureUnit when needed
        if (pressureUnit.equals(getResources().getString(R.string.Pressure_MPa))){
//            convert MPa to bar abs.
            values[1] = values[1] * 12.533125;
            values[4] = values[4] * 12.533125;
        }
//        convert temperatureUnit when needed
        if (temperatureUnit.equals(getResources().getString(R.string.Temperature_Centrigrade))){
//            convert Celsius to K
            values[2] = values[2] + 273.15;
            values[3] = values[3] + 273.15;
        }
    }
    public void calculate() // values[]: 0 volume, 1 linePressure, 2 lineTemp, 3baseTemp, 4 basePress, 5 pipeDiameter, 6 QminATMAir, 7 densityAtQmax, 8 densityAtBase, 9 dpATMAir, 10 relDensity;
    {
        //System.out.println("Calculate");
        lineCapacity = values[0]/( (values[1]/values[4])*(values[3]/values[2]) );
        pipeVelocity = (lineCapacity/3600)/( 0.25*Math.PI*Math.pow(values[5],2) );
        calculateMaxCapacity();
        req_G_Rating = maxCapacity;
        minCapacity = values[6]*Math.sqrt( (1.01325/values[1])*(1/values[10]) );
        maxDensityAtQLine = values[7]/( Math.pow((lineCapacity/maxCapacity), 2) );
        PMaxAtQLine = maxDensityAtQLine/values[8];
        dpLineCond = values[9]*values[10]*(values[1]/1.01325)*Math.pow((lineCapacity/maxCapacity), 2);
    }

    private void calculateMaxCapacity() {
        maxCapacity = gRatings[0];
        for(int i =0; lineCapacity - gRatings[i] >= 0; i++)
            maxCapacity = gRatings[i+1];
    }

    public void testValues() {
        //System.out.println("testValues");
        isPipeVelocityTooBig = false;
        isPmaxAtQLineTooBig = false;
        if(pipeVelocity > maxPipeVelocity)
        {
            isPipeVelocityTooBig = true;
        }
        if(PMaxAtQLine > maxPmaxAtQLine)
        {
            isPmaxAtQLineTooBig = true;
        }
    }

    public void searchPossibleMeter() {
        possibleMeter = -1;
        for(int i = 0; i < gRating_Diameter.length; i++)
        {
            if((gRating_Diameter[i][0] == req_G_Rating)&& (gRating_Diameter[i][1]/1000 == values[5]))
            {
                possibleMeter = i;
                //System.out.println("Found a meter: " + i);
            }
        }
    }

    private void toMeterChoosingScreen() {
        boolean startInformationMeterScreen = true;
        for(int i = 0; i < valuesEditText.length; i++)
        {
            if(isErrorInValues[i])
            {
                startInformationMeterScreen = false;
            }
        }
        if((isPipeVelocityTooBig||isPmaxAtQLineTooBig)&&startInformationMeterScreen)
        {
            //System.out.println("isPipeVelocityTooBig||isPmaxAtQLineTooBig");
            Intent intent = new Intent(this, ErrorActivity.class);
            intent.putExtra("isPipeVelocityTooBig", isPipeVelocityTooBig);
            intent.putExtra("isPmaxAtQLineTooBig", isPmaxAtQLineTooBig);
            startActivity(intent);
        }
        else if(startInformationMeterScreen) {
            Intent i = new Intent(this, InformationActivity.class);
            i.putExtra("PossibleMeter", possibleMeter);
            i.putExtra("LineCapacity", lineCapacity);
            i.putExtra("PipeVelocity", pipeVelocity);
            i.putExtra("MaxCapacity", maxCapacity);
            i.putExtra("Req_G_Rating", req_G_Rating);
            i.putExtra("MinCapacity", minCapacity);
            i.putExtra("MaxDensityAtQLine", maxDensityAtQLine);
            i.putExtra("PMaxAtQLine", PMaxAtQLine);
            i.putExtra("DpLineCond", dpLineCond);
            i.putExtra("Diameter", values[5]);
            startActivity(i);
        }
    }


    @Override
    //    needed for icons in toolbar
    public boolean onCreateOptionsMenu(Menu menu){
        getMenuInflater().inflate(R.menu.settings, menu);
        return true;
    }
    @Override
    //    set onClick to icons in toolbar
    public boolean onOptionsItemSelected(MenuItem item){
        int id = item.getItemId();
        if (id == R.id.action_item_settings) {
            Intent i = new Intent(this, SettingsActivity.class);
            startActivity(i);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    //    ONLY FOR TESTING PURPOSES
    public void setText() {
        ((EditText) findViewById(R.id.volumeEditText)).setText("230");
        ((EditText) findViewById(R.id.linePressureEditText)).setText("2.51325");
        ((EditText) findViewById(R.id.lineTempEditText)).setText("25");
        ((EditText) findViewById(R.id.baseTempEditText)).setText("0");
        ((EditText) findViewById(R.id.basePressureEditText)).setText("1.01325");
//        6. diameter
        ((EditText) findViewById(R.id.QminATMAirEditText)).setText("16");
        ((EditText) findViewById(R.id.densityAtQmaxEditText)).setText("53");
        ((EditText) findViewById(R.id.densityAtBaseEditText)).setText("0.83");
        ((EditText) findViewById(R.id.dpATMAirEditText)).setText("3");
        ((EditText) findViewById(R.id.relDensityEditText)).setText("0.624");
    }

    @Override
    public void onBackPressed(){
        Intent i = new Intent(this, UserActivity.class);
        this.finish();
        startActivity(i);
    }
}
