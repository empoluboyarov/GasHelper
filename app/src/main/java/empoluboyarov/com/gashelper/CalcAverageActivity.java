package empoluboyarov.com.gashelper;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import empoluboyarov.com.gashelper.core.BaseCalc;
import empoluboyarov.com.gashelper.core.Utils;
import empoluboyarov.com.gashelper.core.Verifier;

public class CalcAverageActivity extends AppCompatActivity {

    private EditText etRo, etPn, etPk, etPrt, etTn, etTk, etTgr, etQf, etL, etD;
    private TextView tvResult;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calc_average);

        etRo = (EditText)findViewById(R.id.etRoAv);
        etPn = (EditText)findViewById(R.id.etPnAv);
        etPk = (EditText)findViewById(R.id.etPkAv);
        etPrt = (EditText)findViewById(R.id.etPrtAv);
        etTn = (EditText)findViewById(R.id.etTnAv);
        etTk = (EditText)findViewById(R.id.etTkAv);
        etTgr = (EditText)findViewById(R.id.etTgrAv);
        etQf = (EditText)findViewById(R.id.etQfAv);
        etL = (EditText)findViewById(R.id.etLAv);
        etD = (EditText)findViewById(R.id.etDAv);

        tvResult = (TextView) findViewById(R.id.tvResultAv);
    }

    public void calcAverage(View view) {
        Verifier.isCheck = true;

        String txtRo = etRo.getText().toString();
        String txtPn = etPn.getText().toString();
        String txtPk = etPk.getText().toString();
        String txtPrt = etPrt.getText().toString();
        String txtTn = etTn.getText().toString();
        String txtTk = etTk.getText().toString();
        String txtTgr = etTgr.getText().toString();
        String txtQf = etQf.getText().toString();
        String txtL = etL.getText().toString();
        String txtD = etD.getText().toString();


        Utils.ro = Verifier.checkDensity(txtRo);
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

        Utils.makeToast(this);

        
        if (Verifier.isCheck) {
            double patm = Utils.prt * 0.001359511;
            double pnabs = Utils.pn + patm;
            double pkabs = Utils.pk + patm;
            Utils.privp = Utils.privp * 0.001359511;
            Utils.tn = Utils.tn + 273.15;
            Utils.tk = Utils.tk + 273.15;
            Utils.tgr = Utils.tgr + 273.15;
            Utils.privt = Utils.privt + 273.15;
//
            double delta = BaseCalc.calcDelta(Utils.ro);
//
            if (Utils.dmm < 800 && Utils.dmm > 200)
                Utils.dmm = Utils.dmm + 20;
            else if (Utils.dmm > 800)
                Utils.dmm = Utils.dmm + 40;
            else if (Utils.dmm < 200)
                Utils.dmm = Utils.dmm + 10;
//
            Utils.psr = BaseCalc.calcAverPres(pnabs, pkabs);
            Utils.tsr = BaseCalc.calcAverTempSimple(1, Utils.tgr, Utils.tn, Utils.tk);
// первое приближение
            double cp = BaseCalc.calcCp(Utils.tsr, Utils.psr);
            double di = BaseCalc.calcDi(Utils.tsr, cp);
            double ax = BaseCalc.calcAx(1, Utils.dmm, Utils.lkm, Utils.qf, delta, cp);
            double eax = BaseCalc.calcEax(Utils.tn, Utils.tk, Utils.tgr, pnabs, pkabs, Utils.psr, ax, di);
            double km = BaseCalc.calcKmDross(eax, Utils.qf, delta, cp, Utils.dmm, Utils.lkm);
            ax = BaseCalc.calcAx(km, Utils.dmm, Utils.lkm, Utils.qf, delta, cp);
            Utils.tsr = BaseCalc.calcAverTem(ax, Utils.tgr, Utils.tn, Utils.tk);
// второе приближение
            cp = BaseCalc.calcCp(Utils.tsr, Utils.psr);
            di = BaseCalc.calcDi(Utils.tsr, cp);
            ax = BaseCalc.calcAx(km, Utils.dmm, Utils.lkm, Utils.qf, delta, cp);
            eax = BaseCalc.calcEax(Utils.tn, Utils.tk, Utils.tgr, pnabs, pkabs, Utils.psr, ax, di);
            km = BaseCalc.calcKmDross(eax, Utils.qf, delta, cp, Utils.dmm, Utils.lkm);
            ax = BaseCalc.calcAx(km, Utils.dmm, Utils.lkm, Utils.qf, delta, cp);
//
            Utils.tsr = BaseCalc.calcAverTem(ax, Utils.tgr, Utils.tn, Utils.tk) - 273.15;
//
            Utils.psr = Utils.psr - patm;

            tvResult.setText("Среднее избыточное давление газа " + Utils.psr + "  кгс/см2\n"
                    + "Средняя температура газа: " + Utils.tsr + "  по Цельсию");
        } else tvResult.setText("" + 0.000);
    }
}
