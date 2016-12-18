package com.example.library;

import android.app.Activity;
import android.content.Context;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.Handler;
import android.support.v4.view.MotionEventCompat;
import android.util.Log;
import android.view.MotionEvent;
import android.view.ViewConfiguration;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.VideoView;

import static android.view.MotionEvent.INVALID_POINTER_ID;

public class MainActivity extends Activity {
    private int mActivePointerId = INVALID_POINTER_ID;
    private EditText et;
    private AudioManager audio;
    private CustomView customView;
    private CircularSeekBar csk;
    private SeekView sv;
    private VideoView videoView;
    private MediaPlayer mediaPlayer;
    private int maxVolume;
    private int currentVolume;
    private int numberOfTaps = 0;
    private long lastTapTimeMs = 0;
    private long touchDownMs = 0;
    private double volper;
    private double per;
    private float brightness;
    private float seekdistance = 0;
    private float d = 0;
    private String onHorizontal;
    private String onVertical;
    private String onCircular;
    private boolean checkBrightness=true;
    private boolean checkVolume=true;
    private boolean checkSeek=true;

    public void set(Context context, VideoView vv) {
        customView = new CustomView(context);
        sv = new SeekView(context);
        brightness = android.provider.Settings.System.getFloat(getContentResolver(), android.provider.Settings.System.SCREEN_BRIGHTNESS, -1);
        WindowManager.LayoutParams layout = getWindow().getAttributes();
        layout.screenBrightness = brightness / 255;
        getWindow().setAttributes(layout);
        customView.setProgress((int) ((brightness / 255) * 100));
        customView.setProgressText(Integer.valueOf((int) ((brightness / 255) * 100)).toString() + "%");
        csk = new CircularSeekBar(context);
        audio = (AudioManager) getSystemService(Context.AUDIO_SERVICE);
        currentVolume = audio.getStreamVolume(AudioManager.STREAM_MUSIC);
        maxVolume = audio.getStreamMaxVolume(AudioManager.STREAM_MUSIC);
        videoView = vv;
    }

    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        final int action = MotionEventCompat.getActionMasked(ev);

