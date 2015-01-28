package com.github.ihal20.drybones.flowmortar;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import flow.Flow;
import flow.Path;
import flow.PathContainer;
import flow.PathContainerView;

public class FramePathContainerView extends FrameLayout implements HandlesBack, HandlesUp, PathContainerView {
  private final PathContainer container;
  private boolean disabled;

  public FramePathContainerView(Context context, AttributeSet attrs) {
    this(context, attrs, new SimplePathContainer(2131034112, Path.contextFactory()));
  }

  protected FramePathContainerView(Context context, AttributeSet attrs, PathContainer container) {
    super(context, attrs);
    this.container = container;
  }

  public boolean dispatchTouchEvent(MotionEvent ev) {
    return !this.disabled && super.dispatchTouchEvent(ev);
  }

  public ViewGroup getContainerView() {
    return this;
  }

  protected void onFinishInflate() {
    super.onFinishInflate();
  }

  public void dispatch(Flow.Traversal traversal, final Flow.TraversalCallback callback) {
    this.disabled = true;
    this.container.executeTraversal(this, traversal, new Flow.TraversalCallback() {
      public void onTraversalCompleted() {
        callback.onTraversalCompleted();
        FramePathContainerView.this.disabled = false;
      }
    });
  }

  public boolean onUpPressed() {
    return UpAndBack.onUpPressed(this.getCurrentChild());
  }

  public boolean onBackPressed() {
    return UpAndBack.onBackPressed(this.getCurrentChild());
  }

  public ViewGroup getCurrentChild() {
    return (ViewGroup)this.getContainerView().getChildAt(0);
  }
}

