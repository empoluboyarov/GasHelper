package empoluboyarov.com.gashelper;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import empoluboyarov.com.gashelper.core.BaseCalc;
import empoluboyarov.com.gashelper.core.Utils;
import empoluboyarov.com.gashelper.core.Verifier;

public class CalcEffectivActivity extends AppCompatActivity {

    private EditText etRo, etAzot, etPn, etPk, etPrt, etTn, etTk, etTgr, etQf,
            etL, etD, etHn, etHk;
    private TextView tvResult;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calc_effectiv);

        etRo = (EditText)findViewById(R.id.etRoProd);
        etAzot = (EditText)findViewById(R.id.etAzotEf);
        etPn = (EditText)findViewById(R.id.etPnProp);
        etPk = (EditText)findViewById(R.id.etPkProp);
        etPrt = (EditText)findViewById(R.id.etPrtProp);
        etTn = (EditText)findViewById(R.id.etTnProp);
        etTk = (EditText)findViewById(R.id.etTkProp);
        etTgr = (EditText)findViewById(R.id.etTgrProp);
        etQf = (EditText)findViewById(R.id.etHydroProp);
        etL = (EditText)findViewById(R.id.etLProp);
        etD = (EditText)findViewById(R.id.etTimProd);
        etHn = (EditText)findViewById(R.id.etHnEf);
        etHk = (EditText)findViewById(R.id.etHkEf);

        tvResult = (TextView) findViewById(R.id.tvResultEf);
    }

    public void calcEffectiv(View view) {

        boolean critMode = false;
        Verifier.isCheck = true;

        String txtRo = etRo.getText().toString();
        String txtAzot = etAzot.getText().toString();
        String txtPn = etPn.getText().toString();
        String txtPk = etPk.getText().toString();
        String txtPrt = etPrt.getText().toString();
        String txtTn = etTn.getText().toString();
        String txtTk = etTk.getText().toString();
        String txtTgr = etTgr.getText().toString();
        String txtQf = etQf.getText().toString();
        String txtL = etL.getText().toString();
        String txtD = etD.getText().toString();
        String txtHn = etHn.getText().toString();
        String txtHk = etHk.getText().toString();


        Utils.ro = Verifier.checkDensity(txtRo);
        Utils.azot = Verifier.checkNitrogen(txtAzot);
        Utils.pn = Verifier.checkPressure(txtPn);
        Utils.pk = Verifier.checkPressure(txtPk);
        Utils.prt = Verifier.checkAtmPressure(txtPrt);
        Verifier.checkDeltaPres(Utils.pn, Utils.pk);
        Utils.tn = Verifier.checkTemperature(txtTn);
        Utils.tk = Verifier.checkTemperature(txtTk);
        Verifier.checkDeltaTemp(Utils.tn, Utils.tk);
        Utils.tgr = Verifier.checkTemperature(txtTgr);
        Utils.qf = Verifier.checkGasTransport(txtQf);
        Utils.lkm = Verifier.checkLength(txtL);
        Utils.dmm = Verifier.checkDiameter(txtD);
        Utils.h1 = Verifier.checkVisota(txtHn);
        Utils.h2 = Verifier.checkVisota(txtHk);

        Utils.makeToast(this);


        if (Verifier.isCheck) {
            double patm = Utils.prt * 0.001359511;
            double pkabs = Utils.pk + patm;
            Utils.tn = Utils.tn + 273.15;
            Utils.tk = Utils.tk + 273.15;
            Utils.tgr = Utils.tgr + 273.15;
//
            double pnabs = Utils.pn + patm;
            double dm = Utils.dmm / 1000.0;
            double qday = Utils.qf;
//
            Utils.qth = BaseCalc.calcQTheor(Utils.ro, Utils.pn, Utils.pk, Utils.prt, Utils.tn, Utils.tk,
                    Utils.tgr, Utils.lkm, Utils.dmm, Utils.h1, Utils.h2);
            Utils.expr = BaseCalc.calcExpCritNew(Utils.azot, Utils.ro, Utils.dmm / 1000.0,
                    pnabs, pkabs, Utils.tn, Utils.tk, 1 * 60 * 60);
            Utils.expr = Utils.expr / 1000.0 / 1000.0 * 24;
//
            if (Utils.qth > Utils.expr) {
                Utils.qth = Utils.expr;
                critMode = true;
            }
//
            Utils.qth = Utils.qth / 24.0 * 1000;
            Utils.hydro = (Utils.qf * 1000 / 24.0) / Utils.qth;
            if (Utils.hydro > 1) Utils.hydro = 1;
// upstream
            Utils.z = BaseCalc.calcZ(Utils.tn, pnabs, Utils.ro);
            double qmin = 2.45 * qday * Utils.z * Utils.tn / pnabs;
            double s = 3.14159 * dm * dm / 4.0;
            double v = qmin / s;
            Utils.upspeed = v / 60.0;
// downstream
            Utils.z = BaseCalc.calcZ(Utils.tk, pkabs, Utils.ro);
            qmin = 2.45 * qday * Utils.z * Utils.tk / pkabs;
            s = 3.14159 * dm * dm / 4.0;
            v = qmin / s;
            Utils.downspeed = v / 60.0;

            if (!critMode) {
                Verifier.message = "Коэффициент гидравлической эффективности: " + Utils.hydro + "\n" +
                        "при теоретическом объеме транспорта газа: " + Utils.qth + " тыс.м3/час\n" +
                        "Линейная скорость потока газа:  \n" +
                        "В начале газопровода: " + Utils.upspeed + " м/сек\n" +
                        "В конце газопровода: " + Utils.downspeed + " м/сек";
            } else
                Verifier.message = "Коэффициент гидравлической эффективности: " + Utils.hydro + "\n" +
                        "при теоретическом объеме транспорта газа: " + Utils.qth + " тыс.м3/час\n" +
                        "Внимание! Режим критического истечения газа в атмосферу!\n" +
                        "Линейная скорость потока газа:  \n" +
                        "В начале газопровода: " + Utils.upspeed + " м/сек\n" +
                        "В конце газопровода: " + Utils.downspeed + " м/сек";
            tvResult.setText("");
            Utils.makeToast(this);

        } else tvResult.setText("" + 0.000);
    }
}

