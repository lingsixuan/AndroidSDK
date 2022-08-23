package ling.android.操作;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;

public class APP操作 {

    public static boolean 应用是否安装(Context context, String pkgName) {
        if (pkgName== null || pkgName.isEmpty()) {
            return false;
        }
        PackageInfo packageInfo;
        try {
            packageInfo = context.getPackageManager().getPackageInfo(pkgName, 0);
        } catch (PackageManager.NameNotFoundException e) {
            packageInfo = null;
            e.printStackTrace();
        }
        if(packageInfo == null) {
            return false;
        } else {
            return true;
        }
    }

}
