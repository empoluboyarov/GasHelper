package empoluboyarov.com.gashelper;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class MainActivity extends AppCompatActivity {

    Button btn1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void onClick(View view) {
        Intent intent = null;
        switch (view.getId()) {
            case R.id.button:
                intent = new Intent(this, CalcZActivity.class);
                break;
            case R.id.button2:
                intent = new Intent(this, CalcAdiabataActivity.class);
                break;
            case R.id.button3:
                intent = new Intent(this, CalcFactDensActivity.class);
                break;
            case R.id.button4:
                intent = new Intent(this, CalcGasCountActivity.class);
                break;
        }
        startActivity(intent);
    }
}
