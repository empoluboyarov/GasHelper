package empoluboyarov.com.gashelper;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import empoluboyarov.com.gashelper.core.DewPoint;
import empoluboyarov.com.gashelper.core.Utils;
import empoluboyarov.com.gashelper.core.Verifier;

public class DewPointActivity extends AppCompatActivity {

    private EditText etDp, etPn;
    private TextView tvResult;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dew_point);

        etDp = (EditText) findViewById(R.id.etDpDP);
        etPn = (EditText) findViewById(R.id.etPnDP);
        tvResult = (TextView) findViewById(R.id.tvResultDP);
    }

    public void calcDP(View view){
        Verifier.isCheck = true;
        Verifier.message = null;

        Utils.dp = Verifier.checkDewPoint(etDp.getText().toString());
        Utils.pn = Verifier.checkPressure(etPn.getText().toString());

        Utils.makeToast(this);

        if (Verifier.isCheck) {
            tvResult.setText("Приведенное значение точки росы составляет " + new DewPoint(Utils.dp, Utils.pn).getDp40() + " 0C.");
        } else tvResult.setText("Введите корректные значения");
    }
}
