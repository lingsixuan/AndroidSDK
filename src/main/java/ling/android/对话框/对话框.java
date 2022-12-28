package ling.android.对话框;


import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.view.View;
import android.view.WindowManager;
//import androidx.constraintlayout.widget.ConstraintLayout;

import java.io.IOException;
import java.lang.reflect.Field;


public class 对话框 extends AlertDialog.Builder {
    private Context context;
    private AlertDialog dialog;
    //private AlertDialog.Builder builder;
    //private ConstraintLayout container;
    private View 组件;
    private boolean 显示 = false;
    private 对话框关闭事件 关闭事件;

    public 对话框(Context context) {
        super(context);
        this.context = context;
        //this.builder = new AlertDialog.Builder(context);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
            setOnDismissListener((v) -> {
                显示 = false;
                if (关闭事件 != null) {
                    关闭事件.被关闭();
                }
            });
        }
    }

    public 对话框(Context context, int 资源ID) {
        super(context, 资源ID);
        this.context = context;
        //this.builder = new AlertDialog.Builder(context);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
            setOnDismissListener((v) -> {
                显示 = false;
                if (关闭事件 != null) {
                    关闭事件.被关闭();
                }
            });
        }
    }

    public 对话框 标题(String 标题) {
        setTitle(标题);
        return this;
    }

    public 对话框 信息(String 信息) {
        setMessage(信息);
        return this;
    }

    public 对话框 图标(String 图片名称) {
        try {
            setIcon(Drawable.createFromStream(context.getAssets().open("图片名称"), "图片名称"));
        } catch (IOException e) {
            e.printStackTrace();
        }
        return this;
    }

    public 对话框 按钮1(String 文本, 按钮点击事件 object, boolean 可取消) {
        setPositiveButton(文本, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                    try {
                        Field field = dialog.getClass().getSuperclass().getDeclaredField("mShowing");
                        field.setAccessible(true);
                        field.set(dialog, 可取消);
                    } catch (NoSuchFieldException | IllegalAccessException e) {
                        e.printStackTrace();
                    }
                }
                if (object != null)
                    object.被单击();
            }
        });
        return this;
    }

    public 对话框 按钮1(String 文本, 按钮点击事件 object) {
        setPositiveButton(文本, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if (object != null)
                    object.被单击();
            }
        });
        return this;
    }

    public 对话框 按钮1(String 文本) {
        setPositiveButton(文本, null);
        return this;
    }

    public 对话框 按钮2(String 文本, 按钮点击事件 object) {
        setNegativeButton(文本, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if (object != null)
                    object.被单击();
            }
        });
        return this;
    }

    public 对话框 按钮2(String 文本, 按钮点击事件 object, boolean 可取消) {
        setNegativeButton(文本, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                    try {
                        Field field = dialog.getClass().getSuperclass().getDeclaredField("mShowing");
                        field.setAccessible(true);
                        field.set(dialog, 可取消);
                    } catch (NoSuchFieldException | IllegalAccessException e) {
                        e.printStackTrace();
                    }
                }
                if (object != null)
                    object.被单击();
            }
        });
        return this;
    }

    public 对话框 按钮2(String 文本) {
        setNegativeButton(文本, null);
        return this;
    }

    public 对话框 按钮3(String 文本, 按钮点击事件 object) {
        setNeutralButton(文本, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if (object != null)
                    object.被单击();
            }
        });
        return this;
    }

    public 对话框 按钮3(String 文本, 按钮点击事件 object, boolean 可取消) {
        setNeutralButton(文本, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                    try {
                        Field field = dialog.getClass().getSuperclass().getDeclaredField("mShowing");
                        field.setAccessible(true);
                        field.set(dialog, 可取消);
                    } catch (NoSuchFieldException | IllegalAccessException e) {
                        e.printStackTrace();
                    }
                }
                if (object != null)
                    object.被单击();
            }
        });
        return this;
    }

    public 对话框 按钮3(String 文本) {
        setNeutralButton(文本, null);
        return this;
    }

    public 对话框 组件(View 组件) {
        setView(组件);
        this.组件 = 组件;
        return this;
    }

    public 对话框 组件(int 组件ID) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            setView(组件ID);
        }
        return this;
    }

    public View 取组件() {
        return this.组件;
    }

    public 对话框 可取消(boolean 可取消) {
        setCancelable(可取消);
        if (dialog != null)
            dialog.setCancelable(可取消);
        return this;
    }

    public 对话框 显示() {
        if (dialog == null)
            dialog = create();
        dialog.show();
        显示 = true;
        return this;
    }

    public 对话框 显示(int type) {
        if (dialog == null)
            dialog = create();
        dialog.getWindow().setType(type);
        显示();
        return this;
    }

    public 对话框 隐藏() {
        dialog.hide();
        显示 = false;
        return this;
    }

    public void 设置关闭事件(对话框关闭事件 关闭事件) {
        this.关闭事件 = 关闭事件;
    }

    public 对话框 关闭() {
        dialog.dismiss();
        显示 = false;
        return this;
    }

    public boolean 是否显示() {
        return 显示;
    }

    public interface 按钮点击事件 {
        void 被单击();
    }

    public interface 对话框关闭事件 {
        void 被关闭();
    }

}
