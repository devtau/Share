package com.devtau.share;

import android.content.ClipData;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.support.v4.content.FileProvider;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.ShareActionProvider;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.CompoundButton;
import android.widget.ToggleButton;
import java.io.File;

public class MainActivity extends AppCompatActivity {

    private static final String LOG_TAG = "MainActivity";
    private MenuItem mMenuItemShare;

    private static final String IMAGE_PATH_ON_SD = Environment.getExternalStorageDirectory() + "/DCIM/Camera/kitty.jpg";
    private static final String VIDEO_PATH_ON_SD = Environment.getExternalStorageDirectory() + "/DCIM/Camera/panda.mp4";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
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
        Uri fileUri = buildUriFromPath(IMAGE_PATH_ON_SD);
        prepareFileShare(menuItemShare, "image/jpeg", fileUri);
    }

    private void prepareVideoShare(MenuItem menuItemShare) {
        Uri fileUri = buildUriFromPath(VIDEO_PATH_ON_SD);
        prepareFileShare(menuItemShare, "video/mp4", fileUri);
    }

    private void prepareFileShare(MenuItem menuItemShare, String fileType, Uri fileUri) {
        Intent shareImageIntent = new Intent(Intent.ACTION_SEND);
        shareImageIntent.setType(fileType);
        if (shareImageIntent.resolveActivity(getPackageManager()) != null) {
            //блок, не влияющий ни на что
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                shareImageIntent.setTypeAndNormalize(fileType);
                shareImageIntent.setClipData(new ClipData(null, new String[]{fileType}, new ClipData.Item(fileUri)));
            }
            shareImageIntent.putExtra(Intent.EXTRA_STREAM, fileUri);
            shareImageIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION | Intent.FLAG_GRANT_WRITE_URI_PERMISSION);
            ShareActionProvider providerShareImage = (ShareActionProvider) MenuItemCompat.getActionProvider(menuItemShare);
            providerShareImage.setShareIntent(shareImageIntent);
        } else {
            Log.e(LOG_TAG, "prepareFileShare no activity for share");
        }
    }

    private Uri buildUriFromPath(String validPath) {
        Uri fileUri;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            fileUri = FileProvider.getUriForFile(this, BuildConfig.APPLICATION_ID + ".fileprovider", new File(validPath));
        } else {
            fileUri = Uri.fromFile(new File(validPath));
        }
        Log.d(LOG_TAG, "buildUriFromPath fileUri is: " + fileUri);
        return fileUri;
    }
}
