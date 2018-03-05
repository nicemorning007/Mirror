package cn.nicemorning.mymirror.util;

import android.app.Activity;
import android.content.ContentResolver;
import android.net.Uri;
import android.provider.Settings;
import android.view.WindowManager;

/**
 * Created by Nicemorning on 05-Mar-18.
 */

public class SetBrightness {
    public static boolean isAutoBrightness(ContentResolver contentResolver) {
        boolean automicBrightness = false;
        try {
            automicBrightness = Settings.System.getInt(contentResolver,
                    Settings.System.SCREEN_BRIGHTNESS_MODE) ==
                    Settings.System.SCREEN_BRIGHTNESS_MODE_AUTOMATIC;
        } catch (Settings.SettingNotFoundException e) {
            e.printStackTrace();
        }
        return automicBrightness;
    }

    public static int getScreenBrightness(Activity activity) {
        int nowBrightnessValue = 0;
        ContentResolver resolver = activity.getContentResolver();
        try {
            nowBrightnessValue = Settings.System.getInt(
                    resolver, Settings.System.SCREEN_BRIGHTNESS
            );
        } catch (Settings.SettingNotFoundException e) {
            e.printStackTrace();
        }
        return nowBrightnessValue;
    }

    public static void setBrightness(Activity activity, int brightness) {
        WindowManager.LayoutParams layoutParams = activity.getWindow().getAttributes();
        layoutParams.screenBrightness = Float.valueOf(brightness) * (1F / 255F);
        activity.getWindow().setAttributes(layoutParams);
    }

    public static void stopAutoBrightness(Activity activity) {
        Settings.System.putInt(activity.getContentResolver(),
                Settings.System.SCREEN_BRIGHTNESS,
                Settings.System.SCREEN_BRIGHTNESS_MODE_MANUAL);
    }

    public static void startAutoBrightness(Activity activity) {
        Settings.System.putInt(activity.getContentResolver(),
                Settings.System.SCREEN_BRIGHTNESS_MODE,
                Settings.System.SCREEN_BRIGHTNESS_MODE_AUTOMATIC);
    }

    public static void saveBrightness(ContentResolver contentResolver, int brightness) {
        Uri uri = android.provider.Settings.System.getUriFor("screen_brightness");
        android.provider.Settings.System.putInt(contentResolver,
                "screen_brightness", brightness);
        contentResolver.notifyChange(uri, null);
    }

    public static ContentResolver getResolver(Activity activity) {
        return activity.getContentResolver();
    }

}
