package br.com.willfelix.ninjabluetooth;

import android.bluetooth.BluetoothAdapter;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import br.com.willfelix.ninjabluetooth.bluetooth.BluetoothController;
import br.com.willfelix.ninjabluetooth.bluetooth.ClientActivity;
import br.com.willfelix.ninjabluetooth.bluetooth.ServerActivity;
import br.com.willfelix.ninjabluetooth.utils.ActivityUtils;
import br.com.willfelix.ninjabluetooth.utils.User;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        TextView t = (TextView) findViewById(R.id.title);
        t.setText(User.getInstance().getName());

        Button create = (Button) findViewById(R.id.btn_create);
        create.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                bluetooth(1);
            }
        });

        Button search = (Button) findViewById(R.id.btn_search);
        search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                bluetooth(2);
            }
        });
    }

    public void bluetooth(int code) {

        if (BluetoothController.bluetoothAdapter == null) {

            Toast.makeText(getApplicationContext(), "Seu aparelho não suporta bluetooth!", Toast.LENGTH_LONG).show();

        } else {

            Intent enableBtIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
            startActivityForResult(enableBtIntent, code);

        }

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == RESULT_OK) {

            Intent i;
            if (requestCode == 1) {
                i = new Intent(MainActivity.this, ServerActivity.class);
            } else {
                i = new Intent(MainActivity.this, ClientActivity.class);
            }

            startActivity(i);
            finish();

        }
    }
}
