package empoluboyarov.com.gashelper.core;

/**
 * Created by Evgeniy on 15.05.2016.
 */
public class BaseCalc {

    public double dens; // плотность газа, кг/м3
    public double temp; // температура газа, по Кельвину
    public double pres; // абсолютное давление газа, кгс/см2
    public double nitrogen; // молярная составляющая азота, доли единицы
    public double z; // коэффициент сжимаемости газа.
    public double privPres; // давление приведения, кгс/см2
    public double privTemp; // температура приведения, по Кельвину


    /* относительная плотность газа
    * dens - абсолютная плотность газа, кг/м3  */
    private static double calcDensity(double dens) {
        return dens / 1.2044;
    }

    /* перевод кгс/см2 в МПа
     * pres - абсолютное давление газа, кгс/см2  */
    private static double calcPres(double pres) {
        return pres * 0.0980665;
    }

    /*  коэффициент сжимаемости газа
    *
    *
    *  */
    public static double calcZ(double temp, double pres, double dens) {
        double dlt = calcDensity(dens);
        double pmpa = calcPres(pres);
        return 1 - ((10.2 * pmpa - 6) * (0.345 / 100 * dlt - 0.446 / 1000) + 0.015) * (1.3 - 0.0144 * (temp - 283.2));
    }

    /* коэффициент адиабатты*/
    public static double calcAdiabata(double temp, double pres, double dens, double nitrogen) {
        double pmpa = calcPres(pres);
        double x = pmpa / temp;
        double y = 1 - nitrogen;

        double k1 = 1.556 * (1 + 0.074 * nitrogen);
        double k2 = 3.9 / 10000 * temp * (1 - 0.68 * nitrogen);
        double k3 = 0.208 * dens;
        double k4 = Math.pow(x, 1.43) * (384 * y * Math.pow(x, 0.8) + 26.4 * nitrogen);
        return k1 - k2 - k3 + k4;

    }

    /* фактическая плотность газа*/
    public static double calcFactDens(double dens, double pres, double temp, double z) {
        double pmpa = calcPres(pres);
        return dens * (293.15 * pmpa) / (temp * 0.101325 * z);
    }

    /* запас газа в сосуде*/
    public static double calcCountGas(double v, double pres, double privPres, double temp, double privTemp, double z) {
        return (v * pres * privTemp) / (temp * z * privPres);
    }


}
