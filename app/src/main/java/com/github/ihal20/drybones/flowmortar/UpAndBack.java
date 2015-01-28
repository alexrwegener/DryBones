package com.github.ihal20.drybones.flowmortar;

import android.view.View;
import flow.Flow;

public class UpAndBack {
  public static boolean onUpPressed(View childView) {
    return childView instanceof HandlesUp && ((HandlesUp) childView).onUpPressed() ? true : Flow.get(childView).goUp() || onBackPressed(childView);
  }

  public static boolean onBackPressed(View childView) {
    return childView instanceof HandlesBack && ((HandlesBack) childView).onBackPressed() ? true : Flow.get(childView).goBack();
  }

  private UpAndBack() {
  }
}
