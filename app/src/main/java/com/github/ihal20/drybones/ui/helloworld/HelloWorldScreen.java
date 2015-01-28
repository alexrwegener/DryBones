package com.github.ihal20.drybones.ui.helloworld;

import android.os.Bundle;
import com.github.ihal20.drybones.R;
import com.github.ihal20.drybones.activity.MainModule;
import com.github.ihal20.drybones.android.AppBarOwner;
import com.github.ihal20.drybones.flowmortar.WithModule;
import flow.Layout;
import flow.Path;
import javax.inject.Inject;
import javax.inject.Singleton;
import mortar.ViewPresenter;

@Layout(R.layout.hello_world_view) @WithModule(HelloWorldScreen.Module.class)
public class HelloWorldScreen extends Path {

  @dagger.Module(injects = HelloWorldView.class, addsTo = MainModule.class)
  public static class Module {
  }

  @Singleton
  static class Presenter extends ViewPresenter<HelloWorldView> {
    private final AppBarOwner actionBarOwner;

    @Inject Presenter(AppBarOwner actionBarOwner) {
      this.actionBarOwner = actionBarOwner;
    }

    @Override
    protected void onLoad(Bundle savedInstanceState) {
      super.onLoad(savedInstanceState);
      HelloWorldView view = getView();
      if (view == null) {
        return;
      }
      actionBarOwner.setConfig(new AppBarOwner.Config(true, false, "Hello World!", null, true));
      view.setText("Boring onLoad update");
    }
  }
}
