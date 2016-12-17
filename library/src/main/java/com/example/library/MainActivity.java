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
    private CustomView cv;
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
    private float d;
    private float seekdistance = 0;
    private float volumeSum = 0;
    private String onHorizontal;
    private String onVertical;
    private String onCircular;

    public void set(Context context, VideoView vv) {
        cv = new CustomView(context);
        sv = new SeekView(context);
        brightness = android.provider.Settings.System.getFloat(getContentResolver(), android.provider.Settings.System.SCREEN_BRIGHTNESS, -1);
        WindowManager.LayoutParams layout = getWindow().getAttributes();
        layout.screenBrightness = brightness / 255;
        getWindow().setAttributes(layout);
        cv.setProgress((int) ((brightness / 255) * 100));
        cv.setProgressText(Integer.valueOf((int) ((brightness / 255) * 100)).toString() + "%");
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
                volumeSum = 0;
                touchDownMs = System.currentTimeMillis();
                break;
            }

            case MotionEvent.ACTION_MOVE: {
                final float x = ev.getX();
                final float y = ev.getY();
                d = getDistance(x, y, ev);
                try {
//                    Seek(ev.getHistoricalX(0, 0), ev.getHistoricalY(0, 0), x, y, d, "X");

                    if(onVertical=="Brightness")
                        Brightness(ev.getHistoricalX(0, 0),ev.getHistoricalY(0, 0),x,y,d,"Y");
                    else
                        if(onVertical=="Volume")
                            Volume(ev.getHistoricalX(0, 0),ev.getHistoricalY(0, 0),x,y,d,"Y");
                    else
                         if(onVertical=="Seek")
                         {
                             Seek(ev.getHistoricalX(0, 0),ev.getHistoricalY(0, 0),x,y,d,"Y");
                         }


                    if(onHorizontal=="Brightness")
                        Brightness(ev.getHistoricalX(0, 0),ev.getHistoricalY(0, 0),x,y,d,"X");
                    else
                    if(onHorizontal=="Volume")
                        Volume(ev.getHistoricalX(0, 0),ev.getHistoricalY(0, 0),x,y,d,"X");
                    else
                    if(onHorizontal=="Seek")
                    {
                        Seek(ev.getHistoricalX(0, 0),ev.getHistoricalY(0, 0),x,y,d,"X");
                    }



//                   Volume(ev.getHistoricalX(0, 0),ev.getHistoricalY(0, 0),x,y,d,"Y");

                    /*if (x == ev.getHistoricalX(0, 0)) {
                        d = (getDistance(x, y, ev) / 270);
                        cv.setTitle("Brightness");
                        if (y < ev.getHistoricalY(0, 0)) {
                            WindowManager.LayoutParams layout = getWindow().getAttributes();
                            if (getWindow().getAttributes().screenBrightness + (float) (getDistance(x, y, ev) / 270) <= 1 && getWindow().getAttributes().screenBrightness + (float) (getDistance(x, y, ev) / 270) >= 0) {
                                cv.show();
                                cv.setProgress((int) ((getWindow().getAttributes().screenBrightness + (float) (getDistance(x, y, ev) / 270)) * 100));
                                layout.screenBrightness = getWindow().getAttributes().screenBrightness + (float) (getDistance(x, y, ev) / 270);
                                getWindow().setAttributes(layout);
                                cv.setProgressText(Integer.valueOf((int) ((getWindow().getAttributes().screenBrightness + (float) (getDistance(x, y, ev) / 270)) * 100)).toString() + "%");

                            } else {
                                cv.show();
                                cv.setProgress(100);
                                layout.screenBrightness = 1;
                                getWindow().setAttributes(layout);
                                cv.setProgressText("100" + "%");

                            }
                        } else {
                            Log.e("p", "in else");
                            WindowManager.LayoutParams layout = getWindow().getAttributes();
                            if (getWindow().getAttributes().screenBrightness - (float) (getDistance(x, y, ev) / 270) >= 0 && getWindow().getAttributes().screenBrightness - (float) (getDistance(x, y, ev) / 270) <= 1) {
                                cv.show();
                                cv.setProgress((int) ((getWindow().getAttributes().screenBrightness - (float) (getDistance(x, y, ev) / 270)) * 100));
                                layout.screenBrightness = getWindow().getAttributes().screenBrightness - (float) (getDistance(x, y, ev) / 270);
                                getWindow().setAttributes(layout);
                                cv.setProgressText(Integer.valueOf((int) ((getWindow().getAttributes().screenBrightness - (float) (getDistance(x, y, ev) / 270)) * 100)).toString() + "%");
                            } else {
                                cv.show();
                                cv.setProgress(0);
                                layout.screenBrightness = 0;
                                getWindow().setAttributes(layout);
                                cv.setProgressText("0" + "%");
                            }
                        }
                    }*/
                } catch (IllegalArgumentException e) {

                }
                try {
//                    Volume(ev.getHistoricalX(0, 0), ev.getHistoricalY(0, 0), x, y, d, "X");
                  /*  if (y == ev.getHistoricalY(0, 0)) {
                        cv.setTitle("  Volume     ");
                        if (x > ev.getHistoricalX(0, 0)) {
                            Log.e("in if", "in if");
                            currentVolume = audio.getStreamVolume(AudioManager.STREAM_MUSIC);
                            Log.e("current volume", currentVolume + "");
                            per = (double) currentVolume / (double) maxVolume;
                            try {
                                if (per + ((double) getDistance(x, y, ev) / (double) 160) < 1) {
                                    Log.e("((double)getDistance", ((double) getDistance(x, y, ev) / (double) 160) + "");
                                    cv.show();
                                    cv.setProgress((int) ((per + ((double) getDistance(x, y, ev) / (double) 160)) * 100));
                                    cv.setProgressText((int) ((per + ((double) getDistance(x, y, ev) / (double) 160)) * 100) + "%");
                                    volper = (per + ((double) getDistance(x, y, ev) / (double) 160));
                                    Log.e("volper", volper + "");
                                    audio.setStreamVolume(AudioManager.STREAM_MUSIC, (int) (volper * 15), 0);
                                    Log.e("pulkit", (int) (volper * maxVolume) + " ");
                                } else {
                                    audio.setStreamVolume(AudioManager.STREAM_MUSIC, (maxVolume), 0);
                                    cv.setProgressText("100" + "%");
                                }
                            } catch (SecurityException e) {
                            }
                        } else {
                            Log.e("in else", "inelse");
                            currentVolume = audio.getStreamVolume(AudioManager.STREAM_MUSIC);
                            Log.e("current volume", currentVolume + "");
                            per = (double) currentVolume / maxVolume;
                            if (per - (float) (getDistance(x, y, ev) / 160) > 0) {
                                cv.show();
                                cv.setProgress((int) ((per - ((double) getDistance(x, y, ev) / (double) 160)) * 100));
                                cv.setProgressText((int) ((per - ((double) getDistance(x, y, ev) / (double) 160)) * 100) + "%");
                                volper = (per - ((double) getDistance(x, y, ev) / (double) 160));
                                audio.setStreamVolume(AudioManager.STREAM_MUSIC, (int) (volper * maxVolume), 0);
                            } else {
                                audio.setStreamVolume(AudioManager.STREAM_MUSIC, 0, 0);
                                cv.setProgressText("0" + "%");
                            }
                        }
                    }*/
                } catch (IllegalArgumentException e) {

                }
                break;
            }

            case MotionEvent.ACTION_UP: {
                Handler handler = new Handler();
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        if (cv.isVisible())
                            cv.hide();
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

    public void Volume(float X, float Y, float x, float y, float d, String type) {
        cv.setTitle(" Volume  ");
        if (type == "Y" && x == X) {
            d = d / 200;
            if (y < Y) {
                commonVolume(d);
            } else {
                commonVolume(-d);
            }
        } else if (type == "X" && y == Y) {
            Log.e("pul", "in if");
            d = d / 100;
            if (x > X) {
                commonVolume(d);
            } else {
                commonVolume(-d);
            }
        }

    }

    public void commonVolume(float d) {
        currentVolume = audio.getStreamVolume(AudioManager.STREAM_MUSIC);
        per = (double) currentVolume / (double) maxVolume;
        Log.e("per", per + "");
        volumeSum += d;
        if (per + volumeSum <= 1 && per + volumeSum >= 0) {
            cv.show();
            cv.setProgress((int) ((per + volumeSum) * 100));
            cv.setProgressText((int) ((per + volumeSum) * 100) + "%");
            volper = (per + (double) volumeSum);
            audio.setStreamVolume(AudioManager.STREAM_MUSIC, (int) (volper * 15), 0);
        }
    }

    public void Brightness(float X, float Y, float x, float y, float d, String type) {
        cv.setTitle("Brightness");
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
        } else if (type == "circular") {

        }
    }

    public void commonBrightness(float d) {
        WindowManager.LayoutParams layout = getWindow().getAttributes();
        if (getWindow().getAttributes().screenBrightness + d <= 1 && getWindow().getAttributes().screenBrightness + d >= 0) {
            cv.show();
            cv.setProgress((int) ((getWindow().getAttributes().screenBrightness + d) * 100));
            layout.screenBrightness = getWindow().getAttributes().screenBrightness + d;
            getWindow().setAttributes(layout);
            cv.setProgressText(Integer.valueOf((int) ((getWindow().getAttributes().screenBrightness + d) * 100)).toString() + "%");
        }
    }

    public void Seek(float X, float Y, float x, float y, float d, String type) {

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
//            Log.e("current",mp.getCurrentPosition()+"");
            sv.show();
            if(mediaPlayer!=null) {
                Log.e("after", mediaPlayer.getCurrentPosition() + (int) (d * 60000) + "");
                Log.e("seek distance", (int) (seekdistance) + "");
                if (mediaPlayer.getCurrentPosition() + (int) (d * 60000) > 0 && mediaPlayer.getCurrentPosition() + (int) (d * 60000) < mediaPlayer.getDuration()) {
                    mediaPlayer.seekTo(mediaPlayer.getCurrentPosition() + (int) (d * 60000));
//                Log.e("afterincrement",mp.getCurrentPosition()+"");
                    sv.setText((int) (seekdistance / 60000) + ":" + String.valueOf(Math.abs((int) ((seekdistance) % 60000))).substring(0, 2));
                }
            }else
                if(videoView!=null)
                {
                    Log.e("after",videoView.getCurrentPosition() + (int) (d * 60000) + "");
                    Log.e("seek distance", (int) (seekdistance) + "");
                    if (videoView.getCurrentPosition() + (int) (d * 60000) > 0 && videoView.getCurrentPosition() + (int) (d * 60000) < videoView.getDuration()) {
                        videoView.seekTo(videoView.getCurrentPosition() + (int) (d * 60000));
//                Log.e("afterincrement",mp.getCurrentPosition()+"");
                        sv.setText((int) (seekdistance / 60000) + ":" + String.valueOf(Math.abs((int) ((seekdistance) % 60000))).substring(0, 2));
                    }
                }
    }

    public void Seek1(String type, VideoView v) {

        if (type == "vertical")
            onVertical = "Seek";
        else if (type == "horizontal")
            onHorizontal = "Seek";
        videoView=v;
    }
    public void Seek1(String type, MediaPlayer v) {

        if (type == "vertical")
            onVertical = "Seek";
        else if (type == "hoizontal")
            onHorizontal = "Seek";
        mediaPlayer=v;
    }

    public void Brightness1(String type) {

        if (type == "vertical")
            onVertical = "Brightness";
        else if (type == "horizontal")
            onHorizontal = "Brightness";
        else if (type == "circular")
            onCircular = "Brightness";
    }

    public void Volume1(String type) {

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
