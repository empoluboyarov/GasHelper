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

public class CalcSpeedGasActivity extends AppCompatActivity {

    private EditText etTemp, etPres, etDens, etAtmPres, etNitro;
    private TextView tvResult;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calc_speed_gas);

        etTemp = (EditText) findViewById(R.id.etTempSpeedSound);
        etPres = (EditText) findViewById(R.id.etPresSpeedSound);
        etDens = (EditText) findViewById(R.id.etDensSpeedSound);
        etAtmPres = (EditText) findViewById(R.id.etAtmPresSpeedSound);
        etNitro = (EditText) findViewById(R.id.etNitroSpeedSound);
        tvResult = (TextView) findViewById(R.id.tvResultSpeedSound);

    }

    public void calcSpeedGas(View view) {
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

        if (Verifier.isCheck){
            double patm = atmPres * 0.001359511;// атмосферное давление из мм рт. ст. в килограммы
            double pabs = pres + patm;// абсолютное давление
            temp = temp + 273.15;// температура по Кельвину
            double z = BaseCalc.calcZ(temp, pabs, dens);
            double adiabata = BaseCalc.calcAdiabata(temp, pabs, dens, nitrogen);
            double rofact = BaseCalc.calcRoFact(dens, pabs, temp, z);
            double result = BaseCalc.calcSpeedGas(adiabata, rofact, pabs, patm);
            tvResult.setText("" + result);
        } else tvResult.setText("" + 0.000);

    }
}
