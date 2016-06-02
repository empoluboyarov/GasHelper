package empoluboyarov.com.gashelper;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import empoluboyarov.com.gashelper.core.BaseCalc;
import empoluboyarov.com.gashelper.core.Utils;
import empoluboyarov.com.gashelper.core.Verifier;

public class CalcProduvkaActivity extends AppCompatActivity {

    private EditText etRo, etAzot, etPn, etPrt, etTn, etTgr,
            etL, etD, etDk, etTim;
    private TextView tvResult;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calc_produvka);

        etRo = (EditText) findViewById(R.id.etRoProd);
        etAzot = (EditText) findViewById(R.id.etPnProp);
        etPn = (EditText) findViewById(R.id.etPkProp);
        etPrt = (EditText) findViewById(R.id.etPrtProp);
        etTn = (EditText) findViewById(R.id.etTnProp);
        etTgr = (EditText) findViewById(R.id.etTkProp);
        etL = (EditText) findViewById(R.id.etTgrProp);
        etD = (EditText) findViewById(R.id.etHydroProp);
        etDk = (EditText) findViewById(R.id.etLProp);
        etTim = (EditText) findViewById(R.id.etTimProd);

        tvResult = (TextView) findViewById(R.id.tvResultProd);
    }

    public void calcProduvka(View view){
        String flowMode = null;
        String result = null;
        Verifier.isCheck = true;

        String txtRo = etRo.getText().toString();
        String txtAzot = etAzot.getText().toString();
        String txtPn = etPn.getText().toString();
        String txtPrt = etPrt.getText().toString();
        String txtTn = etTn.getText().toString();
        String txtTgr = etTgr.getText().toString();
        String txtL = etL.getText().toString();
        String txtD = etD.getText().toString();
        String txtDk = etDk.getText().toString();
        String txtTim = etTim.getText().toString();


        Utils.ro = Verifier.checkDensity(txtRo);
        Utils.azot = Verifier.checkNitrogen(txtAzot);
        Utils.pn = Verifier.checkPressure(txtPn);
        Utils.prt = Verifier.checkAtmPressure(txtPrt);
        Utils.tn = Verifier.checkTemperature(txtTn);
        Utils.tgr = Verifier.checkTemperature(txtTgr);
        Utils.tim = Verifier.checkFlowTime(txtTim);
        Utils.dmm = Verifier.checkSmallDiameter(txtD);
        Utils.dkmm = Verifier.checkSmallDiameter(txtDk);
        Utils.lm = Verifier.checkSmallLength(txtL);

        Utils.makeToast(this);

        if (Verifier.isCheck) {
            double patm = Utils.prt * 0.001359511;
            double pnabs = Utils.pn + patm;
            double pkabs = patm;
            Utils.tn = Utils.tn + 273.15;
            Utils.tk = Utils.tn - 1;
            Utils.tgr = Utils.tgr + 273.15;

            Utils.privp = Utils.privp * 0.001359511;
            Utils.privt = Utils.privt + 273.15;

            Utils.lkm = Utils.lm / 1000;
            double dexpir = 0.0;
            if (Utils.dkmm < Utils.dmm) dexpir = Utils.dkmm;
            else dexpir = Utils.dmm;

            dexpir = dexpir / 1000;

            double z = BaseCalc.calcZ(Utils.tn, pnabs, Utils.ro);
            double adiabata = BaseCalc.calcAdiabata(Utils.tn, pnabs, Utils.ro, Utils.azot);
            double rofact = BaseCalc.calcRoFact(Utils.ro, pnabs, Utils.tn, z);// фактическая плотность газа
            double speedsound = BaseCalc.calcSpeedSound(Utils.tn, adiabata, z, Utils.ro);// скорость звука в газе
            double speedgas = BaseCalc.calcSpeedGas(adiabata, rofact, pnabs, pkabs);// скорость истечения газа

            if (speedsound > speedgas) {
                double s = 3.141593 * dexpir * dexpir / 4;
                Utils.expr = BaseCalc.calcExpNoCrit(s, Utils.tim, pnabs, pkabs);
                flowMode = "некритический";
            } else {
                Utils.expr = BaseCalc.calcExpCritNew(Utils.azot, Utils.ro, dexpir, pnabs, pkabs, Utils.tn, Utils.tk, Utils.tim);
                flowMode = "критический";
            }
            System.out.println(Utils.expr);
            System.out.println(flowMode);

// пропускная способность свечной линии:
            Utils.qth = BaseCalc.calcQTheor(Utils.ro, Utils.pn, 0, Utils.prt, Utils.tn, Utils.tk, Utils.tgr, Utils.lkm, Utils.dmm, 0, 0);
            System.out.println(Utils.qth);
            Utils.qth = Utils.qth * 1E6 / 24.0 / 60.0 / 60.0 * Utils.tim;
            System.out.println(Utils.qth);
// если имеется влияние гидравлического сопротивления свечной линии:
            if (Utils.qth < Utils.expr) {
                Utils.expr = Utils.qth;
                flowMode = "определен пропускной способностью свечной линии.";
            }

            if (Utils.PRES_PRIVEDNIYA != 760 && Utils.TEMP_PRIVEDENIYA != 20)
                Utils.expr = BaseCalc.calcCountGas(Utils.expr, 760 * 0.001359511, Utils.privp, 293.15, Utils.privt, 1);

            result = Utils.expr + " м.куб";
            if (Utils.expr > 1000) result = Utils.expr / 1000 + " тыс.м.куб";
            if (result != null) {
                Verifier.message = "Объем стравленного газа: " + result + "\n"+
                        "Режим истечения газа: " + flowMode;
            } else Verifier.message = "Расчет не выполнен";

            tvResult.setText("");
            Utils.makeToast(this);

        } else tvResult.setText("" + 0.000);
    }
}
