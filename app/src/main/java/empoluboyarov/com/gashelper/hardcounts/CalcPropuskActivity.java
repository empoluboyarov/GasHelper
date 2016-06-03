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

public class CalcPropuskActivity extends AppCompatActivity {

    private EditText etRo, etAzot, etPn, etPk, etPrt, etTn, etTk,
            etTgr, etHydro, etL, etD, etHn, etHk;
    private TextView tvResult;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calc_propusk);

        etRo = (EditText) findViewById(R.id.etRoProp);
        etAzot = (EditText) findViewById(R.id.etAzotProp);
        etPn = (EditText) findViewById(R.id.etPnProp);
        etPk = (EditText) findViewById(R.id.etPkProp);
        etPrt = (EditText) findViewById(R.id.etPrtProp);
        etTn = (EditText) findViewById(R.id.etTnProp);
        etTk = (EditText) findViewById(R.id.etTkProp);
        etTgr = (EditText) findViewById(R.id.etTgrProp);
        etHydro = (EditText) findViewById(R.id.etHydroProp);
        etL = (EditText) findViewById(R.id.etLProp);
        etD = (EditText) findViewById(R.id.etDProp);
        etHn = (EditText) findViewById(R.id.etHnProp);
        etHk = (EditText) findViewById(R.id.etHkProp);

        tvResult = (TextView) findViewById(R.id.tvResultProp);
    }

    public void calcPropusk(View view) {
        boolean critMode = false;
        Verifier.isCheck = true;

        Utils.ro = Verifier.checkDensity(etRo.getText().toString());
        Utils.azot = Verifier.checkNitrogen(etAzot.getText().toString());
        Utils.pn = Verifier.checkPressure(etPn.getText().toString());
        Utils.pk = Verifier.checkPressure(etPk.getText().toString());
        Utils.prt = Verifier.checkAtmPressure(etPrt.getText().toString());
        Verifier.checkDeltaPres(Utils.pn, Utils.pk);
        Utils.tn = Verifier.checkTemperature(etTn.getText().toString());
        Utils.tk = Verifier.checkTemperature(etTk.getText().toString());
        Verifier.checkDeltaTemp(Utils.tn, Utils.tk);
        Utils.tgr = Verifier.checkTemperature(etTgr.getText().toString());
        Utils.hydro = Verifier.checkHydro(etHydro.getText().toString());
        Utils.lkm = Verifier.checkLength(etL.getText().toString());
        Utils.dmm = Verifier.checkDiameter(etD.getText().toString());
        Utils.h1 = Verifier.checkVisota(etHn.getText().toString());
        Utils.h2 = Verifier.checkVisota(etHk.getText().toString());

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
//
            Utils.qth = BaseCalc.calcQTheor(Utils.ro, Utils.pn, Utils.pk, Utils.prt, Utils.tn, Utils.tk,
                    Utils.tgr, Utils.lkm, Utils.dmm, Utils.h1, Utils.h2);
            Utils.qth = Utils.qth * Utils.hydro;
            Utils.expr = BaseCalc.calcExpCritNew(Utils.azot, Utils.ro, Utils.dmm / 1000.0,
                    pnabs, pkabs, Utils.tn, Utils.tk, 1 * 60 * 60);
            Utils.expr = Utils.expr / 1000.0 / 1000.0 * 24;
// сверка с критическим истечением газа:
            if (Utils.qth > Utils.expr) {
                Utils.qth = Utils.expr;
                critMode = true;
            }
//
            double qday = Utils.qth;
// upstream
            Utils.z = BaseCalc.calcZ(Utils.tn, pnabs, Utils.ro);
            double qmin = 2.45 * qday * Utils.z * Utils.tn / pnabs;
            double s = 3.14159 * dm * dm / 4.0;
            double v = qmin / s;
            Utils.upspeed = v / 60.0;// линейная скорость газа в начале МГ
// downstream
            Utils.z = BaseCalc.calcZ(Utils.tk, pnabs, Utils.ro);

            qmin = 2.45 * qday * Utils.z * Utils.tk / pkabs;
            s = 3.14159 * dm * dm / 4.0;
            v = qmin / s;
            Utils.downspeed = v / 60.0;// линейная скорость газа в конце МГ

            if (!critMode) {
                Verifier.message = "Теоретический объем транспорта газа: " + Utils.qth / 24.0 * 1000 + " тыс. м3/час" + "\n" +
                        "при предположительном коэффициенте гидравлической эффективности: " + Utils.hydro + "\n" +
                        "Линейная скорость потока газа:  \n" +
                        "В начале газопровода: " + Utils.upspeed + " м/сек\n" +
                        "В конце газопровода: " + Utils.downspeed + " м/сек";
            } else
                Verifier.message = "Теоретический объем транспорта газа: " + Utils.qth / 24.0 * 1000 + " тыс. м3/час" + "\n" +
                        "при предположительном коэффициенте гидравлической эффективности: " + Utils.hydro + "\n" +
                        "Внимание! Режим критического истечения газа в атмосферу!\n" +
                        "Линейная скорость потока газа:  \n" +
                        "В начале газопровода: " + Utils.upspeed + " м/сек\n" +
                        "В конце газопровода: " + Utils.downspeed + " м/сек";
            tvResult.setText("");
            Utils.makeToast(this);

        } else tvResult.setText("" + 0.000);
    }
}
