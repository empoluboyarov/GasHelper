package empoluboyarov.com.gashelper.core;

import android.content.Context;
import android.widget.Toast;

/**
 * Created by Evgeniy on 15.05.2016.
 */
public class Utils {

    public static void makeToast(Context context){

        if (Verifier.message != null) {
            Toast toast = Toast.makeText(context, Verifier.message, Toast.LENGTH_LONG);
            toast.show();
        }
    }
}
