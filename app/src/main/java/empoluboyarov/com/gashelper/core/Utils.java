package empoluboyarov.com.gashelper.core;

import android.content.Context;
import android.widget.Toast;

/**
 * Created by Evgeniy on 15.05.2016.
 */
public final class Utils {

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

    public static void makeToast(Context context){

        if (Verifier.message != null) {
            Toast toast = Toast.makeText(context, Verifier.message, Toast.LENGTH_LONG);
            toast.show();
        }
    }
}
