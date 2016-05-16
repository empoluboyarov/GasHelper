package empoluboyarov.com.gashelper.core;


public final class BaseCalc {

    /* относительная плотность газа
     * ro - абсолютная плотность газа, кг/м3  */
    private static double calcDelta(double ro) {
        return ro / 1.2044;
    }

    /* перевод кгс/см2 в МПа
     * p - абсолютное давление газа, кгс/см2  */
    private static double calcPres(double p) {
        return p * 0.0980665;
    }

    /* коэффициент сжимаемости газа
     * ro - плотность газа, кг/м3
     * p - абсолютное давление газа, кгс/см2
     * t - температура газа, по Кельвину */
    public static double calcZ(double t, double p, double ro) {
        double dlt = calcDelta(ro);
        p = calcPres(p);
        return 1 - ((10.2 * p - 6) * (0.345 / 100 * dlt - 0.446 / 1000) + 0.015) * (1.3 - 0.0144 * (t - 283.2));
    }

    /* коэффициент адиабатты
     * azot - молярная составляющая азота, доли единицы
     * ro - плотность газа абсолютная, кг/м3
     * p - абсолютное давление, кгс/см2
     * t - температура, по Кельвину  */
    public static double calcAdiabata(double t, double p, double ro, double azot) {
        double pmpa = calcPres(p);
        double k1 = 1.556 * (1 + 0.074 * azot);
        double k2 = 3.9 / 10000 * t * (1 - 0.68 * azot) + 0.208 * ro; // последний плюс - не ошибка! потом будем отнимать все сразу
        double k3 = Math.pow((pmpa / t), 1.43) * (384 * (1 - azot) * (Math.pow((pmpa / t), 0.8)) + 26.4 * azot);
        return k1 - k2 + k3; // (Сарданашвили, стр. 44, формула 1.69)
    }

    /* фактическая плотность газа
     * ro - плотность газа абсолютная, кг/м3
     * p - абсолютное давление, кгс/см2
     * t - температура, по Кельвину
     * z - коэффициент сжимаемости  */
    public static double calcRoFact(double ro, double p, double t, double z) {
        double pmpa = calcPres(p);
        return ro * (293.15 * pmpa) / (t * 0.101325 * z);
    }

    /* запас газа в сосуде
     * v - геометрический объем, м3
     * p - абсолютное давление, кгс/см2
     * privp - давление приведения, кгс/см2
     * t - температура газа, по Кельвину
     * privt - температура приведения, по Кельвину
     * z - коэффициент сжимаемости  */
    public static double calcCountGas(double v, double p, double privP, double t, double privT, double z) {
        return (v * p * privT) / (t * z * privP);
    }

    /* среднее давление на участке МГ
    * pn - начальное абсолютное давление, кгс/см2
    * pk - конечное абсолютное давление, кгс/см2 */
    public static double calcAverPres(double pn, double pk) {
        return 2 / 3 * (pn + (pk * pk) / (pn + pk));
    }

    /* средняя температура газа, по Кельвину (простой метод)
     * tgr - температура грунта, по Кельвину
     * tn - температура в начале мГ, по Кельвину
     * tk - температура в конце МГ, по Кельвину*/
    public static double calcAverTempSimple(double ax, double tgr, double tn, double tk) {
        double result;
        if (tgr < tk)
            //TODO разобраться с логарифмом в формуле
            result = tgr + (tn - tk) / ln((tn - tgr) / (tk - tgr));
        else result = tk;
        return result;
    }

    /* средняя температура газа, по Кельвину
     * ax - множитель для определения средней температуры
     * tgr - температура грунта, по Кельвину
     * tn - температура в начале мГ, по Кельвину
     * tk - температура в конце МГ, по Кельвину*/
    //TODO исправить работу метода
    public static double calcAverTem(double ax, double tgr, double tn, double tk) {
        return tgr + ((tn - tk) / ax) * (1 - exp(-ax));
    }

    /* множитель ax для определения средней температуры
     * km - коэффициент теплопроводности
     * d - внутренний диаметр, мм
     * l - длина МГ, км
     * q - транспорт газа, млн.м3/сут
     * delta - относительная плотность газа
     * cp - теплоемкость газа */
    public static double calcAx(double km, double d, double l, double q, double delta, double cp) {
        return 62.6 * ((km * d * l) / (q * delta * cp * 1E6));
    }

