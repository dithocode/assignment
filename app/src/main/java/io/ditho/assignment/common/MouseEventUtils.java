package io.ditho.assignment.common;

import android.os.SystemClock;
import android.view.MotionEvent;
import android.view.View;

public class MouseEventUtils {

    public static void generatePullTouch(View view) {

        long downTime = SystemClock.uptimeMillis();
        long eventTime = SystemClock.uptimeMillis() + 100;
        float x = 10f;
        float y = 50.f;
        int metaState = 0;
        MotionEvent motionEventDown = MotionEvent.obtain(
                downTime,
                eventTime,
                MotionEvent.ACTION_DOWN,
                x,
                y,
                metaState
        );
        view.dispatchTouchEvent(motionEventDown);

        downTime = SystemClock.uptimeMillis();
        eventTime = SystemClock.uptimeMillis() + 600;
        x = 10.0f;
        y = 150.0f;
        metaState = 0;
        MotionEvent motionEventMove = MotionEvent.obtain(
                downTime,
                eventTime,
                MotionEvent.ACTION_MOVE,
                x,
                y,
                metaState
        );
        view.dispatchTouchEvent(motionEventMove);
        downTime = SystemClock.uptimeMillis();
        eventTime = SystemClock.uptimeMillis() + 600;
        x = 10f;
        y = 150.f;
        metaState = 0;
        MotionEvent motionEventUp = MotionEvent.obtain(
                downTime,
                eventTime,
                MotionEvent.ACTION_UP,
                x,
                y,
                metaState
        );
        view.dispatchTouchEvent(motionEventUp);
    }
}
