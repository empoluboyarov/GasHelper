package empoluboyarov.com.gashelper.core;

public final class Verifier {

    public static boolean isCheck;
    public static String message = null;
    private static double result = 0.0;

    public static double checkDensity(String txtDens) {
        message = null;
        if (txtDens == null || txtDens.isEmpty()) {
            message = "Введите корректное значение плотности.";
            isCheck = false;
        } else {
            double dens = Double.valueOf(txtDens);
            if (dens >= 0.55 && dens <= 1) {
                result = dens;
            } else {
                message = "Абсолютная плотность газа: от 0,55 до 1 кг/м3!";
                isCheck = false;
            }
        }
        return result;
    }

    public static double checkPressure(String txtPres) {
        message = null;
        if (txtPres == null || txtPres.isEmpty()) {
            message = "Введите корректное значение давления.";
            isCheck = false;
        } else {
            double pres = Double.valueOf(txtPres);
            if (pres < 0.1 || pres > 250) {
                message = "Избыточное давление газа: от 0,1 до 250 кгс/см2!";
                isCheck = false;
            } else {
                result = pres;
            }
        }
        return result;
    }

    public static double checkTemperature(String txtTemp) {
        message = null;
        if (txtTemp == null || txtTemp.isEmpty()) {
            message = "Введите корректное значение тумпературы.";
            isCheck = false;
        } else {
            double temp = Double.valueOf(txtTemp);
            if (temp < -60 || temp > 150) {
                message = "Температура газа: от -60 до 150 градусов Цельсия!";
                isCheck = false;
            } else {
                result = temp;
            }
        }
        return result;
    }

    public static double checkAtmPressure(String txtAtmPres) {
        message = null;
        if (txtAtmPres == null || txtAtmPres.isEmpty()) {
            message = "Введите корректное значение атомосферного давления.";
            isCheck = false;
        } else {
            double pres = Double.valueOf(txtAtmPres);
            if (pres < 300 || pres > 780) {
                message = "Атмосферное давление: от 300 до 780 мм рт.ст.!";
                isCheck = false;
            } else {
                result = pres;
            }
        }
        return result;
    }

    public static double checkNitrogen(String txtNitro) {
        message = null;
        if (txtNitro == null || txtNitro.isEmpty()) {
            message = "Введите корректное значение молярной составляющей азота.";
            isCheck = false;
        } else {
            double nitro = Double.valueOf(txtNitro);
            if (nitro < 0.1 || nitro > 25) {
                message = "Молярная составляющая азота: от 0,1 до 25 % !";
                isCheck = false;
            } else {
                result = nitro / 100;
            }
        }
        return result;
    }

    public static double checkLength(String txtLength) {
        message = null;
        if (txtLength == null || txtLength.isEmpty()) {
            message = "Введите корректное значение длины газопровода.";
            isCheck = false;
        } else {
            double length = Double.valueOf(txtLength);
            if (length < 0.001 || length > 250) {
                message = "Длина газопровода: от 0,001 до 250 км!";
                isCheck = false;
            } else {
                result = length;
                Utils.lm = length * 1000;
            }
        }
        return result;
    }

    public static double checkDiameter(String txtDiameter) {
        message = null;
        if (txtDiameter == null || txtDiameter.isEmpty()) {
            message = "Введите корректное значение диаметра газопровода.";
            isCheck = false;
        } else {
            double diameter = Double.valueOf(txtDiameter);
            if (diameter < 5 || diameter > 2500) {
                message = "Внутренний диаметр трубы: от 5 до 2500 мм!";
                isCheck = false;
            } else {
                result = diameter;
                Utils.dm = diameter / 1000;
            }
        }
        return result;
    }

    public static void checkDeltaPres(double presStart, double presFin) {
        if (presFin >= presStart) {
            isCheck = false;
            message = "Давление в конце МГ должно быть меньше, чем в начале!";
        }
    }

    public static void checkDeltaTemp(double tempStart, double tempFin) {
        if (tempFin >= tempStart) {
            isCheck = false;
            message = "Температура газа в конце МГ должна быть меньше, чем в начале!";
        }
    }

    public static double checkGasTransport(String txtGasTransport) {
        message = null;
        if (txtGasTransport == null || txtGasTransport.isEmpty()) {
            message = "Введите корректное значение объема транспорта газа.";
            isCheck = false;
        } else {
            double gasTransport = Double.valueOf(txtGasTransport);
            if (gasTransport <= 0) {
                message = "Фактический объем транспорта газа должен быть больше ноля!";
                isCheck = false;
            } else {
                result = gasTransport / 1000 * 24;
            }
        }
        return result;
    }
}
