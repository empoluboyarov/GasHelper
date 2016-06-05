package empoluboyarov.com.gashelper;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;

import empoluboyarov.com.gashelper.hardcounts.CalcAverageActivity;
import empoluboyarov.com.gashelper.hardcounts.CalcEffectivActivity;
import empoluboyarov.com.gashelper.hardcounts.CalcProduvkaActivity;
import empoluboyarov.com.gashelper.hardcounts.CalcPropuskActivity;
import empoluboyarov.com.gashelper.hardcounts.ConvertVolumeActivity;
import empoluboyarov.com.gashelper.hardcounts.DynamicCountActivity;
import empoluboyarov.com.gashelper.simplecounts.CalcAdiabataActivity;
import empoluboyarov.com.gashelper.simplecounts.CalcFactDensActivity;
import empoluboyarov.com.gashelper.simplecounts.CalcGasCountActivity;
import empoluboyarov.com.gashelper.simplecounts.CalcSpeedGasActivity;
import empoluboyarov.com.gashelper.simplecounts.CalcSpeedSoundActivity;
import empoluboyarov.com.gashelper.simplecounts.CalcZActivity;

public class MainActivity extends AppCompatActivity {

    Button btn1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        AdapterView.OnItemClickListener listener = new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                if (i == 0)
                    startActivity(new Intent(MainActivity.this, CalcZActivity.class));
                else if (i == 1)
                    startActivity(new Intent(MainActivity.this, CalcAdiabataActivity.class));
                else if (i == 2)
                    startActivity(new Intent(MainActivity.this, CalcFactDensActivity.class));
                else if (i == 3)
                    startActivity(new Intent(MainActivity.this, CalcGasCountActivity.class));
                else if (i == 4)
                    startActivity(new Intent(MainActivity.this, CalcSpeedSoundActivity.class));
                else if (i == 5)
                    startActivity(new Intent(MainActivity.this, CalcSpeedGasActivity.class));
                else if (i == 6)
                    startActivity(new Intent(MainActivity.this, DynamicCountActivity.class));
                else if (i == 7)
                    startActivity(new Intent(MainActivity.this, CalcProduvkaActivity.class));
                else if (i == 8)
                    startActivity(new Intent(MainActivity.this, CalcPropuskActivity.class));
                else if (i == 9)
                    startActivity(new Intent(MainActivity.this, CalcEffectivActivity.class));
                else if (i == 10)
                    startActivity(new Intent(MainActivity.this, CalcAverageActivity.class));
                else if (i == 11)
                    startActivity(new Intent(MainActivity.this, ConvertVolumeActivity.class));
                else if (i == 12)
                    startActivity(new Intent(MainActivity.this, DewPointActivity.class));
            }
        };
        ListView listView = (ListView) findViewById(R.id.list_main);
        listView.setOnItemClickListener(listener);
    }
}

//TODO разобраться с глюком при записи только плотность - попробовать удалить обнуление сообщение в каждом методе проверке
// а обнуление вызывать при обнулении isCheck
