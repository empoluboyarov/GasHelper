package empoluboyarov.com.gashelper.core;

/**
 * Created by Evgeniy on 25.05.2016.
 */
public class PressureConverter {
    private static double finalPressure;

    public static double convertPressure(double initialPressure, PressureType startType, PressureType finalType) {
        switch (startType) {
            case KGF:
                return convertKGF(initialPressure, finalType);
            case ATM:
                return convertATM(initialPressure, finalType);
            case MPA:
                return convertMPA(initialPressure, finalType);
            case MMPT:
                return convertMMPT(initialPressure, finalType);
            case BAR:
                return convertBAR(initialPressure, finalType);
        }
        return finalPressure;
    }

    private static double convertKGF(double initialPressure, PressureType finalType) {
        switch (finalType) {
            case KGF:
                return initialPressure;
            case ATM:
                return initialPressure * 0.967840850;
            case MPA:
                return initialPressure * 0.0980665;
            case MMPT:
                return initialPressure * 735.562;
            case BAR:
                return initialPressure * 0.980665;
        }
        return finalPressure;
    }

    private static double convertATM(double initialPressure, PressureType finalType) {
        switch (finalType) {
            case KGF:
                return initialPressure * 1.03323;
            case ATM:
                return initialPressure;
            case MPA:
                return initialPressure * 0.101325;
            case MMPT:
                return initialPressure * 760.002;
            case BAR:
                return initialPressure * 1.01325;
        }
        return finalPressure;
    }

    private static double convertMPA(double initialPressure, PressureType finalType) {
        switch (finalType) {
            case KGF:
                return initialPressure * 10.1972;
            case ATM:
                return initialPressure * 9.86923;
            case MPA:
                return initialPressure;
            case MMPT:
                return initialPressure * 7500.64;
            case BAR:
                return initialPressure * 10.0000;

        }
        return finalPressure;
    }

    private static double convertMMPT(double initialPressure, PressureType finalType) {
        switch (finalType) {
            case KGF:
                return initialPressure * 0.001359511;
            case ATM:
                return initialPressure * 0.0011315785;
            case MPA:
                return initialPressure * 0.000133322;
            case MMPT:
                return initialPressure;
            case BAR:
                return initialPressure * 0.00133322;

        }
        return finalPressure;
    }

    private static double convertBAR(double initialPressure, PressureType finalType) {
        switch (finalType) {
            case KGF:
                return initialPressure * 1.01972;
            case ATM:
                return initialPressure * 0.986923;
            case MPA:
                return initialPressure * 0.1;
            case MMPT:
                return initialPressure * 750.064;
            case BAR:
                return initialPressure;
        }
        return finalPressure;
    }
}