    /* теплоемкость газа в смешанной системе координат, кКал/кг*К
     * tsr - средняя температура, по Кельвину
     * psr - среднее давление, кгс/см2 */
    public static double calcCp(double tsr, double psr) {
        return 0.405 + 4.39e-4 * tsr + 46000 * (psr - 1) / (tsr * tsr * tsr);
    }

    /* коэффициент Джоуля-Томсона, К/(кгс/см2)
     * tsr - средняя температура, по Кельвину
     * cp - теплоемкость газа */
    public static double calcDi(double tsr, double cp) {
        return (23000 / (tsr * tsr) - 0.035) / cp;
    }

    /* скорость звука в газе, м/с
     * t - температура газа, по Кельвину
     * adiabata - коэффициент адиабаты
     * z - коэффициент сжимаемости газа
     * ro - плотность газа абсолютная, кг/м3  */
    public static double calcSpeedSound(double t, double adiabata, double z, double ro) {
        return 18.591 * Math.pow((t * adiabata * z) / ro, 0.5);
    }

    /* скорость истечения газа (по формуле Сен-Венана), м/с
     * adiabata - коэффициент адиабаты
     * rofact - фактическая плотность газа, кг/м3
     * pn - начальное абсолютное давление, кгс/см2
     * pk - конечное абсолютное давление, кгс/см2  */
    public static double calcSpeedGas(double adiabata, double rofact, double pn, double pk) {
        double pnmpa = calcPres(pn);
        double pkmpa = calcPres(pk);
        return Math.sqrt(2 * adiabata / (adiabata - 1) * pnmpa * 1000000 / rofact *
                (1 - Math.pow(pkmpa / pnmpa, (adiabata - 1) / adiabata)));
    }

    /* некритическое истечение
     * s - площадь истечения, м2
     * tim - время истечения, сек
     * pn - начальное абсолютное давление, кгс/см2
     * pk - конечное абсолютное давление, кгс/см2 */
    public static double calcExpNoCrit(double s, double tim, double pn, double pk) {
        double dp = pn - pk;
        return 110 * s * dp * tim;
    }

    /* число Рейнолдса
     * q - расход газа, млн.м3/сут
     * ro - плотность газа, кг/м3
     * d -  внутренний диаметр, мм
     * mu - динамическая вязкость */
    public static double calcRe(double ro, double q, double d, double mu) {
        double dlt = calcDelta(ro);
        return 1810 * q * dlt / (d * mu);
    }

    //TODO доделать метод
    /* лямбда- коэффициент сопротивления трения
     * re - число Рейнолдса
     * d - внутренний диаметр, мм  */
    public static double calcLambda(double re, double d) {
        return 0.067 * (exp(ln(158 / re + 0.06 / d) * (1 / 5)));
    }

    //TODO доделать метод
    /* коэффициент теплопередачи Km без учета эффекта дросселирования, кКал/м2чК
     * tn - начальная температура, по Кельвину
     * tk - конечная температура, по Кельвину
     * tgr - температура грунта, по Кельвину
     * l - длина МГ, км
     * d - внутренний диаметр МГ, мм
     * delta - относительная плотность газа
     * q - транспорт газа, млн.м3/сут
     * cp - теплоемкость газа   */
    public static double calcKm(double tn, double tk, double tgr, double l,
                                double d, double delta, double q, double cp) {
        return -ln((tk - tgr) / (tn - tgr)) / l * q * delta * cp * 1000000 / (62.6 * (d + 20));
    }


    /* коэффициент теплопередачи с учетом эффекта дросселирования, кКал/м2чК
     * eax - множитель для KM
     * q - транспорт газа, млн.м3/сут
     * delta - относительная плотность газа
     * cp - теплоемкость газа
     * d - диаметр МГ, мм
     * l - длина МГ, км */
    public static double calcKmDross(double eax, double q, double delta, double cp, double d, double l) {
        return (eax * q * delta * cp * 1E6) / (62.6 * d * l);
    }

    /* множитель Eax для расчета Km (методом подбора по наименьшей разнице температур)
     * tn - начальная температура, по Кельвину
     * tk - конечная температура, по Кельвину
     * tgr - температура грунта, по Кельвину
     * pn - начальное абсолютное давление, кгс/см2
     * pk - конечное абсолютное давление, кгс/см2
     * psr - среднее абсолютное давление, кгс/см2
     * ax - множитель для Tsr
     * di - коэффициент Джоуля-Томсона */
    public static double calcEax(double tn, double tk, double tgr, double pn, double pk,
                                 double psr, double ax, double di) {
        //TODO уточнить что за метод ABS
        double deltat = Math.abs(tn);
        double val = 0.0;
        for (int x = 0; x < 3000; x++) {
            double dx = (ax / 100) * x;
            double tcrush = tgr + (tn - tgr) * exp(-dx) - di * (pn * pn - pk * pk) / (2 * dx * psr) * (1 - exp(-dx));
            if (deltat > Math.abs(tcrush - tk)) {
                val = dx;
                deltat = Math.abs(tcrush - tk);
            }
        }
        return val;
    }


