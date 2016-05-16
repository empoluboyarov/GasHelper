package empoluboyarov.com.gashelper.simplecounts;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import empoluboyarov.com.gashelper.R;
import empoluboyarov.com.gashelper.core.BaseCalc;
import empoluboyarov.com.gashelper.core.Utils;
import empoluboyarov.com.gashelper.core.Verifier;

public class CalcAdiabataActivity extends AppCompatActivity {

    private EditText etTemp, etPres, etDens, etAtmPres, etNitro;
    private TextView tvResult;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calc_adiabata);

        etTemp = (EditText) findViewById(R.id.etTempFD);
        etPres = (EditText) findViewById(R.id.etPresFD);
        etDens = (EditText) findViewById(R.id.etDensAdiabat);
        etAtmPres = (EditText) findViewById(R.id.etAtmPresFD);
        etNitro = (EditText) findViewById(R.id.etNitroAdiabat);
        tvResult = (TextView) findViewById(R.id.tvResultAdiabatic);
    }

    public void calcAdiabat(View view) {

        Verifier.isCheck = true;

        String txtTemp = etTemp.getText().toString();
        String txtPres = etPres.getText().toString();
        String txtDens = etDens.getText().toString();
        String txtAtmPres = etAtmPres.getText().toString();
        String txtNitro = etNitro.getText().toString();

        double dens = Verifier.checkDensity(txtDens);
        double pres = Verifier.checkPressure(txtPres);
        double temp = Verifier.checkTemperature(txtTemp);
        double atmPres = Verifier.checkAtmPressure(txtAtmPres);
        double nitrogen = Verifier.checkNitrogen(txtNitro);
        Utils.makeToast(this);

        if (Verifier.isCheck) {
            double patm = atmPres * 0.001359511;// атмосферное давление из мм рт. ст. в килограммы
            double pabs = pres + patm;// абсолютное давление
            temp = temp + 273.15;// температура по Кельвину
            double result = BaseCalc.calcAdiabata(temp, pabs, dens, nitrogen);
            tvResult.setText("" + result);
        } else tvResult.setText("" + 0.000);
    }
}
