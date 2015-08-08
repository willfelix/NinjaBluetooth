package br.com.willfelix.ninjabluetooth;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.net.Uri;
import android.os.ParcelFileDescriptor;
import android.provider.SyncStateContract;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import java.io.ByteArrayOutputStream;
import java.io.FileDescriptor;

import br.com.willfelix.ninjabluetooth.utils.User;

public class LoginActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        getSupportActionBar().setTitle("NinjaBluetooth");

        ImageView profile = (ImageView) findViewById(R.id.img_profile);
        profile.setOnClickListener(new View.OnClickListener() {
            public void onClick(View arg0) {
                Intent intent = new Intent();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(Intent.createChooser(intent, "Select Picture"), 1);
            }
        });

        Button btn = (Button) findViewById(R.id.btn_login);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EditText edit = (EditText) findViewById(R.id.editText);
                String user = edit.getText().toString();

                if (user != null && !user.isEmpty()) {
                    User.getInstance().setName(user);
                    startActivity(new Intent(LoginActivity.this, MainActivity.class));
                } else {
                    Toast.makeText(LoginActivity.this, "Digite seu nome antes de prosseguir", Toast.LENGTH_LONG).show();
                }
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK) {
            if (requestCode == 1) {
                Uri selectedImageUri = data.getData();
                ImageView imgView = (ImageView) findViewById(R.id.img_profile);

                try {
                    ParcelFileDescriptor parcelFileDescriptor = this.getContentResolver().openFileDescriptor(selectedImageUri, "r");
                    FileDescriptor fileDescriptor = parcelFileDescriptor.getFileDescriptor();
                    Bitmap image = BitmapFactory.decodeFileDescriptor(fileDescriptor);
                    parcelFileDescriptor.close();

                    /**
                     *  Set image to User
                     */
                    ByteArrayOutputStream bytes = new ByteArrayOutputStream();
                    image.compress(Bitmap.CompressFormat.JPEG, 40, bytes);
                    User.getInstance().setImage(bytes.toByteArray());

                    Bitmap bitmap = Bitmap.createScaledBitmap(image, 250, 250, true);
                    Bitmap result = Bitmap.createBitmap(200, 200, Bitmap.Config.ARGB_8888);
                    Canvas canvas = new Canvas(result);

                    int color = 0xff424242;
                    Paint paint = new Paint();
                    Rect rect = new Rect(0, 0, 200, 200);

                    paint.setAntiAlias(true);
                    canvas.drawARGB(0, 0, 0, 0);
                    paint.setColor(color);
                    canvas.drawCircle(100, 100, 100, paint);
                    paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));
                    canvas.drawBitmap(bitmap, rect, rect, paint);

                    imgView.setImageBitmap(image);
                } catch (Exception e) {
                    e.printStackTrace();
                }

            }
        }

        super.onActivityResult(requestCode, resultCode, data);
    }

}
