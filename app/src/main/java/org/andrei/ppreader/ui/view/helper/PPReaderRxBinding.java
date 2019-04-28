package org.andrei.ppreader.ui.view.helper;

import android.support.annotation.NonNull;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;


import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.android.MainThreadDisposable;


public class PPReaderRxBinding {
    public static Observable<MotionEvent> dbClicks(@NonNull View view) {
       return new ViewDBClickObservable(view);
    }

    static class ViewDBClickObservable extends Observable<MotionEvent>{

        protected ViewDBClickObservable(View v){
            m_view = v;
        }

        @Override
        protected void subscribeActual(Observer<? super MotionEvent> observer) {
            Listener listener = new Listener(m_view,observer);
            m_view.setOnTouchListener(listener);
        }

        private View m_view;
    };

    static final class Listener extends MainThreadDisposable implements  View.OnTouchListener{

        protected Listener(View view, Observer<? super MotionEvent> observer){
            m_observer = observer;
            m_view = view;

            m_gestureDetector =  new GestureDetector(view.getContext(), new GestureDetector.OnGestureListener() {
                @Override
                public boolean onDown(MotionEvent e) {
                    return false;
                }

                @Override
                public void onShowPress(MotionEvent e) {

                }

                @Override
                public boolean onSingleTapUp(MotionEvent e) {
                    return false;
                }

                @Override
                public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {
                    return false;
                }

                @Override
                public void onLongPress(MotionEvent e) {

                }

                @Override
                public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
                    return false;
                }
            });

            m_gestureDetector.setOnDoubleTapListener(new GestureDetector.OnDoubleTapListener() {
                @Override
                public boolean onSingleTapConfirmed(MotionEvent e) {
                    return false;
                }

                @Override
                public boolean onDoubleTap(MotionEvent e) {
                    m_observer.onNext(e);
                    return false;
                }

                @Override
                public boolean onDoubleTapEvent(MotionEvent e) {
                    return false;
                }
            });
        }

        @Override
        protected void onDispose() {
            m_view.setOnTouchListener(null);
        }

        @Override
        public boolean onTouch(View v, MotionEvent event) {
            m_gestureDetector.onTouchEvent(event);
            return false;
        }

        private Observer<? super MotionEvent> m_observer;
        private View m_view;
        private GestureDetector m_gestureDetector;
    }
}
