package br.com.willfelix.ninjabluetooth.utils;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.view.View;

import br.com.willfelix.ninjabluetooth.LoginActivity;
import br.com.willfelix.ninjabluetooth.R;

/**
 * Created by willfelix on 8/5/15.
 */
public class ActivityUtils {

    public static void setFullScreen(Activity context) {
        context.getWindow().getDecorView().setSystemUiVisibility(
                View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                        | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                        | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                        | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION // hide nav bar
                        | View.SYSTEM_UI_FLAG_FULLSCREEN // hide status bar
                        | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY);
    }

    public static void createShortcut(Context context) {
        SharedPreferences appPreferences;
        boolean isAppInstalled = false;

        // check if application is running first time, only then create shorcut
        appPreferences = PreferenceManager.getDefaultSharedPreferences(context);
        isAppInstalled = appPreferences.getBoolean("isAppInstalled", false);
        if (isAppInstalled == false) {

            //create short code
            Intent shortcutIntent = new Intent(context, LoginActivity.class);
            shortcutIntent.setAction(Intent.ACTION_MAIN);
            Intent intent = new Intent();
            intent.putExtra(Intent.EXTRA_SHORTCUT_INTENT, shortcutIntent);
            intent.putExtra(Intent.EXTRA_SHORTCUT_NAME, "NinjaBluetooth");
            intent.putExtra(Intent.EXTRA_SHORTCUT_ICON_RESOURCE, Intent.ShortcutIconResource.fromContext(context, R.drawable.ninja));
            intent.setAction("com.android.launcher.action.INSTALL_SHORTCUT");
            context.sendBroadcast(intent);

            // Make preference true
            SharedPreferences.Editor editor = appPreferences.edit();
            editor.putBoolean("isAppInstalled", true);
            editor.commit();
        }
    }

}
