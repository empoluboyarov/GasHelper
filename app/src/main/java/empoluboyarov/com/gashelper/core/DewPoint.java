package empoluboyarov.com.gashelper.core;

import java.util.List;

/**
 * Created by Evgeniy on 25.05.2016.
 */
public class DewPoint {

    private double factorA1, factorA2,factorB1, factorB2, factorDP1, factorDP2,
            point, point1, point2, dp40;

    private  List<Double> tempListAB, tempListDP;

    private int count;

    public DewPoint(double dp, double pn) {
        count = 0;
        dp40 = calcDP(dp, pn);
    }

    public double getDp40() {
        System.out.println(dp40);
        return dp40;
    }

    private double calcDP(double initDP, double pressure) {
        Utils utils = new Utils();
        tempListAB = utils.getListAB();
        tempListDP = utils.getListDP();
        double factorA = calcFactorA(initDP);
        double factorB = calcFactorB(initDP);
        double tempPress = convertPressure(pressure);
        double waterStream = ((factorA * 101.325) / tempPress) + factorB;
        return calcDP40(waterStream);
    }

    private double convertPressure(double pressure) {
        return (pressure + 1) * 0.0980665 * 1000;
    }


    private double calcFactorA(double initDP) {
        // проверяем на совпадение с целыми числами
        for (int i = 0; i < tempListAB.size(); i++) {
            double n = tempListAB.get(i);
            if (n == initDP) {
                point = n;
                return Utils.factorA.get(point);
            }
        }
        // если проверка не удалась - ищем ближайшие точки для расчета
        analizator1(initDP);
        analizator1(initDP);

        return interpolator(initDP, point1, point2, factorA1, factorA2);
    }

    private double calcFactorB(double initDP) {
        if (point != 0) {
            return Utils.factorB.get(point);
        }
        return interpolator(initDP, point1, point2, factorB1, factorB2);
    }

    private double calcDP40(double waterStream) {

        for (int i = 0; i < tempListDP.size(); i++) {
            double n = tempListDP.get(i);
            if (n == waterStream) {
                point1 = n;
                return Utils.factorDP.get(point1);
            }
        }
        count = 0;
        analisator2(waterStream);
        analisator2(waterStream);
        return interpolator(waterStream, point1, point2, factorDP1, factorDP2);
    }

    private void analizator1(double initDP) {

        double deltaMin = Math.abs(tempListAB.get(0) - initDP);
        for (int i = 1; i < tempListAB.size(); i++) {
            double delta = Math.abs(tempListAB.get(i) - initDP);
            if (delta < deltaMin) {
                deltaMin = delta;
                double deltanext = Math.abs(tempListAB.get(i + 1) - initDP);
                if (deltaMin < deltanext) {
                    if (count == 0) point1 = tempListAB.get(i);
                    else point2 = tempListAB.get(i);
                    tempListAB.remove(i);
                    if (count == 0) {
                        factorB1 = Utils.factorB.get(point1);
                        factorA1 = Utils.factorA.get(point1);

                    } else {
                        factorB2 = Utils.factorB.get(point2);
                        factorA2 = Utils.factorA.get(point2);
                    }
                    count++;
                }
            } else break;
        }
    }

    private void analisator2(double waterStream) {
        double deltaMin = Math.abs(tempListDP.get(0) - waterStream);
        for (int i = 1; i < tempListDP.size(); i++) {
            double delta = Math.abs(tempListDP.get(i) - waterStream);
            if (delta < deltaMin) {
                deltaMin = delta;
                double deltanext = Math.abs(tempListDP.get(i + 1) - waterStream);
                if (deltaMin < deltanext) {
                    if (count == 0) point1 = tempListDP.get(i);
                    else point2 = tempListDP.get(i);
                    tempListDP.remove(i);
                    if (count == 0) {
                        factorDP1 = Utils.factorDP.get(point1);
                    } else {
                        factorDP2 = Utils.factorDP.get(point2);
                    }
                    count++;
                }
            } else break;
        }
    }

    private double interpolator(double targetY, double Y1, double Y2, double X1, double X2) {
        return (X1 + (targetY - Y1) * (X2 - X1) / (Y2 - Y1));
    }
}
