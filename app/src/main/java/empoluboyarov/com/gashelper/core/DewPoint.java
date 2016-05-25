package empoluboyarov.com.gashelper.core;

import java.util.List;

/**
 * Created by Evgeniy on 25.05.2016.
 */
public class DewPoint {

    private double initDP;
    private double pressure;
    private static double factorA1;
    private static double factorA2;
    private static double factorB1;
    private static double factorB2;
    private static double factorDP1;
    private static double factorDP2;

    public static double calcDP(double initDP, double pressure) {
        double factorA = calcFactorA(initDP);
        double factorB = calcFactorB(initDP);
        double tempPress = convertPressure(pressure);
        double waterStream = ((factorA * 101.325) / tempPress) + factorB;
        return calcDP40(waterStream);
    }

    private static double convertPressure(double pressure) {
        return (pressure + 1) * 0.0980665 * 1000;
    }

    private static double point;
    private static double point1;
    private static double point2;
    private static List<Double> tempListAB = CorrectionFactors.listAB;
    private static List<Double> tempListDP = CorrectionFactors.listDP;

    private static double calcFactorA(double initDP) {
        // проверяем на совпадение с целыми числами
        for (int i = 0; i < tempListAB.size(); i++) {
            double n = tempListAB.get(i);
            if (n == initDP) {
                point = n;
                return CorrectionFactors.factorA.get(point);
            }
        }
        // если проверка не удалась - ищем ближайшие точки для расчета
        analizator1(initDP);
        analizator1(initDP);

        return interpolator(initDP, point1, point2, factorA1, factorA2);
    }

    private static double calcFactorB(double initDP) {
        if (point != 0) {
            return CorrectionFactors.factorB.get(point1);
        }
        return interpolator(initDP, point1, point2, factorB1, factorB2);
    }

    private static double calcDP40(double waterStream) {

        for (int i = 0; i < tempListDP.size(); i++) {
            double n = tempListDP.get(i);
            if (n == waterStream) {
                point1 = n;
                return CorrectionFactors.factorDP.get(point1);
            }
        }
        count = 0;
        analisator2(waterStream);
        analisator2(waterStream);
        return interpolator(waterStream, point1, point2, factorDP1, factorDP2);
    }

    private static int count;

    private static void analizator1(double initDP) {

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
                        factorB1 = CorrectionFactors.factorB.get(point1);
                        factorA1 = CorrectionFactors.factorA.get(point1);

                    } else {
                        factorB2 = CorrectionFactors.factorB.get(point2);
                        factorA2 = CorrectionFactors.factorA.get(point2);
                    }
                    count++;
                }
            } else break;
        }
    }

    private static void analisator2(double waterStream) {
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
                        factorDP1 = CorrectionFactors.factorDP.get(point1);
                    } else {
                        factorDP2 = CorrectionFactors.factorDP.get(point2);
                    }
                    count++;
                }
            } else break;
        }
    }

    public static double interpolator(double targetY, double Y1, double Y2, double X1, double X2) {
        return (X1 + (targetY - Y1) * (X2 - X1) / (Y2 - Y1));
    }
}
