package empoluboyarov.com.gashelper.core;

import android.content.Context;
import android.widget.Toast;

import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

/**
 * Created by Evgeniy on 15.05.2016.
 */
public final class Utils {

    public static double TEMP_PRIVEDENIYA = 20;
    public static double PRES_PRIVEDNIYA = 760;

    public static double pn;
    public static double pk;
    public static double prt;
    public static double tn;
    public static double tk;
    public static double tgr;
    public static double dm;
    public static double dmm;
    public static double ro;
    public static double qf;
    public static double lm;
    public static double lkm;
    public static double privp;
    public static double privt;
    public static double psr;
    public static double tsr;
    public static double z;
    public static double stock;
    public static double vg;
    public static double azot;
    public static double tim;
    public static double dkmm;
    public static double expr;
    public static double qth;
    public static double h1;
    public static double h2;
    public static double hydro;
    public static double upspeed;
    public static double downspeed;
    public static double adiabata;
    public static double speedsound;
    public static double rofact;
    public static double speedgas;
    public static double dp;

    public static void makeToast(Context context){

        if (Verifier.message != null) {
            Toast toast = Toast.makeText(context, Verifier.message, Toast.LENGTH_LONG);
            toast.show();
        }
    }

    public final static Map<Double, Double> factorA = new LinkedHashMap<Double, Double>() {{
        put(-40., 0.1451);
        put(-38., 0.178);
        put(-36., 0.2189);
        put(-34., 0.267);
        put(-32., 0.3235);
        put(-30., 0.391);
        put(-28., 0.4715);
        put(-26., 0.566);
        put(-24., 0.6775);
        put(-22., 0.809);
        put(-20., 0.96);
        put(-18., 1.144);
        put(-16., 1.35);
        put(-14., 1.59);
        put(-12., 1.868);
        put(-10., 2.188);
        put(-8., 2.55);
        put(-6., 2.99);
        put(-4., 3.48);
        put(-2., 4.03);
        put(0., 4.67);
        put(2., 5.4);
        put(4., 6.225);
        put(6., 7.15);
        put(8., 8.2);
        put(10., 9.39);
        put(12., 10.72);
        put(14., 12.39);
        put(16., 13.94);
        put(18., 15.75);
        put(20., 17.87);
        put(22., 20.15);
        put(24., 22.8);
        put(26., 25.5);
        put(28., 28.7);
        put(30., 32.3);
        put(32., 36.1);
        put(34., 40.5);
        put(36., 45.2);
        put(38., 50.8);
        put(40., 56.25);
    }};

    public final static Map<Double, Double> factorB = new LinkedHashMap<Double, Double>() {{
        put(-40., 0.00347);
        put(-38., 0.00402);
        put(-36., 0.00465);
        put(-34., 0.00538);
        put(-32., 0.00623);
        put(-30., 0.0071);
        put(-28., 0.00806);
        put(-26., 0.00921);
        put(-24., 0.01043);
        put(-22., 0.01168);
        put(-20., 0.0134);
        put(-18., 0.0151);
        put(-16., 0.01705);
        put(-14., 0.01927);
        put(-12., 0.02155);
        put(-10., 0.0229);
        put(-8., 0.0271);
        put(-6., 0.03035);
        put(-4., 0.0338);
        put(-2., 0.0377);
        put(0., 0.0418);
        put(2., 0.0464);
        put(4., 0.0515);
        put(6., 0.0571);
        put(8., 0.063);
        put(10., 0.0696);
        put(12., 0.0767);
        put(14., 0.0855);
        put(16., 0.093);
        put(18., 0.102);
        put(20., 0.112);
        put(22., 0.1227);
        put(24., 0.1343);
        put(26., 0.1463);
        put(28., 0.1595);
        put(30., 0.174);
        put(32., 0.1895);
        put(34., 0.207);
        put(36., 0.224);
        put(38., 0.2425);
        put(40., 0.263);
    }};

    public final static Map<Double, Double> factorDP = new LinkedHashMap<Double, Double>() {{
        put(0.0075, -40.0);
        put(0.0112, -35.0);
        put(0.015, -31.5);
        put(0.0187, -29.0);
        put(0.0225, -26.5);
        put(0.03, -23.0);
        put(0.0375, -20.0);
        put(0.0449, -18.0);
        put(0.0599, -14.5);
        put(0.0749, -11.0);
        put(0.112, -5.0);
        put(0.15, -1.0);
        put(0.187, 2.5);
        put(0.225, 5.0);
        put(0.3, 9.5);
        put(0.375, 13.5);
        put(0.449, 16.0);
        put(0.599, 21.0);
        put(0.749, 25.0);
    }};

    private final List<Double> listDP = new LinkedList<Double>() {{
        add(0.0075);
        add(0.0112);
        add(0.015);
        add(0.0187);
        add(0.0225);
        add(0.03);
        add(0.0375);
        add(0.0449);
        add(0.0599);
        add(0.0749);
        add(0.112);
        add(0.15);
        add(0.187);
        add(0.225);
        add(0.3);
        add(0.375);
        add(0.449);
        add(0.599);
        add(0.749);
    }};

    private final  List<Double> listAB = new LinkedList<Double>() {{
        add(-40.);
        add(-38.);
        add(-36.);
        add(-34.);
        add(-32.);
        add(-30.);
        add(-28.);
        add(-26.);
        add(-24.);
        add(-22.);
        add(-20.);
        add(-18.);
        add(-16.);
        add(-14.);
        add(-12.);
        add(-10.);
        add(-8.);
        add(-6.);
        add(-4.);
        add(-2.);
        add(0.);
        add(2.);
        add(4.);
        add(6.);
        add(8.);
        add(10.);
        add(12.);
        add(14.);
        add(16.);
        add(18.);
        add(20.);
        add(22.);
        add(24.);
        add(26.);
        add(28.);
        add(30.);
        add(32.);
        add(34.);
        add(36.);
        add(38.);
        add(40.);
    }};

    public List<Double> getListAB() {
        return listAB;
    }

    public List<Double> getListDP() {
        return listDP;
    }
}
