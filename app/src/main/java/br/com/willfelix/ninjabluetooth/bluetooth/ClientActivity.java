package br.com.willfelix.ninjabluetooth.bluetooth;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.AsyncTask;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.Toast;

import java.io.IOException;
import java.util.ArrayList;
import java.util.UUID;

import br.com.willfelix.ninjabluetooth.utils.ActivityUtils;
import br.com.willfelix.ninjabluetooth.ChatActivity;
import br.com.willfelix.ninjabluetooth.MainActivity;
import br.com.willfelix.ninjabluetooth.R;

public class ClientActivity extends ActionBarActivity {
    private ListView listView;
    private ArrayAdapter<String> ninjaAdapter;
    private ArrayList<String> foundDevices = new ArrayList<String>();
    private ArrayList<BluetoothDevice> devices = new ArrayList<BluetoothDevice>();
    private BluetoothAdapter bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();

    private BluetoothSocket socket;
    private UUID uuid = BluetoothController.uuid;

    private BroadcastReceiver mReceiver = new BroadcastReceiver() {

        public void onReceive(Context context, Intent intent) {
            BluetoothDevice device = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
            if (device != null && !devices.contains(device)) {
                if ( "NinjaBluetooth".equals(device.getName()) ) {
                    devices.add(device);
                    foundDevices.add(device.getName());
                    ninjaAdapter.notifyDataSetChanged();
                }
            }
        }

    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_client);

        //Iniciando Busca de Dispositivos
        startDiscovery();

        ninjaAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_single_choice, foundDevices);
        listView = (ListView) findViewById(R.id.lista_connection_client);
        listView.setAdapter(ninjaAdapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> arg0, View view, int index, long arg3) {

                AsyncTask<Integer, Void, Void> connectTask = new AsyncTask<Integer, Void, Void>() {

                    @Override
                    protected Void doInBackground(Integer... params) {
                        try {
                            BluetoothDevice device = devices.get(params[0]);
                            socket = device.createRfcommSocketToServiceRecord(uuid);
                            socket.connect();
                            Log.d("CLIENT", "Socket Conectou - Cliente");
                        } catch (IOException e) {
                            Log.d("BLUETOOTH_CLIENT", e.getMessage());
                        }

                        return null;
                    }

                    @Override
                    protected void onPostExecute(Void result) {
                        devices.clear();

                        BluetoothController.setSocket(socket);

                        Intent i = new Intent(ClientActivity.this, ChatActivity.class);
                        startActivity(i);

                        finish();
                    }
                };
                connectTask.execute(index);

            }
        });
    }


    /**
     * Inicia Busca de Dispositivos;
     */
    public void startDiscovery() {
        IntentFilter filter = new IntentFilter(BluetoothDevice.ACTION_FOUND);
        registerReceiver(mReceiver, filter); // Don't forget to unregister during onDestroy
        if (!bluetoothAdapter.isDiscovering()) {
            devices.clear();
            foundDevices.clear();
            bluetoothAdapter.startDiscovery();
        }
    }

    /**
     * Para a Busca de Dispositivos;
     */
    public void stopDiscovery() {
        unregisterReceiver(mReceiver); // Don't forget to unregister during onDestroy
        bluetoothAdapter.cancelDiscovery();
    }

    @Override
    public void onBackPressed() {
        finish();
        startActivity(new Intent(this, MainActivity.class));
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        stopDiscovery();
    }

}