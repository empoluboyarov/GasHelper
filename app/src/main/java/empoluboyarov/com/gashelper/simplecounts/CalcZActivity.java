package empoluboyarov.com.gashelper.simplecounts;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import empoluboyarov.com.gashelper.R;
import empoluboyarov.com.gashelper.core.BaseCalc;
import empoluboyarov.com.gashelper.core.Utils;
import empoluboyarov.com.gashelper.core.Verifier;
import empoluboyarov.com.gashelper.methodics.BaseMethodicsActivity;

public class CalcZActivity extends AppCompatActivity {

    private EditText etTemp, etPres, etDens, etAtmPres;
    private TextView tvResult;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calc_z);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), BaseMethodicsActivity.class);
                startActivity(intent);
            }
        });


        etTemp = (EditText) findViewById(R.id.etTempFD);
        etPres = (EditText) findViewById(R.id.etPresFD);
        etDens = (EditText) findViewById(R.id.etDensAdiabat);
        etAtmPres = (EditText) findViewById(R.id.etAtmPresFD);
        tvResult = (TextView) findViewById(R.id.tvResultZ);
    }

    public void calcZ(View view) {

        Verifier.isCheck = true;

        String txtTemp = etTemp.getText().toString();
        String txtPres = etPres.getText().toString();
        String txtDens = etDens.getText().toString();
        String txtAtmPres = etAtmPres.getText().toString();


        double dens = Verifier.checkDensity(txtDens);
        double pres = Verifier.checkPressure(txtPres);
        double temp = Verifier.checkTemperature(txtTemp);
        double atmPres = Verifier.checkAtmPressure(txtAtmPres);
        Utils.makeToast(this);


        if (Verifier.isCheck) {
            double patm = atmPres * 0.001359511;// атмосферное давление из мм рт. ст. в килограммы
            double pabs = pres + patm;// абсолютное давление
            temp = temp + 273.15;// температура по Кельвину
            double result = BaseCalc.calcZ(temp, pabs, dens);
            tvResult.setText("" + result);
        } else tvResult.setText("" + 0.000);
    }
}
