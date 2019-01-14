package com.gfo.gfo_meesterproef.SearchTool.LookUp;

import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.Toast;

import com.gfo.gfo_meesterproef.R;
import com.gfo.gfo_meesterproef.SearchTool.ViewGRatingDiameter;
import com.gfo.gfo_meesterproef.Support.ConnectionCheck;
import com.gfo.gfo_meesterproef.User.UserActivity;

import java.util.concurrent.ExecutionException;

public class LookUpMeterActivity extends AppCompatActivity {

    String gRating, diameter, possibleDiameters;
    Spinner gRatingSpinner, diameterSpinner;

    ArrayAdapter<Integer> diameterAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_look_up_meter);
//        get all gRatings and convert to int[]
        String gRatings = null;
        try {
            gRatings = new ViewGRatingDiameter(this).execute("gRating").get();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();}
        String[] temporaryGRating = gRatings.split(",");
        int[] gRatingIntArray = new int[temporaryGRating.length];
        for (int i = 0; i < temporaryGRating.length; i++) {
            gRatingIntArray[i] = Integer.parseInt(temporaryGRating[i]);
        }
        Integer[] gRatingArray = new Integer[gRatingIntArray.length];
        for (int i = 0; i < gRatingIntArray.length; i++) {
            gRatingArray[i] = Integer.valueOf(gRatingIntArray[i]);
        }

//        get all diameters and convert to int[]
//        String diameters = null;
//        try {
//            diameters = new ViewGRatingDiameter(this).execute("diameter").get();
//        } catch (InterruptedException e) {
//            e.printStackTrace();
//        } catch (ExecutionException e) {
//            e.printStackTrace();}
//        String[] temporaryDiameter = diameters.split(",");
//        int[] diameterIntArray = new int[temporaryDiameter.length];
//        for (int i = 0; i < temporaryDiameter.length; i++) {
//            diameterIntArray[i] = Integer.parseInt(temporaryDiameter[i]);
//        }
//        Integer[] diameterArray = new Integer[diameterIntArray.length];
//        for (int i = 0; i < diameterIntArray.length; i++) {
//            diameterArray[i] = Integer.valueOf(diameterIntArray[i]);
//        }

//        create gRatingSpinner
        gRatingSpinner = (Spinner) findViewById(R.id.gRatingSpinner);
        ArrayAdapter<Integer> gRatingAdapter = new ArrayAdapter<Integer>(this, R.layout.spinner_item, gRatingArray);
        gRatingAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        gRatingSpinner.setAdapter(gRatingAdapter);

//        create diameterSpinner
        diameterSpinner = (Spinner) findViewById(R.id.diameterSpinner);
        diameterAdapter = null;
        diameterSpinner.setAdapter(diameterAdapter);

        onGRatingSelect();
    }

    private void onGRatingSelect() {
        gRatingSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View viewSelected, int position, long id) {
                //        get selected gRating
                gRating = gRatingSpinner.getSelectedItem().toString();
//                get possible diameters
                    try {
                        possibleDiameters = new PossibleDiameter().execute("grating", gRating).get();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    } catch (ExecutionException e) {
                        e.printStackTrace();}
//            convert
                    String[] temporaryPossibleDiameter = possibleDiameters.split(",");
                    int[] possibleDiameterIntArray = new int[temporaryPossibleDiameter.length];
                    for (int i = 0; i < temporaryPossibleDiameter.length; i++) {
                        possibleDiameterIntArray[i] = Integer.parseInt(temporaryPossibleDiameter[i]);
                    }
                Integer[] diameterArray = new Integer[possibleDiameterIntArray.length];
                    for (int i = 0; i < possibleDiameterIntArray.length; i++) {
                        diameterArray[i] = Integer.valueOf(possibleDiameterIntArray[i]);
                    }
//            update diameterSpinner/Adapter
                diameterSpinner = (Spinner) findViewById(R.id.diameterSpinner);
                diameterAdapter = new ArrayAdapter<Integer>(getApplicationContext(), R.layout.spinner_item, diameterArray);
                diameterAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
//                (next line) needed ...?
                diameterAdapter.notifyDataSetChanged();
                diameterSpinner.setAdapter(diameterAdapter);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
//                do nothing
            }
        });
    }

    public void confirm(View view){
        //        check for internet connection
        boolean connection = new ConnectionCheck().test(getApplicationContext());
        if (!connection){return;}
//        get selected values
        gRating = gRatingSpinner.getSelectedItem().toString();
        diameter = diameterSpinner.getSelectedItem().toString();

//        get url/location
        String result = null;
        try {
            result = new LookUpMeter(this).execute("lookup", gRating, diameter).get();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
//        error/open specifications
        switch (result){
            case "No Meter Found":
                Toast.makeText(this, "No Meter Found", Toast.LENGTH_SHORT).show();
                break;
            default:
                Intent web = new Intent(Intent.ACTION_VIEW);
                web.setData(Uri.parse(result));
                startActivity(web);
                break;
        }
    }

    @Override
    public void onBackPressed(){
        Intent i = new Intent(this, UserActivity.class);
        this.finish();
        startActivity(i);
    }
}