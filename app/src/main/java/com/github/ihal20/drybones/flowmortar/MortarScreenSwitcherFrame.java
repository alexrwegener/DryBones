package com.github.ihal20.drybones.flowmortar;

import android.content.Context;
import android.util.AttributeSet;
import com.github.ihal20.drybones.R;
import flow.Path;

public class MortarScreenSwitcherFrame extends FramePathContainerView {
  public MortarScreenSwitcherFrame(Context context, AttributeSet attrs) {
    super(context, attrs, new SimplePathContainer(R.id.screen_switcher_tag, Path.contextFactory(new MortarContextFactory())));
  }
}
