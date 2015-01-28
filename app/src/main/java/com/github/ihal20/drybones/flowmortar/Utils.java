package com.github.ihal20.drybones.flowmortar;

import android.view.View;
import android.view.ViewTreeObserver;

public final class Utils {
  public static void waitForMeasure(final View view, final Utils.OnMeasuredCallback callback) {
    int width = view.getWidth();
    int height = view.getHeight();
    if (width > 0 && height > 0) {
      callback.onMeasured(view, width, height);
    } else {
      view.getViewTreeObserver().addOnPreDrawListener(new ViewTreeObserver.OnPreDrawListener() {
        public boolean onPreDraw() {
          ViewTreeObserver observer = view.getViewTreeObserver();
          if (observer.isAlive()) {
            observer.removeOnPreDrawListener(this);
          }
          callback.onMeasured(view, view.getWidth(), view.getHeight());
          return true;
        }
      });
    }
  }

  private Utils() {
  }

  public interface OnMeasuredCallback {
    void onMeasured(View var1, int var2, int var3);
  }
}
