package com.alexeychurchill.plotbuilder;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.alexeychurchill.plotbuilder.math.MathParser;

public class EnterDataActivity extends AppCompatActivity implements View.OnClickListener {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_enter_data);
        Button btnBuild = ((Button) findViewById(R.id.btnBuild));
        if (btnBuild != null) {
            btnBuild.setOnClickListener(this);
        }
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btnBuild:
                callShowPlotActivity();
                break;
        }
    }

    private void callShowPlotActivity() {
        //lg(x^3-1.2)/(x^2+cos(x))
//        EditText etFunction = ((EditText) findViewById(R.id.etFunction));
//        EditText etFrom = ((EditText) findViewById(R.id.etFrom));
//        EditText etTo = ((EditText) findViewById(R.id.etTo));
//        Intent showPlotIntent = new Intent(this, ShowPlotActivity.class);
//        if (etFunction == null || etFrom == null || etTo == null) {
//            return;
//        }
//        showPlotIntent.putExtra(ShowPlotActivity.EXTRA_FUNCTION, etFunction.getText().toString());
//        showPlotIntent.putExtra(ShowPlotActivity.EXTRA_FROM, etFrom.getText().toString());
//        showPlotIntent.putExtra(ShowPlotActivity.EXTRA_TO, etTo.getText().toString());
//        startActivity(showPlotIntent);
        MathParser parser = new MathParser("lg(x^3-1.2)/(x^2+cos(x))");
//        MathParser parser = new MathParser("(a/b^2-455.34)*(4-d^4/2)");
//        MathParser parser = new MathParser("a+b*c");
//        MathParser parser = new MathParser("a+(b-c)*d");
//        MathParser parser = new MathParser("a+b*c-d");
//        MathParser parser = new MathParser("(a+b)*c");
//        MathParser parser = new MathParser("(a+b*c)/2");
//        MathParser parser = new MathParser("(a*(b+c)+d)/2");
//        MathParser parser = new MathParser("x+sin(x)*2");
//        MathParser parser = new MathParser("a+sin(b+c)*somevar");
//        MathParser parser = new MathParser("");
        parser.parse();
    }
}
