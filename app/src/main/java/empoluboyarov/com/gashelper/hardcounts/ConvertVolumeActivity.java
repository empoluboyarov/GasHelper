package empoluboyarov.com.gashelper.hardcounts;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import empoluboyarov.com.gashelper.R;
import empoluboyarov.com.gashelper.core.BaseCalc;
import empoluboyarov.com.gashelper.core.Utils;
import empoluboyarov.com.gashelper.core.Verifier;

public class ConvertVolumeActivity extends AppCompatActivity {

    private EditText etTn, etPn, etRo, etPrt, etVg;
    private TextView tvResult;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_convert_volume);

        etTn = (EditText) findViewById(R.id.etTnCV);
        etPn = (EditText) findViewById(R.id.etPnCV);
        etRo = (EditText) findViewById(R.id.etRoCV);
        etPrt = (EditText) findViewById(R.id.etPrtCV);
        etVg = (EditText) findViewById(R.id.etVgCV);
        tvResult = (TextView) findViewById(R.id.tvResultCV);
    }

    public void convertVolume(View view) {
        Verifier.isCheck = true;

        Utils.ro = Verifier.checkDensity(etRo.getText().toString());
        Utils.pn = Verifier.checkPressure(etPn.getText().toString());
        Utils.prt = Verifier.checkAtmPressure(etPrt.getText().toString());
        Utils.tn = Verifier.checkTemperature(etTn.getText().toString());
        Utils.vg = Verifier.checkVolume(etVg.getText().toString());
        Utils.privp = Utils.PRES_PRIVEDNIYA;
        Utils.privt = Utils.TEMP_PRIVEDENIYA;

        Utils.makeToast(this);

        if (Verifier.isCheck) {

            double patm = Utils.prt * 0.001359511;
            double pabs = Utils.pn + patm;
            Utils.privp = Utils.privp * 0.001359511;
            Utils.tn = Utils.tn + 273.15;
            Utils.privt = Utils.privt + 273.15;
            Utils.z = BaseCalc.calcZ(Utils.tn, pabs, Utils.ro);
            Utils.stock = BaseCalc.calcCountGas(Utils.vg, pabs, Utils.privp, Utils.tn, Utils.privt, Utils.z);
            tvResult.setText("Объем газа по условиям приведения: " + Utils.stock + " тыс.м3");
        } else tvResult.setText("" + 0.000);
    }
}
