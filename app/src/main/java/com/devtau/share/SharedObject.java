package com.devtau.share;

import android.content.Intent;
import android.net.Uri;
import android.os.Parcel;
import android.os.Parcelable;

import static com.devtau.share.SharedObject.SharedType.TEXT;
import static com.devtau.share.SharedObject.SharedType.AUDIO;
import static com.devtau.share.SharedObject.SharedType.DOC;
import static com.devtau.share.SharedObject.SharedType.IMAGE;
import static com.devtau.share.SharedObject.SharedType.VIDEO;

public class SharedObject implements Parcelable {

    private String sharedMIMEType;
    private SharedType sharedType;
    private Uri extraUri;
    private String extraText;


    public SharedObject(Intent intent) {
        if (intent == null) return;
        String action = intent.getAction();
        sharedMIMEType = intent.getType();

        if (Intent.ACTION_SEND.equals(action) && sharedMIMEType != null) {
            if (TEXT.equalsType(sharedMIMEType)) {
                sharedType = TEXT;
                extraText = intent.getStringExtra(Intent.EXTRA_TEXT);//просто текст
                extraUri = intent.getParcelableExtra(Intent.EXTRA_STREAM);//файл .txt
            } else if (IMAGE.equalsType(sharedMIMEType)) {
                sharedType = IMAGE;
                extraUri = intent.getParcelableExtra(Intent.EXTRA_STREAM);
            } else if (AUDIO.equalsType(sharedMIMEType)) {
                sharedType = AUDIO;
                extraUri = intent.getParcelableExtra(Intent.EXTRA_STREAM);
            } else if (VIDEO.equalsType(sharedMIMEType)) {
                sharedType = VIDEO;
                extraUri = intent.getParcelableExtra(Intent.EXTRA_STREAM);
            } else if (DOC.equalsType(sharedMIMEType)) {
                sharedType = DOC;
                extraUri = intent.getParcelableExtra(Intent.EXTRA_STREAM);
            }
        }
    }

    private SharedObject(Parcel in) {
        sharedMIMEType = in.readString();
        sharedType = (SharedType) in.readSerializable();
        extraUri = in.readParcelable(Uri.class.getClassLoader());
        extraText = in.readString();
    }

    public static final Creator<SharedObject> CREATOR = new Creator<SharedObject>() {
        @Override
        public SharedObject createFromParcel(Parcel in) {
            return new SharedObject(in);
        }

        @Override
        public SharedObject[] newArray(int size) {
            return new SharedObject[size];
        }
    };

    public String getSharedMIMEType() {
        return sharedMIMEType;
    }

    public SharedType getSharedType() {
        return sharedType;
    }

    public Uri getExtraUri() {
        return extraUri;
    }

    public String getExtraText() {
        return extraText;
    }


    public boolean hasExtraUri() {
        return extraUri != null;
    }

    public boolean hasExtraText() {
        return extraText != null;
    }


    public boolean isSupportedFile() {
        return isText() || isImage() || isAudio() || isVideo() || isDocument();
    }

    public boolean isText() {
        return sharedType == TEXT;
    }

    public boolean isImage() {
        return sharedType == SharedType.IMAGE;
    }

    public boolean isAudio() {
        return sharedType == SharedType.AUDIO;
    }

    public boolean isVideo() {
        return sharedType == SharedType.VIDEO;
    }

    public boolean isDocument() {
        return sharedType == SharedType.DOC;
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(sharedMIMEType);
        parcel.writeSerializable(sharedType);
        parcel.writeParcelable(extraUri, i);
        parcel.writeString(extraText);
    }


    enum SharedType {
        TEXT("text/"), IMAGE("image/"), AUDIO("audio/"), VIDEO("video/"), DOC("application/");

        private String prefix;

        SharedType(String prefix) {
            this.prefix = prefix;
        }

        private boolean equalsType(String mimeType) {
            return mimeType.startsWith(prefix);
        }
    }
}
