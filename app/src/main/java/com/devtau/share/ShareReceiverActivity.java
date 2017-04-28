package com.devtau.share;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;
import android.widget.MediaController;
import android.widget.TextView;
import android.widget.VideoView;
import com.bumptech.glide.Glide;
/**
 * Активность, способная получать расшаренные файлы/текст извне
 */
public class ShareReceiverActivity extends AppCompatActivity {

	private TextView mMimeTypeView;
	private TextView mExtraTextTextView;
	private TextView mExtraDataTextView;
	private View mImageContainer;
	private ImageView mExtraImageView;
	private View mAVContainer;
	private VideoView mExtraAVView;


	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_share_receiver);
		initViews();
		updateUi(new SharedObject(getIntent()));
	}

	@Override
	protected void onNewIntent(Intent intent) {
		this.setIntent(intent);
		updateUi(new SharedObject(getIntent()));
	}


	private void initViews() {
		mMimeTypeView = (TextView) findViewById(R.id.mime_type);
		mExtraTextTextView = (TextView) findViewById(R.id.extra_text);
		mExtraDataTextView = (TextView) findViewById(R.id.extra_data);
		mImageContainer = findViewById(R.id.image_container);
		mExtraImageView = (ImageView) findViewById(R.id.extra_image);
		mAVContainer = findViewById(R.id.av_container);
		mExtraAVView = (VideoView) findViewById(R.id.extra_av);
	}

	private void updateUi(SharedObject sharedObject) {
		if (mMimeTypeView != null && sharedObject.getSharedMIMEType() != null) {
			mMimeTypeView.setText(sharedObject.getSharedMIMEType());
		}
		if (mExtraTextTextView != null && sharedObject.getExtraText() != null) {
			mExtraTextTextView.setText(sharedObject.getExtraText());
		}
		if (mExtraDataTextView != null && sharedObject.getExtraUri() != null) {
			mExtraDataTextView.setText(sharedObject.getExtraUri().toString());
		}

		mImageContainer.setVisibility(View.GONE);
		mAVContainer.setVisibility(View.GONE);
		switch (sharedObject.getSharedType()) {
			case IMAGE:
				mImageContainer.setVisibility(View.VISIBLE);
				Glide.with(this).load(sharedObject.getExtraUri()).into(mExtraImageView);
				break;
			case AUDIO:
			case VIDEO:
				mAVContainer.setVisibility(View.VISIBLE);
				mExtraAVView.setVideoURI(sharedObject.getExtraUri());
				mExtraAVView.setMediaController(new MediaController(this));
				break;
		}
	}
}
