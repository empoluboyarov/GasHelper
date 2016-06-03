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

public class CalcFactDensActivity extends AppCompatActivity {

    private EditText etTn, etPn, etRo, etPrt;
    private TextView tvResult;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calc_fact_dens);

        etTn = (EditText) findViewById(R.id.etTnFD);
        etPn = (EditText) findViewById(R.id.etPnFD);
        etRo = (EditText) findViewById(R.id.etRoFD);
        etPrt = (EditText) findViewById(R.id.etPrtFD);
        tvResult = (TextView) findViewById(R.id.tvResultFD);
    }

    public void calcFactDensity(View view) {

        Verifier.isCheck = true;

        String txtTn = etTn.getText().toString();
        String txtPn = etPn.getText().toString();
        String txtRo = etRo.getText().toString();
        String txtPrt = etPrt.getText().toString();


        Utils.ro = Verifier.checkDensity(txtRo);
        Utils.pn = Verifier.checkPressure(txtPn);
        Utils.tn = Verifier.checkTemperature(txtTn);
        Utils.prt = Verifier.checkAtmPressure(txtPrt);

        Utils.makeToast(this);

        if (Verifier.isCheck) {

            double patm = Utils.prt * 0.001359511;
            double pnabs = Utils.pn + patm;
            Utils.tn = Utils.tn + 273.15;

            double result = BaseCalc.calcRoFact(Utils.ro, pnabs, Utils.tn, Utils.z);
            tvResult.setText("" + result);
        } else tvResult.setText("" + 0.000);
    }
}
