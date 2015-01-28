package com.github.ihal20.drybones.flowmortar;

import android.os.Bundle;
import flow.Backstack;
import flow.Flow;
import flow.Parceler;

public abstract class FlowBundler {
  private static final String FLOW_KEY = "flow_key";
  private final Parceler parceler;
  private Flow flow;

  protected FlowBundler(Parceler parceler) {
    this.parceler = parceler;
  }

  public Flow onCreate(Bundle savedInstanceState) {
    if (this.flow != null) {
      return this.flow;
    } else {
      Backstack restoredBackstack = null;
      if (savedInstanceState != null && savedInstanceState.containsKey("flow_key")) {
        restoredBackstack = Backstack.from(savedInstanceState.getParcelable("flow_key"), this.parceler);
      }

      this.flow = new Flow(this.getColdStartBackstack(restoredBackstack));
      return this.flow;
    }
  }

  public void onSaveInstanceState(Bundle outState) {
    Backstack backstack = this.getBackstackToSave(this.flow.getBackstack());
    if (backstack != null) {
      outState.putParcelable("flow_key", backstack.getParcelable(this.parceler));
    }
  }

  protected Backstack getBackstackToSave(Backstack backstack) {
    return backstack;
  }

  protected abstract Backstack getColdStartBackstack(Backstack var1);
}

