package ling.android.操作;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.provider.Settings;
import android.util.DisplayMetrics;
import android.view.WindowManager;

public class 系统操作 {
    public static int 取自身版本号(Context context) {
        try {
            PackageManager pack = context.getPackageManager();
            PackageInfo packageInfo = pack.getPackageInfo(context.getPackageName(), 0);
            return packageInfo.versionCode;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return 0;
    }

    public static String 取Android_id(Context context) {
        String android_id = Settings.System.getString(context.getContentResolver(),
                Settings.System.ANDROID_ID);
        return android_id;
    }

    public static void 置剪切板文本(Context context, String 内容) {
        ClipboardManager clipboard = null;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
            clipboard = (ClipboardManager)
                    context.getSystemService(Context.CLIPBOARD_SERVICE);
            ClipData clip = ClipData.newPlainText("剪切板", 内容);
            clipboard.setPrimaryClip(clip);
        }
    }

    public static synchronized String 取应用名称(Context context) {
        try {
            PackageManager packageManager = context.getPackageManager();
            PackageInfo packageInfo = packageManager.getPackageInfo(context.getPackageName(), 0);
            int labelRes = packageInfo.applicationInfo.labelRes;
            return context.getResources().getString(labelRes);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
            return null;
        }
    }

    public static synchronized String 取版本名称(Context context) {
        try {
            PackageManager packageManager = context.getPackageManager();
            PackageInfo packageInfo = packageManager.getPackageInfo(context.getPackageName(), 0);
            return packageInfo.versionName;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
            return null;
        }
    }

    public static synchronized Bitmap 取图标(Context context) {
        PackageManager packageManager = null;
        ApplicationInfo applicationInfo = null;
        try {
            packageManager = context.getApplicationContext().getPackageManager();
            applicationInfo = packageManager.getApplicationInfo(context.getPackageName(), 0);
        } catch (PackageManager.NameNotFoundException e) {
            applicationInfo = null;
        }
        Drawable d = packageManager.getApplicationIcon(applicationInfo);
        BitmapDrawable bd = (BitmapDrawable) d;
        Bitmap bm = bd.getBitmap();
        return bm;
    }

    public static int 取屏幕宽度(Context context) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
            WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
            DisplayMetrics outMetrics = new DisplayMetrics();
            wm.getDefaultDisplay().getRealMetrics(outMetrics);
            return outMetrics.widthPixels;
        }
        return 0;
    }

    public static int 取屏幕高度(Context context) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
            WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
            DisplayMetrics outMetrics = new DisplayMetrics();
            wm.getDefaultDisplay().getRealMetrics(outMetrics);
            return outMetrics.heightPixels;
        }
        return 0;
    }

    @TargetApi(Build.VERSION_CODES.KITKAT)
    public static int 取状态栏高度(Context context) {
        if (Build.VERSION.SDK_INT < 29) {
            try {
                @SuppressLint("PrivateApi") Class<?> c = Class.forName("com.android.internal.R$dimen");
                return context.getResources()
                        .getDimensionPixelSize(
                                Integer.parseInt(c.getField("status_bar_height").get(c.newInstance()).toString()));
            } catch (ClassNotFoundException | NoSuchFieldException | IllegalAccessException |
                     InstantiationException e) {
                e.printStackTrace();
                return 0;
            }
        } else {
            Resources resources = context.getResources();
            return resources.getDimensionPixelSize(resources.getIdentifier("status_bar_height", "dimen", "android"));
        }
    }
}