        switch (action) {
            case MotionEvent.ACTION_DOWN: {
                seekdistance = 0;
                d = 0;
                touchDownMs = System.currentTimeMillis();
                break;
            }

            case MotionEvent.ACTION_MOVE: {
                final float x = ev.getX();
                final float y = ev.getY();
                d = getDistance(x, y, ev);
                try {
                    if (onVertical == "Brightness" && checkBrightness==true)
                        changeBrightness(ev.getHistoricalX(0, 0), ev.getHistoricalY(0, 0), x, y, d, "Y");
                    else if (onVertical == "Volume" && checkVolume==true)
                        changeVolume(ev.getHistoricalX(0, 0), ev.getHistoricalY(0, 0), x, y, d, "Y");
                    else if (onVertical == "Seek" && checkSeek==true) {
                        changeSeek(ev.getHistoricalX(0, 0), ev.getHistoricalY(0, 0), x, y, d, "Y");
                    }
                    if (onHorizontal == "Brightness" && checkBrightness==true)
                        changeBrightness(ev.getHistoricalX(0, 0), ev.getHistoricalY(0, 0), x, y, d, "X");
                    else if (onHorizontal == "Volume" && checkVolume==true)
                        changeVolume(ev.getHistoricalX(0, 0), ev.getHistoricalY(0, 0), x, y, d, "X");
                    else if (onHorizontal == "Seek" && checkSeek==true) {
                        changeSeek(ev.getHistoricalX(0, 0), ev.getHistoricalY(0, 0), x, y, d, "X");
                    }
                } catch (IllegalArgumentException e) {

                }

                break;
            }

            case MotionEvent.ACTION_UP: {
                Handler handler = new Handler();
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        if (customView.isVisible())
                            customView.hide();
                        if (sv.isVisible())
                            sv.hide();
                    }
                }, 2000);

                if ((System.currentTimeMillis() - touchDownMs) > ViewConfiguration.getTapTimeout()) {
                    numberOfTaps = 0;
                    lastTapTimeMs = 0;
                    break;
                }

                if (numberOfTaps > 0 && (System.currentTimeMillis() - lastTapTimeMs) < ViewConfiguration.getDoubleTapTimeout()) {
                    numberOfTaps += 1;
                } else {
                    numberOfTaps = 1;
                }

                lastTapTimeMs = System.currentTimeMillis();
                if (numberOfTaps == 2) {
                    if (onCircular == "Brightness") {
                        csk.setType("Brightness");
                        if (csk.isVisibile())
                            csk.hide();
                        else
                            csk.show();
                    } else if (onCircular == "Volume") {
                        csk.setType("Volume");
                        if (csk.isVisibile())
                            csk.hide();
                        else
                            csk.show();
                    }
                }
                mActivePointerId = INVALID_POINTER_ID;
                break;
            }
            case MotionEvent.ACTION_CANCEL: {
                mActivePointerId = INVALID_POINTER_ID;
                break;
            }
            case MotionEvent.ACTION_POINTER_UP: {

                final int pointerIndex = MotionEventCompat.getActionIndex(ev);
                final int pointerId = MotionEventCompat.getPointerId(ev, pointerIndex);

                if (pointerId == mActivePointerId) {
                    final int newPointerIndex = pointerIndex == 0 ? 1 : 0;
                    mActivePointerId = MotionEventCompat.getPointerId(ev, newPointerIndex);
                }
                break;
            }
        }
        return true;
    }

    public void disableBrightness()
    {
        checkBrightness=false;
    }
    public void disableSeek()
    {
        checkSeek=false;
    }
    public void disableVolume()
    {
        checkVolume=false;
    }

    public void enableBrightness()
    {
        checkBrightness=true;
    }
    public void enableSeek()
    {
        checkSeek=true;
    }
    public void enableVolume()
    {
        checkVolume=true;
    }

    public void changeVolume(float X, float Y, float x, float y, float d, String type) {
        customView.setTitle(" Volume  ");
        if (type == "Y" && x == X) {
            if (y < Y) {
                d = d / 100;
                commonVolume(d);
            } else {
                d = d / 150;
                commonVolume(-d);
            }
        } else if (type == "X" && y == Y) {
            Log.e("pul", "in if");
            if (x > X) {
                d = d / 150;
                commonVolume(d);
            } else {
                d = d / 150;
                commonVolume(-d);
            }
        }
    }
    public void commonVolume(float d) {
        currentVolume = audio.getStreamVolume(AudioManager.STREAM_MUSIC);
        per = (double) currentVolume / (double) maxVolume;
        Log.e("per", per + "");
        if (per + d <= 1 && per + d >= 0) {
            customView.show();
            if(d>0.05||d<-0.05) {
                customView.setProgress((int) ((per + d) * 100));
                customView.setProgressText((int) ((per + d) * 100) + "%");
                volper = (per + (double) d);
                audio.setStreamVolume(AudioManager.STREAM_MUSIC, (int) (volper * 15), 0);
            }
        }
    }
    public void changeBrightness(float X, float Y, float x, float y, float d, String type) {
        customView.setTitle("Brightness");
        if (type == "Y" && x == X) {
            d = d / 270;
            if (y < Y) {
                commonBrightness(d);
            } else {
                commonBrightness(-d);
            }
        } else if (type == "X" && y == Y) {
            d = d / 160;
            if (x > X) {
                commonBrightness(d);
            } else {
                commonBrightness(-d);
            }
        }
    }

    public void commonBrightness(float d) {
        WindowManager.LayoutParams layout = getWindow().getAttributes();
        if (getWindow().getAttributes().screenBrightness + d <= 1 && getWindow().getAttributes().screenBrightness + d >= 0) {
            customView.show();
            if((int) ((getWindow().getAttributes().screenBrightness + d) * 100)>100)
                customView.setProgress(100);
            else
            if((int) ((getWindow().getAttributes().screenBrightness + d) * 100)<0)
                customView.setProgress(0);
            else
                customView.setProgress((int) ((getWindow().getAttributes().screenBrightness + d) * 100));
            layout.screenBrightness = getWindow().getAttributes().screenBrightness + d;
            getWindow().setAttributes(layout);
            customView.setProgressText(Integer.valueOf((int) ((getWindow().getAttributes().screenBrightness + d) * 100)).toString() + "%");
        }
    }

    public void changeSeek(float X, float Y, float x, float y, float d, String type) {

        if (type == "Y" && x == X) {
            d = d / 300;
            if (y < Y) {
                seekCommon(d);
            } else {
                seekCommon(-d);
            }
        } else if (type == "X" && y == Y) {
            d = d / 200;
            if (x > X) {
                seekCommon(d);
            } else {
                seekCommon(-d);
            }
        }
    }

    public void seekCommon(float d) {
        seekdistance += d * 60000;
        sv.show();
        if (mediaPlayer != null) {
            Log.e("after", mediaPlayer.getCurrentPosition() + (int) (d * 60000) + "");
            Log.e("seek distance", (int) (seekdistance) + "");
            if (mediaPlayer.getCurrentPosition() + (int) (d * 60000) > 0 && mediaPlayer.getCurrentPosition() + (int) (d * 60000) < mediaPlayer.getDuration() + 10) {
                mediaPlayer.seekTo(mediaPlayer.getCurrentPosition() + (int) (d * 60000));
                if (seekdistance > 0)
                    sv.setText("+" + Math.abs((int) (seekdistance / 60000)) + ":" + String.valueOf(Math.abs((int) ((seekdistance) % 60000))).substring(0, 2) + "(" + (int) ((mediaPlayer.getCurrentPosition() + (int) (d * 60000)) / 60000) + ":" + String.valueOf((int) ((mediaPlayer.getCurrentPosition() + (int) (d * 60000)) % 60000)).substring(0, 2) + ")");
                else
                    sv.setText("-" + Math.abs((int) (seekdistance / 60000)) + ":" + String.valueOf(Math.abs((int) ((seekdistance) % 60000))).substring(0, 2) + "(" + (int) ((mediaPlayer.getCurrentPosition() + (int) (d * 60000)) / 60000) + ":" + String.valueOf((int) ((mediaPlayer.getCurrentPosition() + (int) (d * 60000)) % 60000)).substring(0, 2) + ")");
            }
        } else if (videoView != null) {
            Log.e("after", videoView.getCurrentPosition() + (int) (d * 60000) + "");
            Log.e("seek distance", (int) (seekdistance) + "");
            if (videoView.getCurrentPosition() + (int) (d * 60000) > 0 && videoView.getCurrentPosition() + (int) (d * 60000) < videoView.getDuration() + 10) {
                videoView.seekTo(videoView.getCurrentPosition() + (int) (d * 60000));
                if (seekdistance > 0)
                    sv.setText("+" + Math.abs((int) (seekdistance / 60000)) + ":" + String.valueOf(Math.abs((int) ((seekdistance) % 60000))).substring(0, 2) + "(" + (int) ((videoView.getCurrentPosition() + (int) (d * 60000)) / 60000) + ":" + String.valueOf((int) ((videoView.getCurrentPosition() + (int) (d * 60000)) % 60000)).substring(0, 2) + ")");
                else
                    sv.setText("-" + Math.abs((int) (seekdistance / 60000)) + ":" + String.valueOf(Math.abs((int) ((seekdistance) % 60000))).substring(0, 2) + "(" + (int) ((videoView.getCurrentPosition() + (int) (d * 60000)) / 60000) + ":" + String.valueOf((int) ((videoView.getCurrentPosition() + (int) (d * 60000)) % 60000)).substring(0, 2) + ")");

            }
        }
    }
    public void Seek(String type, VideoView v) {

        if (type == "vertical")
            onVertical = "Seek";
        else if (type == "horizontal")
            onHorizontal = "Seek";
        videoView = v;
    }
    public void Seek(String type, MediaPlayer v) {

        if (type == "vertical")
            onVertical = "Seek";
        else if (type == "hoizontal")
            onHorizontal = "Seek";
        mediaPlayer = v;
    }

    public void Brightness(String type) {

        if (type == "vertical")
            onVertical = "Brightness";
        else if (type == "horizontal")
            onHorizontal = "Brightness";
        else if (type == "circular")
            onCircular = "Brightness";
    }

    public void Volume(String type) {

        if (type == "vertical")
            onVertical = "Volume";
        else if (type == "horizontal")
            onHorizontal = "Volume";
        else if (type == "circular")
            onCircular = "Volume";
    }


    float getDistance(float startX, float startY, MotionEvent ev) {
        float distanceSum = 0;
        final int historySize = ev.getHistorySize();
        for (int h = 0; h < historySize; h++) {
            float hx = ev.getHistoricalX(0, h);
            float hy = ev.getHistoricalY(0, h);
            float dx = (hx - startX);
            float dy = (hy - startY);
            distanceSum += Math.sqrt(dx * dx + dy * dy);
            startX = hx;
            startY = hy;
        }
        float dx = (ev.getX(0) - startX);
        float dy = (ev.getY(0) - startY);
        distanceSum += Math.sqrt(dx * dx + dy * dy);
        return distanceSum;
    }
}
