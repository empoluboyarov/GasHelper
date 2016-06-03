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

    private EditText etTn, etPn, etRo, etPrt;
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


        etTn = (EditText) findViewById(R.id.etTnZ);
        etPn = (EditText) findViewById(R.id.etPnZ);
        etRo = (EditText) findViewById(R.id.etRoZ);
        etPrt = (EditText) findViewById(R.id.etPrtZ);
        tvResult = (TextView) findViewById(R.id.tvResultZ);
    }

    public void calcZ(View view) {

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

            double result = BaseCalc.calcZ(Utils.tn, pnabs, Utils.ro);
            tvResult.setText("" + result);
        } else tvResult.setText("" + 0.000);
    }
}
