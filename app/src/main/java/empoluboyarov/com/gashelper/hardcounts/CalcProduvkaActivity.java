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

public class CalcProduvkaActivity extends AppCompatActivity {

    private EditText etRo, etAzot, etPn, etPrt, etTn, etTgr,
            etL, etD, etDk, etTim;
    private TextView tvResult;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calc_produvka);

        etRo = (EditText) findViewById(R.id.etRoProd);
        etAzot = (EditText) findViewById(R.id.etAzotProd);
        etPn = (EditText) findViewById(R.id.etPnProd);
        etPrt = (EditText) findViewById(R.id.etPrtProd);
        etTn = (EditText) findViewById(R.id.etTnProd);
        etTgr = (EditText) findViewById(R.id.etTgrProd);
        etL = (EditText) findViewById(R.id.etLProd);
        etD = (EditText) findViewById(R.id.etDProd);
        etDk = (EditText) findViewById(R.id.etDkProd);
        etTim = (EditText) findViewById(R.id.etTimProd);

        tvResult = (TextView) findViewById(R.id.tvResultProd);
    }

    public void calcProduvka(View view){
        String flowMode = null;
        String result = null;
        Verifier.isCheck = true;

        Utils.ro = Verifier.checkDensity(etRo.getText().toString());
        Utils.azot = Verifier.checkNitrogen(etAzot.getText().toString());
        Utils.pn = Verifier.checkPressure(etPn.getText().toString());
        Utils.prt = Verifier.checkAtmPressure(etPrt.getText().toString());
        Utils.tn = Verifier.checkTemperature(etTn.getText().toString());
        Utils.tgr = Verifier.checkTemperature(etTgr.getText().toString());
        Utils.tim = Verifier.checkFlowTime(etTim.getText().toString());
        Utils.dmm = Verifier.checkSmallDiameter(etD.getText().toString());
        Utils.dkmm = Verifier.checkSmallDiameter(etDk.getText().toString());
        Utils.lm = Verifier.checkSmallLength(etL.getText().toString());

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
