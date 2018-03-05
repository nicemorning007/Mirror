package cn.nicemorning.mymirror.util;

import android.media.AudioFormat;
import android.media.AudioRecord;

import android.media.MediaRecorder;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

/**
 * Created by Nicemorning on 05-Mar-18.
 */

public class AudioRecordManger {
    private static final String TAG = "AudioRecord";
    private static final int SAMPLE_RATE_IN_HZ = 8000;
    private static final int BUFFER_SIZE = AudioRecord.getMinBufferSize(SAMPLE_RATE_IN_HZ,
            AudioFormat.CHANNEL_IN_DEFAULT, AudioFormat.ENCODING_PCM_16BIT);
    private AudioRecord audioRecord;
    private Handler handler;
    private int what;
    public Object lock;
    public boolean isGetVoiceRun;

    public AudioRecordManger(Handler handler, int what) {
        lock = new Object();
        this.handler = handler;
        this.what = what;
    }

    public void getNoiseLevel() {
        if (isGetVoiceRun) {
            Log.d(TAG, "Still recording");
            return;
        }
        audioRecord = new AudioRecord(MediaRecorder.AudioSource.MIC, SAMPLE_RATE_IN_HZ,
                AudioFormat.CHANNEL_IN_DEFAULT, AudioFormat.ENCODING_PCM_16BIT, BUFFER_SIZE);
        if (audioRecord == null) {
            Log.d(TAG, "AudioRecord initialization failed");
        }
        isGetVoiceRun = true;
        new Thread(new Runnable() {
            @Override
            public void run() {
                audioRecord.startRecording();
                short[] buffer = new short[BUFFER_SIZE];
                while (isGetVoiceRun) {
                    int r = audioRecord.read(buffer, 0, BUFFER_SIZE);
                    long v = 0L;
                    for (short i : buffer) {
                        v += i * i;
                    }
                    double mean = v / (double) r;
                    double volume = 10 * Math.log10(mean);
                    synchronized (lock) {
                        try {
                            lock.wait(500);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                    Message message = Message.obtain();
                    message.what = what;
                    message.obj = volume;
                    handler.sendMessage(message);
                }
                if (audioRecord != null) {
                    audioRecord.stop();
                    audioRecord.release();
                    audioRecord = null;
                }
            }
        }).start();

    }

}