    /* газовая постоянная:
     * из СИ (Дж/(кг*К)) в смешанную (кГм/(кг*К)) */
    public static double calcRToR(double r) {
        return r / 9.7619;
    }

    /* средняя температура с учетом эффекта дросселирования
     * tn - температура в начале участка, по Кельвину
     * tk - температура в конце участка, по Кельвину
     * tgr - средняя температура, по Кельвину
     * pn - абсолютное давление в начале участка, кгс/см2
     * pk - абсолютное давление в конце участка, кгс/см2
     * psr - абсолютное среднее давление, кгс/см2
     * di - коэффициент Джоуля-Томсона, К/(кгс/см2)
     * ax - множитель для TsrDross
     * l - длина МГ, км  */
    //TODO доделать метод
    public static double calcTsrDross(double tn, double tk, double tgr, double pn, double pk,
                                      double psr, double di, double ax, double l) {
        double t1 = tgr + (tn - tk) / (ax * l) * (1 - exp(-ax));
        double t2 = di * (pn * pn - pk * pk) / (2 * ax * l * psr) * (1 - (1 / (ax * l)) * (1 - exp(-ax)));
        return t1 - t2;
    }

    /* начальное предположение о пропускной способности газа Qf, млн.м3/сут
     * dt - диаметр трубы в метрах  */
    public static double calcQf(double dt) {
        double a = 0.138177961111069;
        double b = -2.48613691329956;
        double c = 13.8517427444458;
        double d = 11.1694717407227;
        return a + b * dt + c * dt * dt + d * dt * dt * dt;
    }

    /* динамический коэффициент вязкости, кгс*с/м2
     * ro - плотность газа, кг/м3
     * psr - абсолютное среднее давление, кгс/см2
     * tsr - средняя температура, по Кельвину  */
    public static double calcMu(double ro, double psr, double tsr) {
        double dlt = calcDelta(ro);
        double ppk = 1.808 * (26.831 - 1.205 * dlt);
        double tpk = 155.24 * (0.564 + 1.205 * dlt);
        double ppr = psr / ppk;
        double tpr = tsr / tpk;
        double musi = 5.1e-6 * (1 + ro * (1.1 - 0.25 * ro)) * (0.037 + tpr * (1 - 0.104 * tpr)) *
                (1 + (ppr * ppr) / (30 * (tpr - 1)));
        return musi / 9.81;
    }

    //TODO доделать комментарии
    //TODO доделать метод
    /* pn, pk - давление на всасе и на выходе ГПА избыточное, кгс/см2
     * prt - атмосферное давлениеб мм рт.ст.
     * tn, tk - температура на всасе и на выходе ГПА, по Кельвину
     * adiabata - коэффициент адиабаты */
    public static double calcPolyKpd(double pn, double pk, double prt, double tn, double tk, double adiabata) {
        double patm = prt * 0.001359511;
        double pnabs = pn + patm;
        double pkabs = pk + patm;
        double ppn = pnabs * 0.0980665;
        double ppk = pkabs * 0.0980665;
        double mt = log10(ppk / ppn) / log10(tk / tn);
        double kpd = (adiabata - 1) / adiabata * mt * 100;
        if (kpd > 100)
            return 100.0;
        else return kpd;
    }

    //TODO дописать комментарии
    /* остаточное давление на участке МГ, кгс/см2
     * q - тыс.м3
     * v - м3
     * privp - кгс/см2
     * privt - Кельвин
     * tn - Кельвин */
    public static double calcQtoP(double q, double v, double z, double privp, double privt, double tn) {
        return (q * 1000 * privp * tn * z) / (v * privt);
    }

