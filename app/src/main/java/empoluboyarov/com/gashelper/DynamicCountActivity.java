package empoluboyarov.com.gashelper;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import empoluboyarov.com.gashelper.core.BaseCalc;
import empoluboyarov.com.gashelper.core.Utils;
import empoluboyarov.com.gashelper.core.Verifier;

public class DynamicCountActivity extends AppCompatActivity {

    private EditText etTempStart, etTempFin, etTempGrunta, etGasTransport, etLength, etDiamet,
            etPresStart, etPresFin, etDens, etAtmPres;
    private TextView tvResult;

    private static final double TEMP_PRIVEDENIYA = 20;
    private static final double PRES_PRIVEDNIYA = 760;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dynamic_count);

        etDens = (EditText) findViewById(R.id.etRoProd);
        etAtmPres = (EditText) findViewById(R.id.etPrtProp);
        etTempStart = (EditText) findViewById(R.id.etTnProp);
        etTempFin = (EditText) findViewById(R.id.etTkProp);
        etTempGrunta = (EditText) findViewById(R.id.etTgrProp);
        etPresStart = (EditText) findViewById(R.id.etPnProp);
        etPresFin = (EditText) findViewById(R.id.etPkProp);
        etGasTransport = (EditText) findViewById(R.id.etHydroProp);
        etLength = (EditText) findViewById(R.id.etLAv);
        etDiamet = (EditText) findViewById(R.id.etTimProd);
        tvResult = (TextView) findViewById(R.id.tvResultAv);

    }

    public void calcDC(View view) {

        Verifier.isCheck = true;

        String txtTempStart = etTempStart.getText().toString();
        String txtDens = etDens.getText().toString();
        String txtAtmPres = etAtmPres.getText().toString();
        String txtTempFin = etTempFin.getText().toString();
        String txtPresStart = etPresStart.getText().toString();
        String txtPresFin = etPresFin.getText().toString();
        String txtGasTransport = etGasTransport.getText().toString();
        String txtLength = etLength.getText().toString();
        String txtDiamet = etDiamet.getText().toString();
        String txtTempGrunt = etTempGrunta.getText().toString();

        Utils.pn = Verifier.checkPressure(txtPresStart);
        Utils.pk = Verifier.checkPressure(txtPresFin);
        Verifier.checkDeltaPres(Utils.pn, Utils.pk);
        Utils.prt = Verifier.checkAtmPressure(txtAtmPres);
        Utils.ro = Verifier.checkDensity(txtDens);
        Utils.tn = Verifier.checkTemperature(txtTempStart);
        Utils.tk = Verifier.checkTemperature(txtTempFin);
        Verifier.checkDeltaTemp(Utils.tn, Utils.tk);
        Utils.tgr = Verifier.checkTemperature(txtTempGrunt);
        Utils.dmm = Verifier.checkDiameter(txtDiamet);
        Utils.lkm = Verifier.checkLength(txtLength);
        Utils.qf = Verifier.checkGasTransport(txtGasTransport);
        Utils.makeToast(this);

        if (Verifier.isCheck) {

            double[] apx = new double[500];
            double[] atx = new double[500];
            double[] astock = new double[500];

            double patm = Utils.prt * 0.001359511;
            double pnabs = Utils.pn + patm;
            double pkabs = Utils.pk + patm;
            Utils.privp = PRES_PRIVEDNIYA * 0.001359511;
            Utils.privt = TEMP_PRIVEDENIYA + 273.15;
            Utils.tn = Utils.tn + 273.15;
            Utils.tk = Utils.tk + 273.15;
            Utils.tgr = Utils.tgr + 273.15;


            double delta = BaseCalc.calcDelta(Utils.ro);
            // примерный наружный диаметр
            // (для расчета коэффициента теплопередачи):
            if (Utils.dmm > 800) Utils.dmm += 40;
            else if (Utils.dmm < 200) Utils.dmm += 10;
            else if (Utils.dmm >= 200 && Utils.dmm <= 800) Utils.dmm += 20;

            Utils.psr = BaseCalc.calcAverPres(pnabs, pkabs);// среднее давление
            Utils.tsr = BaseCalc.calcAverTempSimple(1, Utils.tgr, Utils.tn, Utils.tk);// средняя температура грубо
            // первое приближение
            double cp = BaseCalc.calcCp(Utils.tsr, Utils.psr);// теплоемкость
            double di = BaseCalc.calcDi(Utils.tsr, cp);// коэффициент Джоуля-Томсона
            double ax = BaseCalc.calcAx(1, Utils.dmm, Utils.lkm, Utils.qf, delta, cp);
            double eax = BaseCalc.calcEax(Utils.tn, Utils.tk, Utils.tgr, pnabs, pkabs, Utils.psr, ax, di);
            double km = BaseCalc.calcKmDross(eax, Utils.qf, delta, cp, Utils.dmm, Utils.lkm);// коэффициент теплопередачи
            ax = BaseCalc.calcAx(km, Utils.dmm, Utils.lkm, Utils.qf, delta, cp);
            Utils.tsr = BaseCalc.calcAverTem(ax, Utils.tgr, Utils.tn, Utils.tk);// средняя температура точно
            // второе приближение
            cp = BaseCalc.calcCp(Utils.tsr, Utils.psr);
            di = BaseCalc.calcDi(Utils.tsr, cp);
            ax = BaseCalc.calcAx(km, Utils.dmm, Utils.lkm, Utils.qf, delta, cp);
            eax = BaseCalc.calcEax(Utils.tn, Utils.tk, Utils.tgr, pnabs, pkabs, Utils.psr, ax, di);
            km = BaseCalc.calcKmDross(eax, Utils.qf, delta, cp, Utils.dmm, Utils.lkm);
            ax = BaseCalc.calcAx(km, Utils.dmm, Utils.lkm, Utils.qf, delta, cp);
            Utils.tsr = BaseCalc.calcAverTem(ax, Utils.tgr, Utils.tn, Utils.tk);

            // коэффициент коррекции для температура
            // в произвольной точке МГ:
            double t1 = Utils.tgr + (Utils.tn - Utils.tgr) * Math.exp(-ax);
            double t2 = di * (pnabs * pnabs - pkabs * pkabs) / (2 * ax * Utils.psr) * (1 - Math.exp(-ax));
            double tx = t1 - t2;
            double dtk = Utils.tk - tx;// коэффициент коррекции
            apx[0] = pnabs;
            atx[0] = Utils.tn;
            double dlkm = Utils.lkm / 500;
            // Делим газопровод на 500 участков,
            // для каждого выполняется расчет запаса газа
            for (int x = 1; x < 500; x++) {
                apx[x] = Math.sqrt(pnabs * pnabs - (pnabs * pnabs - pkabs * pkabs) * (dlkm * x) / Utils.lkm);
                ax = BaseCalc.calcAx(km, Utils.dmm, dlkm * x, Utils.qf, delta, cp);
                t1 = Utils.tgr + (Utils.tn - Utils.tgr) * Math.exp(-ax);
                t2 = di * (pnabs * pnabs - pkabs * pkabs) / (2 * ax * Utils.psr * Utils.lkm / (dlkm * x)) * (1 - Math.exp(-ax));
                tx = t1 - t2;
                atx[x] = tx + dtk * (dlkm * x) / Utils.lkm;
                //
                Utils.psr = BaseCalc.calcAverPres(apx[x - 1], apx[x]);
                Utils.tsr = BaseCalc.calcAverTem(ax, Utils.tgr, atx[x - 1], atx[x]);
                //
                Utils.z = BaseCalc.calcZ(Utils.tsr, Utils.psr, Utils.ro);
                double s = (3.141593 * Utils.dm * Utils.dm) / 4;
                double v = s * (dlkm * 1000);
                double gcount = BaseCalc.calcCountGas(v, Utils.psr, Utils.privp, Utils.tsr, Utils.privt, Utils.z);
                astock[x - 1] = gcount;
            }


            //
            Utils.stock = 0;
            for (int x = 0; x < 500; x++) {
                Utils.stock = Utils.stock + astock[x];
            }
            Utils.stock += astock[498];

            Utils.vg = (3.141593 * Utils.dm * Utils.dm) / 4 * Utils.lm;

            double result = Utils.stock;
            tvResult.setText("" + result);
        } else tvResult.setText("" + 0.000);
    }
}
