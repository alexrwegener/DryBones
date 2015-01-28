package com.github.ihal20.drybones.flowmortar;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import flow.Flow.Direction;
import flow.Flow.TraversalCallback;
import flow.Path;
import flow.PathContainer;
import flow.PathContext;
import flow.PathContextFactory;

public class SimplePathContainer extends PathContainer {
  private final PathContextFactory contextFactory;

  public SimplePathContainer(int tagKey, PathContextFactory contextFactory) {
    super(tagKey);
    this.contextFactory = contextFactory;
  }

  protected void performTraversal(final ViewGroup containerView, TraversalState traversalState, final Direction direction, final TraversalCallback callback) {
    final PathContext oldPath;
    if (containerView.getChildCount() > 0) {
      oldPath = PathContext.get(containerView.getChildAt(0).getContext());
    } else {
      oldPath = PathContext.root(containerView.getContext());
    }

    Path to = traversalState.toPath();
    final PathContext context = PathContext.create(oldPath, to, this.contextFactory);
    int layout = this.getLayout(to);
    ViewGroup newView = (ViewGroup) LayoutInflater.from(context).cloneInContext(context).inflate(layout, containerView, false);
    final View fromView;
    if (traversalState.fromPath() != null) {
      fromView = containerView.getChildAt(0);
      traversalState.saveViewState(fromView);
    } else {
      fromView = null;
    }
    traversalState.restoreViewState(newView);
    if (fromView != null && direction != Direction.REPLACE) {
      containerView.addView(newView);
      Utils.waitForMeasure(newView, new Utils.OnMeasuredCallback() {
        public void onMeasured(View view, int width, int height) {
          SimplePathContainer.this.runAnimation(containerView, fromView, view, direction, new TraversalCallback() {
            public void onTraversalCompleted() {
              containerView.removeView(fromView);
              oldPath.destroyNotIn(context, SimplePathContainer.this.contextFactory);
              callback.onTraversalCompleted();
            }
          });
        }
      });
    } else {
      containerView.removeAllViews();
      containerView.addView(newView);
      oldPath.destroyNotIn(context, this.contextFactory);
      callback.onTraversalCompleted();
    }
  }

  private void runAnimation(final ViewGroup container, final View from, View to, Direction direction, final TraversalCallback callback) {
    Animator animator = this.createSegue(from, to, direction);
    animator.addListener(new AnimatorListenerAdapter() {
      public void onAnimationEnd(Animator animation) {
        container.removeView(from);
        callback.onTraversalCompleted();
      }
    });
    animator.start();
  }

  private Animator createSegue(View from, View to, Direction direction) {
    boolean backward = direction == Direction.BACKWARD;
    int fromTranslation = backward ? from.getWidth() : -from.getWidth();
    int toTranslation = backward ? -to.getWidth() : to.getWidth();
    AnimatorSet set = new AnimatorSet();
    set.play(ObjectAnimator.ofFloat(from, View.TRANSLATION_X, new float[] { (float) fromTranslation }));
    set.play(ObjectAnimator.ofFloat(to, View.TRANSLATION_X, new float[] { (float) toTranslation, 0.0F }));
    return set;
  }
}
