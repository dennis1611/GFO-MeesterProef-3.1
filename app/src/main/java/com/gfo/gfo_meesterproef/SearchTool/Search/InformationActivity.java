package com.gfo.gfo_meesterproef.SearchTool.Search;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

import com.gfo.gfo_meesterproef.R;

public class InformationActivity extends AppCompatActivity {

    int meter;
    double lineCapacity, pipeVelocity, maxCapacity, req_G_Rating, minCapacity, maxDensityAtQLine, PMaxAtQLine, dpLineCond, diameter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_information);
        if(meter == -1) {
            ((TextView) findViewById(R.id.informationTextView)).setText(getString(R.string.label_noMeterPossible));
        } else {
            lineCapacity = (Math.round(((double) (getIntent().getExtras().get("LineCapacity")))*10.0))/10.0;
            pipeVelocity = (Math.round(((double) (getIntent().getExtras().get("PipeVelocity")))*10.0))/10.0;
            maxCapacity = (Math.round(((double) (getIntent().getExtras().get("MaxCapacity")))*10.0))/10.0;
            req_G_Rating = (Math.round(((double) (getIntent().getExtras().get("Req_G_Rating")))*10.0))/10.0;
            minCapacity = (Math.round(((double) (getIntent().getExtras().get("MinCapacity")))*10.0))/10.0;
            maxDensityAtQLine = (Math.round(((double) (getIntent().getExtras().get("MaxDensityAtQLine")))*10.0))/10.0;
            PMaxAtQLine = (Math.round(((double) (getIntent().getExtras().get("PMaxAtQLine")))*10.0))/10.0;
            dpLineCond = (Math.round(((double) (getIntent().getExtras().get("DpLineCond")))*10.0))/10.0;
            diameter = (double) getIntent().getExtras().get("Diameter")*1000;
            //System.out.println("Het werkt nog steeds");
            ((TextView) findViewById(R.id.informationTextView)).setText(
                    getString(R.string.label_BestOption1) + req_G_Rating + " " + getString(R.string.label_BestOption2) + diameter + getString(R.string.label_BestOption3) + "\n"
                            + getString(R.string.label_LineCapacity) + lineCapacity + "\n"
                            + getString(R.string.label_PipeVelocity) + pipeVelocity + "\n"
                            + getString(R.string.label_MaxCapacity) + maxCapacity + "\n"
                            + getString(R.string.label_RequiredGRating) + req_G_Rating + "\n"
                            + getString(R.string.label_MinimalCapacity) + minCapacity + "\n"
                            + getString(R.string.label_MaximalDensityAtQLine) + maxDensityAtQLine + "\n"
                            + getString(R.string.label_PMaxAtQLine) + PMaxAtQLine + "\n"
                            + getString(R.string.label_DpLineCond) + dpLineCond);
        }
    }

    @Override
    public void onBackPressed() {
//            go back to WhichMeterDoINeed (Search-side)
            Intent intent = new Intent(this, SearchMeterActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
            startActivity(intent);
        }
}