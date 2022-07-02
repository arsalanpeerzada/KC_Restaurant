package com.example.smartrestaurant.network;

import android.os.Handler;
import android.os.Looper;


import com.example.smartrestaurant.util.AppLog;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

import okhttp3.MediaType;
import okhttp3.RequestBody;
import okio.BufferedSink;

/**
 * Created by Dell 5521 on 11/10/2017.
 */

public class ProgressRequestBody extends RequestBody {

    private static final String TAG = ProgressRequestBody.class.getSimpleName();
    private File mFile;
    private MediaType mMediaType;
    private UploadCallback mListener;

    private static final int DEFAULT_BUFFER_SIZE = 2048;

    public interface UploadCallback {
        void onProgressUpdate(int percentage, long uploaded, long total);
        void onError();
        void onFinish();
    }

    public ProgressRequestBody(MediaType mediaType, final File file, final UploadCallback listener) {
        mMediaType = mediaType;
        mFile = file;
        mListener = listener;
    }

    @Override
    public MediaType contentType() {
        return mMediaType;
    }

    @Override
    public long contentLength() throws IOException {
        return mFile.length();
    }

    @Override
    public void writeTo(BufferedSink sink) throws IOException {
        long fileLength = mFile.length();
        byte[] buffer = new byte[DEFAULT_BUFFER_SIZE];
        FileInputStream in = new FileInputStream(mFile);
        long uploaded = 0;

        long mb = 10 * 1024 * 1024;
        long interval = 0;

        try {
            int read;
            Handler handler = new Handler(Looper.getMainLooper());
            while ((read = in.read(buffer)) != -1) {

                uploaded += read;
                sink.write(buffer, 0, read);

                interval += read;
                if (interval > mb){
                    AppLog.d(TAG, "writeTo: " + uploaded);
                    interval = 0;
                }

                // update progress on UI thread
                handler.post(new ProgressUpdater(uploaded, fileLength));
            }
        } finally {
            in.close();
        }
    }

    private class ProgressUpdater implements Runnable {
        private long mUploaded;
        private long mTotal;
        public ProgressUpdater(long uploaded, long total) {
            mUploaded = uploaded;
            mTotal = total;
        }

        @Override
        public void run() {
            mListener.onProgressUpdate((int)(100 * mUploaded / mTotal), mUploaded, mTotal);
        }
    }
}

