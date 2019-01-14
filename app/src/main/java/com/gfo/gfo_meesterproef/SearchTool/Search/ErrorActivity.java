package com.gfo.gfo_meesterproef.SearchTool.Search;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

import com.gfo.gfo_meesterproef.R;

public class ErrorActivity extends AppCompatActivity {

    TextView pipeVelocityTooHighTextView, PmaxAtQLineTooHighTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_error);

        pipeVelocityTooHighTextView = (TextView) findViewById(R.id.pipeVelocityTooHighTextView);
        PmaxAtQLineTooHighTextView = (TextView) findViewById(R.id.PmaxAtQLineTooHighTextView);
        if(getIntent().getExtras().getBoolean("isPipeVelocityTooBig"))
        {
            pipeVelocityTooHighTextView.setText("Pipe velocity is too high, consider using bigger pipes or less volume.");
        }
        if(getIntent().getExtras().getBoolean("isPmaxAtQLineTooBig"))
        {
            PmaxAtQLineTooHighTextView.setText("Pressure at QLine is too high for available meters, contact GFO for a special meter for your situation.");
        }
    }
    @Override
    public void onBackPressed() {
        Intent intent = new Intent(this, SearchMeterActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
        this.finish();
        startActivity(intent);
    }
}