    /* критическое истечение
     * azot - молярная составляющая азота
     * ro - абсолютная плотность газа, кг/м3
     * d - внутренний диаметр, м
     * pn - начальное абсолютное давление, кгс/см2
     * pk - конечное абсолютное давление, кгс/см2
     * tn - начальная температура газа, по Кельвину
     * tk - конечная температура газа, по Кельвину
     * tim - время истечения в секундах   */
    public static double calcExpCritNew(double azot, double ro, double d, double pn,
                                        double pk, double tn, double tk, double tim) {


        double pnmpa = pn * 0.0980665;// МПа
        double pkmpa = pk * 0.0980665;// МПа
        double pnpa = pnmpa * 1e6;// Па
        double pkpa = pkmpa * 1e6;// МПа
        double dmm = d * 1000;// диаметр в мм

        double z = calcZ(tn, pn, ro); // давление - в килограммах
        double k = calcAdiabata(tn, pn, ro, azot); // адиабата. Давление - в килограммах

        double qsut = calcQf(d);// предположительный расход газа, млн.м3/сут
        double mu = calcMu(ro, pn, tn); // динамическая вязкость
        double re = calcRe(ro, qsut, dmm, mu); // число Рейнолдса
        // коэффициент расхода:
        double kq = 0.587 + (5.5 / (Math.pow(re, 0.5))) + (0.348 / (Math.pow(re, 0.333))) - (110.92 / re);

        double s = (3.141593 * d * d) / 4;
        double ud = calcRoFact(ro, pn, tn, z);
        double q = kq * s / ro * Math.sqrt(k * pnpa * ud * (Math.pow((2 / (k + 1)), ((k + 1) / (k - 1))))) * 1;

        qsut = (q / 1000000) * 24 * 60 * 60;
        re = calcRe(ro, qsut, dmm, mu);// число Рейнолдса
        kq = 0.587 + (5.5 / (Math.pow(re, 0.5))) + (0.348 / (Math.pow(re, 0.333))) - (110.92 / re);
        double result = kq * s / ro * Math.sqrt(k * pnpa * ud * (Math.pow((2 / (k + 1)), ((k + 1) / (k - 1))))) * tim;
        return result;
    }

    /*  теоретический транспорт газа
     TODO дописать комментарий */
    public static double QTheor(double ro, double pn, double pk, double prt, double tn,
                                double tk, double tgr, double l, double d, double h1, double h2) {
        double patm = prt * 0.001359511;
        pn = pn + patm;
        pk = pk + patm;

        q:=fmod.Qf(d / 1000);
        delta:=fmod.Delta(ro);
        psr:=fmod.Psr(pn, pk);// среднее давление
// первое приближение средней температуры
        km:=1;
        cp:=0.65;// начальное предположение
        ax:=fmod.Ax(km, d, l, q, delta, cp);// множитель для Tsr
        tsr:=fmod.Tsr(ax, tgr, tn, tk);// средняя температура
        cp:=fmod.Cp(tsr, psr);// теплоемкость газа
        di:=fmod.Di(tsr, cp);// коэффициент Джоуля-Томсона
        eax:=fmod.Eax(tn, tk, tgr, pn, pk, psr, ax, di);
        km:=fmod.KmDross(eax, q, delta, cp, d, l);
// второе приближение средней температуры
        ax:=fmod.Ax(km, d, l, q, delta, cp);// множитель для Tsr
        tsr:=fmod.Tsr(ax, tgr, tn, tk);// средняя температура
// теоретический транспорт газа без учета высоты над уровнем моря
        z:=fmod.Z(ro, psr, tsr);// коэф. сжимаемости
        mu:=fmod.Mu(ro, psr, tsr);// динамическая вязкость
        re:=fmod.Re(ro, q, d, mu);// число Рейнолдса
        lambda:=fmod.Lambda(re, d);// лямбда
        qth:=
        3.26e-7 * exp(ln(d) * (5 / 2)) * sqrt((pn * pn - pk * pk) / (delta * lambda * z * tsr * l));// теор. расход промежуточный
// транспорт газа с учетом высоты над уровнем моря
        re:=fmod.Re(ro, qth, d, mu);// число Рейнолдса
        lambda:=fmod.Lambda(re, d);// лямбда
        dH:=h2 - h1;
        A:=62.6 * km * d / (qth * delta * cp * 1000000);// множительная чепухня
        AL:=A * l;
        am:=delta / (14.64 * tsr * z);
        b:=1 + am * dH / 2;
        qth:=
        3.26e-7 * exp(ln(d) * (5 / 2)) * sqrt((pn * pn - pk * pk * (1 + am * dH)) / (delta * lambda * z * tsr * l * b));// теор. расход с учетом dH
//
        Result:=qth;


    }
}
