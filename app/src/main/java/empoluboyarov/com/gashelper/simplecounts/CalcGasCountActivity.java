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

    private EditText etTn, etPn, etRo, etPrt, etL, etD;
    private TextView tvResult;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calc_gas_count);

        etTn = (EditText) findViewById(R.id.etTnGC);
        etPn = (EditText) findViewById(R.id.etPnGC);
        etRo = (EditText) findViewById(R.id.etRoGC);
        etPrt = (EditText) findViewById(R.id.etPrtGC);
        etL = (EditText) findViewById(R.id.etLGC);
        etD = (EditText) findViewById(R.id.etDGC);
        tvResult = (TextView) findViewById(R.id.tvResultGC);
    }

    public void calcGasCount(View view) {

        Verifier.isCheck = true;

        Utils.ro = Verifier.checkDensity(etRo.getText().toString());
        Utils.pn = Verifier.checkPressure(etPn.getText().toString());
        Utils.tn = Verifier.checkTemperature(etTn.getText().toString());
        Utils.prt = Verifier.checkAtmPressure(etPrt.getText().toString());
        Utils.dmm = Verifier.checkDiameter(etD.getText().toString());
        Utils.lkm = Verifier.checkLength(etL.getText().toString());

        Utils.makeToast(this);

        if (Verifier.isCheck) {

            Utils.vg = (Math.PI * Utils.dm * Utils.dm) / 4 * Utils.lm;// геометрический объем газопровода

            double patm = Utils.prt * 0.001359511;
            double pnabs = Utils.pn + patm;
            Utils.tn = Utils.tn + 273.15;

            Utils.z = BaseCalc.calcZ(Utils.tn, pnabs, Utils.ro);// расчет коэффициента сжимаемости
            Utils.privt = Utils.TEMP_PRIVEDENIYA + 273.15; //пересчет температуры приведения в Кельвины
            Utils.privp = Utils.PRES_PRIVEDNIYA * 0.001359511; // пересчет давления приведения из мм.рт.ст. в килограммы

            Utils.stock = BaseCalc.calcCountGas(Utils.vg, pnabs, Utils.privp, Utils.tn, Utils.privt, Utils.z);
            tvResult.setText("" + Utils.stock);

        } else tvResult.setText("" + 0.000);
    }
}
