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

    private EditText etTn, etPn, etRo, etPrt, etAzot;
    private TextView tvResult;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calc_adiabata);

        etTn = (EditText) findViewById(R.id.etTnAd);
        etPn = (EditText) findViewById(R.id.etPnAd);
        etRo = (EditText) findViewById(R.id.etRoAd);
        etPrt = (EditText) findViewById(R.id.etPrtAd);
        etAzot = (EditText) findViewById(R.id.etAzotAd);
        tvResult = (TextView) findViewById(R.id.tvResultAd);
    }

    public void calcAdiabat(View view) {

        Verifier.isCheck = true;
        Verifier.message = null;

        Utils.ro = Verifier.checkDensity(etRo.getText().toString());
        Utils.pn = Verifier.checkPressure(etPn.getText().toString());
        Utils.tn = Verifier.checkTemperature(etTn.getText().toString());
        Utils.prt = Verifier.checkAtmPressure(etPrt.getText().toString());
        Utils.azot = Verifier.checkNitrogen(etAzot.getText().toString());

        Utils.makeToast(this);

        if (Verifier.isCheck) {
            double patm = Utils.prt * 0.001359511;
            double pnabs = Utils.pn + patm;
            Utils.tn = Utils.tn + 273.15;

            double result = BaseCalc.calcAdiabata(Utils.tn, pnabs, Utils.ro, Utils.azot);
            tvResult.setText("" + result);
        } else tvResult.setText("" + 0.000);
    }
}
