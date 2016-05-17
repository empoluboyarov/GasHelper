package empoluboyarov.com.gashelper.core;

/**
 * Created by Evgeniy on 17.05.2016.
 */
public class TestClass {
    public static void main(String[] args) {
        Verifier.isCheck = true;

        String txtTempStart = "30";
        String txtDens = "0.6";
        String txtAtmPres = "750";
        String txtTempFin = "20";
        String txtPresStart = "50";
        String txtPresFin = "30";
        String txtGasTransport = "1250";
        String txtLength = "55";
        String txtDiamet = "1200";
        String txtTempGrunt = "5";

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


        if (Verifier.isCheck) {

            double[] apx = new double[500];
            double[] atx = new double[500];
            double[] astock = new double[500];

            double patm = Utils.prt * 0.001359511;
            double pnabs = Utils.pn + patm;
            double pkabs = Utils.pk + patm;
            Utils.privp = 760 * 0.001359511;
            Utils.privt = 20 + 273.15;
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
            Utils.stock = Utils.stock + astock[498];

            Utils.vg = (3.141593 * Utils.dm * Utils.dm) / 4 * Utils.lm;
            double result = Utils.stock;
            System.out.println(result);
            System.out.println(Utils.vg);
        }
    }


}

