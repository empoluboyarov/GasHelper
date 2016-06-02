package empoluboyarov.com.gashelper.core;

/**
 * Created by Evgeniy on 17.05.2016.
 */
public class TestClass {

    public static void main(String[] args) {
        //calc1(); //расчет расхода газа при продувке оборудования
        //calc2(); // расчет пропускной способности участка мг
        calc3(); // расчет гидравлической эффективности МГ
        //calc4(); //расчет среднего давления и средней температуры
        //calc5(); //пересчет объема газа для счетчика без корректора
    }

    private static void calc1() {

        //TODO перед импортом убрать не нужные System out println

        String flowMode = null;
        String result = null;
        Verifier.isCheck = true;

        String txtRo = "0.691";
        String txtAzot = "1.25";
        String txtPn = "51.3";
        String txtPrt = "745.1";
        String txtTn = "25.5";
        String txtTgr = "8.7";
        String txtL = "15.9";
        String txtD = "118";
        String txtDk = "118";
        String txtTim = "26";


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

        }
        if (result != null) {
            System.out.println("Объем стравленного газа: " + result);
            System.out.println("Режим истечения газа: " + flowMode);
        } else System.out.println("Расчет не выполнен");

    }

    private static void calc2() {
        boolean critMode = false;
        Verifier.isCheck = true;

        String txtRo = "0.691";
        String txtAzot = "1.25";
        String txtPn = "51.3";
        String txtPk = "46.9";
        String txtPrt = "751.1";
        String txtTn = "25.5";
        String txtTk = "15.1";
        String txtTgr = "8.7";
        String txtHydro = "0.96";
        String txtL = "55.155";
        String txtD = "1195";
        String txtHn = "155";
        String txtHk = "215";


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
        Utils.hydro = Verifier.checkHydro(txtHydro);
        Utils.lkm = Verifier.checkLength(txtL);
        Utils.dmm = Verifier.checkDiameter(txtD);
        Utils.h1 = Verifier.checkVisota(txtHn);
        Utils.h2 = Verifier.checkVisota(txtHk);

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

            System.out.println("Теоретический объем транспорта газа: " + Utils.qth / 24.0 * 1000 + " тыс. м3/час");
            System.out.println("при предположительном коэффициенте гидравлической эффективности: " + Utils.hydro);
            if (critMode)
                System.out.println("Внимание! Режим критического истечения газа в атмосферу!");

            System.out.println("Линейная скорость потока газа:");
            System.out.println("В начале газопровода: " + Utils.upspeed + " м/сек");
            System.out.println("В конце газопровода: " + Utils.downspeed + " м/сек");
        }
    }

    private static void calc3() {
        boolean critMode = false;
        Verifier.isCheck = true;

        String txtRo = "0.691";
        String txtAzot = "1.25";
        String txtPn = "51.3";
        String txtPk = "46.9";
        String txtPrt = "745.1";
        String txtTn = "25.5";
        String txtTk = "15.1";
        String txtTgr = "8.7";
        String txtQf = "1350";
        String txtL = "55.155";
        String txtD = "1195";
        String txtHn = "155";
        String txtHk = "215";


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

            System.out.println("Коэффициент гидравлической эффективности: " + Utils.hydro);
            System.out.println("при теоретическом объеме транспорта газа: " + Utils.qth + " тыс.м3/час");
            if (critMode)
                System.out.println("Внимание! Режим критического истечения газа в атмосферу!");

            System.out.println("Линейная скорость потока газа:  ");
            System.out.println("В начале газопровода: " + Utils.upspeed + " м/сек");
            System.out.println("В конце газопровода: " + Utils.downspeed + " м/сек");
        }
    }

    private static void calc4() {
        Verifier.isCheck = true;

        String txtRo = "0.691";
        String txtPn = "50";
        String txtPk = "45";
        String txtPrt = "750";
        String txtTn = "22";
        String txtTk = "13";
        String txtTgr = "8";
        String txtQf = "1355";
        String txtL = "55";
        String txtD = "1200";


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

            System.out.println("Среднее избыточное давление газа " + Utils.psr + "  кгс/см2");
            System.out.println("Средняя температура газа: " + Utils.tsr + "  по Цельсию");
        }
    }

    private static void calc5() {
        Verifier.isCheck = true;

        String txtRo = "0.691";
        String txtPn = "35.5";
        String txtPrt = "745.3";
        String txtTn = "12.7";
        String txtVg = "1.231";

        Utils.ro = Verifier.checkDensity(txtRo);
        Utils.pn = Verifier.checkPressure(txtPn);
        Utils.prt = Verifier.checkAtmPressure(txtPrt);
        Utils.tn = Verifier.checkTemperature(txtTn);
        Utils.vg = Verifier.checkVolume(txtVg);
        Utils.privp = Utils.PRES_PRIVEDNIYA;
        Utils.privt = Utils.TEMP_PRIVEDENIYA;

        if (Verifier.isCheck) {

            double patm = Utils.prt * 0.001359511;
            double pabs = Utils.pn + patm;
            Utils.privp = Utils.privp * 0.001359511;
            Utils.tn = Utils.tn + 273.15;
            Utils.privt = Utils.privt + 273.15;
            Utils.z = BaseCalc.calcZ(Utils.tn, pabs, Utils.ro);
            Utils.stock = BaseCalc.calcCountGas(Utils.vg, pabs, Utils.privp, Utils.tn, Utils.privt, Utils.z);

            System.out.println("Коэффициент сжимаемости газа: " + Utils.z + " .");
            System.out.println("Объем газа по условиям приведения: " + Utils.stock + " тыс.м3");
        }
    }
}

