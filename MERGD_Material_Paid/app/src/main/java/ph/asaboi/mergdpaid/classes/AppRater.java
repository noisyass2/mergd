package ph.asaboi.mergdpaid.classes;

import android.app.AlertDialog;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.view.MenuItem;

import ph.asaboi.mergdpaid.R;

/**
 * Created by neo on 3/9/2016.
 */
public class AppRater {
    public static void LaunchApp(Context context)
    {
        SharedPreferences prefs = context.getSharedPreferences("apprater", 0);
        if(prefs.getBoolean("nevershow",false)) {return;}

        SharedPreferences.Editor editor = prefs.edit();
        int launchCount =  prefs.getInt("launchcount",0) + 1;
        editor.putInt("launchcount", launchCount);

        Long firstLaunch = prefs.getLong("firstlaunch",0);
        if(firstLaunch == 0)
        {
            editor.putLong("firstlaunch",System.currentTimeMillis());

        }

        if(launchCount >= 3)
        {
            if(System.currentTimeMillis() >= firstLaunch + (3 * 24 * 60 * 60 * 1000))
            {
                showRateDialog(context,editor);
            }
        }
        editor.commit();
    }

    private static void showRateDialog(final Context context, final SharedPreferences.Editor editor) {

        AlertDialog.Builder diagBuilder = new AlertDialog.Builder(context);
        diagBuilder.setTitle("Rate MERGD")
                .setMessage("If you enjoy using MERGD, please take a moment to rate it. Thanks for your support!")
                .setPositiveButton("Rate Now!", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        ShowPackage(context, "ph.asaboi.mergd");
                        editor.putBoolean("nevershow", true);
                        editor.commit();
                        dialog.dismiss();
                    }
                })
                .setNeutralButton("Later", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                })
                .setNegativeButton("No, thanks", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if (editor != null) {
                            editor.putBoolean("nevershow", true);
                            editor.commit();

                        }
                        dialog.dismiss();
                    }
                });

        AlertDialog dialog = diagBuilder.create();

//        dialog.setContentView(ll);
        dialog.show();
    }

    public static void ShowPackage(Context context, String packageName) {
        Uri uri = Uri.parse("market://details?id=" + packageName);

        Intent rateIntent = new Intent(Intent.ACTION_VIEW,uri);
        rateIntent.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY | Intent.FLAG_ACTIVITY_CLEAR_WHEN_TASK_RESET | Intent.FLAG_ACTIVITY_MULTIPLE_TASK);
        try
        {
            context.startActivity(rateIntent);
        }catch (ActivityNotFoundException ex)
        {
            context.startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("http://play.google.com/store/apps/details?id=" + packageName)));
        }
        return;
    }

    public static boolean genMenuHandler(Context context, MenuItem item) {
        switch (item.getItemId())
        {
            case R.id.action_rate:
                String packageName = context.getPackageName();
                AppRater.ShowPackage(context, packageName);
                break;
            case R.id.action_buy:
                AppRater.ShowPackage(context, "ph.asaboi.mergdpaid");
                break;
        }
        return true;
    }
}
