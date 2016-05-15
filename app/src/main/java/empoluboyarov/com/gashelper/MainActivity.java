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

    public void onClick(View view){
        switch (view.getId()){
            case R.id.button:
                Intent intent = new Intent(this, CalcZActivity.class);
                startActivity(intent);
                break;
            case R.id.button2:
                Intent intent1 = new Intent(this, CalcAdiabataActivity.class);
                startActivity(intent1);
                break;
            case R.id.button3:
                Intent intent2 = new Intent(this, CalcFactDensActivity.class);
                startActivity(intent2);
                break;
            case R.id.button4:
                Intent intent3 = new Intent(this, CalcGasCountActivity.class);
                startActivity(intent3);
                break;
        }
    }
}
