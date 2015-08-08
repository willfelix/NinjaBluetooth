package br.com.willfelix.ninjabluetooth.bluetooth;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothSocket;
import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import android.os.Vibrator;
import android.util.Log;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.util.UUID;

import br.com.willfelix.ninjabluetooth.utils.NinjaListAdapter;
import br.com.willfelix.ninjabluetooth.utils.User;

/**
 * Created by willfelix on 8/5/15.
 */
public class BluetoothController extends Thread {

    private final InputStream mmInStream;

    private final OutputStream mmOutStream;

    private final NinjaListAdapter adapter;

    private String text;

    private Context context;

    private static BluetoothSocket socket;

    private static BluetoothController controller = null;

    public final static BluetoothAdapter bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();

    public final static UUID uuid = UUID.fromString("a60f35f0-b93a-11de-8a39-08002009c666");


    private BluetoothController(Context context, NinjaListAdapter adapter) {
        this.context = context;
        this.adapter = adapter;

        InputStream tmpIn = null;
        OutputStream tmpOut = null;
        try {
            tmpIn = socket.getInputStream();
            tmpOut = socket.getOutputStream();
        } catch (IOException e) {
        }

        mmInStream = tmpIn;
        mmOutStream = tmpOut;
    }


    public static BluetoothController getInstance() {
        return controller;
    }

    public static void createConnection(Context context, NinjaListAdapter adapter) {
        controller = new BluetoothController(context, adapter);
    }

    public void run() {
        // Keep listening to the InputStream until an exception occurs
        while (isConnected()) {

            try {

                InputStream is = socket.getInputStream();
                ObjectInputStream ois = new ObjectInputStream(is);
                final User user = (User) ois.readObject();
                if (user != null) {

                    // Send the obtained bytes to the UI activity
                    Handler handler = new Handler(Looper.getMainLooper());
                    handler.post(new Runnable() {
                        public void run() {

                            controller.receiveMessage(user);

                        }
                    });
                }

            } catch (IOException e) {
                Log.e("B_NINJA", "IOException", e);
                e.printStackTrace();
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }
        }

    }

    private boolean isConnected() {
        if (!socket.isConnected()) {

            try {
                socket.connect();
            } catch (IOException e) {
                e.printStackTrace();
                return false;
            }

        }

        return true;
    }

    public void sendMessage(User user) {
        try {
            ObjectOutputStream oos = new ObjectOutputStream(socket.getOutputStream());
            oos.writeObject(user);
        } catch (IOException e) {
            Log.d("B_NINJA", e.getMessage());
        }
    }

    public void receiveMessage(User user) {
        adapter.addMessage(user, true);
        adapter.notifyDataSetChanged();

        Vibrator v = (Vibrator) context.getSystemService(Context.VIBRATOR_SERVICE);
        v.vibrate(200);

    }

    /* Call this from the main activity to shutdown the connection */
    public void cancel() {
        try {
            socket.close();
        } catch (IOException e) {
        }
    }

    public String getText() {
        if (text == null)
            text = "";

        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public static BluetoothSocket getSocket() {
        return socket;
    }

    public static void setSocket(BluetoothSocket socket) {
        BluetoothController.socket = socket;
    }

}