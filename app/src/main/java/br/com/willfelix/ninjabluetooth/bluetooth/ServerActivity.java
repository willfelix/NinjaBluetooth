package br.com.willfelix.ninjabluetooth.bluetooth;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothServerSocket;
import android.bluetooth.BluetoothSocket;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;

import java.io.IOException;
import java.util.UUID;

import br.com.willfelix.ninjabluetooth.utils.ActivityUtils;
import br.com.willfelix.ninjabluetooth.ChatActivity;
import br.com.willfelix.ninjabluetooth.MainActivity;
import br.com.willfelix.ninjabluetooth.R;

public class ServerActivity extends ActionBarActivity {

    private final int BLUETOOTH_DISCOVER = 1;
    private BluetoothSocket socket;
    private BluetoothServerSocket serverSocket;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_server);
        ActivityUtils.setFullScreen(this);

        Intent discover = new Intent(BluetoothAdapter.ACTION_REQUEST_DISCOVERABLE);
        discover.putExtra(BluetoothAdapter.EXTRA_DISCOVERABLE_DURATION, 300);
        startActivityForResult(discover, BLUETOOTH_DISCOVER);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == 300) {
            if (requestCode == BLUETOOTH_DISCOVER) {

                try {
                    UUID uuid = BluetoothController.uuid;
                    BluetoothAdapter bAdapter = BluetoothController.bluetoothAdapter;

                    Log.d("BLUETOOTH", "Iniciando Thread");
                    bAdapter.setName("NinjaBluetooth");
                    serverSocket = bAdapter.listenUsingRfcommWithServiceRecord("Ninjandroid", uuid);
                    AsyncTask<Integer, Void, BluetoothSocket> acceptThread = new AsyncTask<Integer, Void, BluetoothSocket>() {
                        @Override
                        protected BluetoothSocket doInBackground(Integer... params) {
                            try {
                                socket = serverSocket.accept(params[0] * 1000);
                                return socket;
                            } catch (IOException e) {
                                Log.d("BLUETOOTH", e.getMessage());
                            }
                            return null;
                        }

                        @Override
                        protected void onPostExecute(BluetoothSocket result) {
                            if (result != null) {

                                BluetoothController.setSocket(socket);

                                Intent i = new Intent(ServerActivity.this, ChatActivity.class);
                                startActivity(i);

                                finish();
                            }
                        }
                    };

                    acceptThread.execute(resultCode);
                } catch (IOException e1) {
                    e1.printStackTrace();
                }

            }
        } else {
            finish();
            startActivity(new Intent(this, MainActivity.class));
        }
    }

    @Override
    public void onBackPressed() {
        finish();
        startActivity(new Intent(this, MainActivity.class));
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        try {
            if (serverSocket != null)
                serverSocket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
