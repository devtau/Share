package com.devtau.share;

import android.os.Build;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.Toast;
import android.widget.ToggleButton;
import java.util.Arrays;
/**
 * Активность, способная шарить файлы/текст вовне
 */
public class ShareEmitterActivity extends AppCompatActivity {

    private static final String IMAGE_PATH_ON_SD = Environment.getExternalStorageDirectory() + "/DCIM/Camera/kitty.jpg";
    private static final String VIDEO_PATH_ON_SD = Environment.getExternalStorageDirectory() + "/DCIM/Camera/panda.mp4";
    private MenuItem mMenuItemShare;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_share_emitter);
        ((ToggleButton) findViewById(R.id.image_or_video)).setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (mMenuItemShare == null) return;
                if (isChecked) prepareVideoShare(mMenuItemShare);
                else prepareImageShare(mMenuItemShare);
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.file_viewer_menu, menu);
        mMenuItemShare = menu.findItem(R.id.action_share);
        prepareImageShare(mMenuItemShare);
        return super.onCreateOptionsMenu(menu);
    }

    private void prepareImageShare(MenuItem menuItemShare) {
        ShareUtils.prepareFileShare(menuItemShare, "image/jpeg", IMAGE_PATH_ON_SD, this);
    }

    private void prepareVideoShare(MenuItem menuItemShare) {
        ShareUtils.prepareFileShare(menuItemShare, "video/mp4", VIDEO_PATH_ON_SD, this);
    }

    public void onDeviceInfoClick(View v) {
        String cpuModel;
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP) {
            cpuModel = Build.CPU_ABI + ", " + Build.CPU_ABI2;
        } else {
            cpuModel = Arrays.toString(Build.SUPPORTED_ABIS);
        }
        String msg = "deviceModel: " + Build.MODEL + '\n' + "cpuModel: " + cpuModel;
        Toast.makeText(v.getContext(), msg, Toast.LENGTH_LONG).show();
    }
}
