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

    private EditText etTn, etPn, etRo, etPrt, etAzot;
    private TextView tvResult;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calc_speed_gas);

        etTn = (EditText) findViewById(R.id.etTnSG);
        etPn = (EditText) findViewById(R.id.etPnSG);
        etRo = (EditText) findViewById(R.id.etRoSG);
        etPrt = (EditText) findViewById(R.id.etPrtSG);
        etAzot = (EditText) findViewById(R.id.etAzotSG);
        tvResult = (TextView) findViewById(R.id.tvResultSG);

    }

    public void calcSpeedGas(View view) {

        Verifier.isCheck = true;
        Verifier.message = null;

        Utils.ro = Verifier.checkDensity(etRo.getText().toString());
        Utils.pn = Verifier.checkPressure(etPn.getText().toString());
        Utils.tn = Verifier.checkTemperature(etTn.getText().toString());
        Utils.prt = Verifier.checkAtmPressure(etPrt.getText().toString());
        Utils.azot = Verifier.checkNitrogen(etAzot.getText().toString());

        Utils.makeToast(this);

        if (Verifier.isCheck){
            double patm = Utils.prt * 0.001359511;
            double pnabs = Utils.pn + patm;
            Utils.tn = Utils.tn + 273.15;
            Utils.z = BaseCalc.calcZ(Utils.tn, pnabs, Utils.ro);
            Utils.adiabata = BaseCalc.calcAdiabata(Utils.tn, pnabs, Utils.ro, Utils.azot);
            Utils.rofact = BaseCalc.calcRoFact(Utils.ro, pnabs, Utils.tn, Utils.z);
            Utils.speedgas = BaseCalc.calcSpeedGas(Utils.adiabata, Utils.rofact, pnabs, patm);
            tvResult.setText("" + Utils.speedgas);
        } else tvResult.setText("" + 0.000);

    }
}
