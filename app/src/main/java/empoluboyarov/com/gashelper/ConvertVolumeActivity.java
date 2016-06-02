package empoluboyarov.com.gashelper;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import empoluboyarov.com.gashelper.core.BaseCalc;
import empoluboyarov.com.gashelper.core.Utils;
import empoluboyarov.com.gashelper.core.Verifier;

public class ConvertVolumeActivity extends AppCompatActivity {

    private EditText etTemp, etPres, etDens, etAtmPres, etVolume;
    private TextView tvResult;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_convert_volume);

        etTemp = (EditText) findViewById(R.id.etTempConVol);
        etPres = (EditText) findViewById(R.id.etPresConVol);
        etDens = (EditText) findViewById(R.id.etDensConVol);
        etAtmPres = (EditText) findViewById(R.id.etAtmPresConVol);
        etVolume = (EditText) findViewById(R.id.etVolumeConVol);
        tvResult = (TextView) findViewById(R.id.tvResultConVol);
    }

    public void convertVolume(View view) {
        Verifier.isCheck = true;

        String txtRo = etDens.getText().toString();
        String txtPn = etPres.getText().toString();;
        String txtPrt = etAtmPres.getText().toString();;
        String txtTn = etTemp.getText().toString();;
        String txtVg = etVolume.getText().toString();;

        Utils.ro = Verifier.checkDensity(txtRo);
        Utils.pn = Verifier.checkPressure(txtPn);
        Utils.prt = Verifier.checkAtmPressure(txtPrt);
        Utils.tn = Verifier.checkTemperature(txtTn);
        Utils.vg = Verifier.checkVolume(txtVg);
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
