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

public class CalcGasCountActivity extends AppCompatActivity {

    private EditText etTemp, etPres, etDens, etAtmPres, etLength, etDiameter;
    private TextView tvResult;
    //TODO реализовать возможность выбора условий приведения
    private static final double TEMP_PRIVEDENIYA = 20;
    private static final double PRES_PRIVEDNIYA = 760;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calc_gas_count);

        etTemp = (EditText) findViewById(R.id.etTempConVol);
        etPres = (EditText) findViewById(R.id.etPresConVol);
        etDens = (EditText) findViewById(R.id.etDensGC);
        etAtmPres = (EditText) findViewById(R.id.etAtmPresConVol);
        etLength = (EditText) findViewById(R.id.etLengGC);
        etDiameter = (EditText) findViewById(R.id.etDiamGC);
        tvResult = (TextView) findViewById(R.id.tvResultGasCount);
    }

    public void calcGasCount(View view) {

        Verifier.isCheck = true;

        String txtTemp = etTemp.getText().toString();
        String txtPres = etPres.getText().toString();
        String txtDens = etDens.getText().toString();
        String txtAtmPres = etAtmPres.getText().toString();
        String txtDiameter = etDiameter.getText().toString();
        String txtLength = etLength.getText().toString();

        double dens = Verifier.checkDensity(txtDens);
        double pres = Verifier.checkPressure(txtPres);
        double temp = Verifier.checkTemperature(txtTemp);
        double atmPres = Verifier.checkAtmPressure(txtAtmPres);
        double diameter = Verifier.checkDiameter(txtDiameter);
        double length = Verifier.checkLength(txtLength);
        Utils.makeToast(this);

        if (Verifier.isCheck) {

            double v = (Math.PI * diameter * diameter) / 4 * length;// геометрический объем газопровода
            double patm = atmPres * 0.001359511;// атмосферное давление из мм рт. ст. в килограммы
            double pabs = pres + patm;// абсолютное давление
            temp = temp + 273.15;// температура по Кельвину
            double z = BaseCalc.calcZ(temp, pabs, dens);// расчет коэффициента сжимаемости
            double privTemp = TEMP_PRIVEDENIYA + 273.15; //пересчет температуры приведения в Кельвины
            double privPres = PRES_PRIVEDNIYA * 0.001359511; // пересчет давления приведения из мм.рт.ст. в килограммы

            double result = BaseCalc.calcCountGas(v, pabs, privPres, temp, privTemp, z);
            tvResult.setText("" + result);

        } else tvResult.setText("" + 0.000);
    }
}
