package br.com.willfelix.ninjabluetooth;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import br.com.willfelix.ninjabluetooth.bluetooth.BluetoothController;
import br.com.willfelix.ninjabluetooth.utils.ActivityUtils;
import br.com.willfelix.ninjabluetooth.utils.NinjaListAdapter;
import br.com.willfelix.ninjabluetooth.utils.User;

public class ChatActivity extends AppCompatActivity {

    private NinjaListAdapter adapter;
    private BluetoothController controller;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);
        ActivityUtils.setFullScreen(this);

        adapter = new NinjaListAdapter(this);
        final ListView list = (ListView) findViewById(R.id.list);
        list.setAdapter(adapter);


        BluetoothController.createConnection(this, adapter);
        controller = BluetoothController.getInstance();
        controller.start();


        final Button submit = (Button) findViewById(R.id.btn_submit);
        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updateList();
                list.invalidateViews();
            }
        });


        EditText edit = (EditText) findViewById(R.id.input);
        edit.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_SEND) {
                    submit.performClick();
                    return true;
                }
                return false;
            }
        });

    }

    public void updateList() {
        EditText edit = (EditText) findViewById(R.id.input);
        String msg = edit.getText().toString();
        edit.setText("");

        if (msg != null && !msg.isEmpty() && !msg.trim().isEmpty()) {
            User user = User.getInstance();
            user.setMessage(msg);
            adapter.addMessage(user);
            adapter.notifyDataSetChanged();

            controller.sendMessage(user);
        }
    }

}
