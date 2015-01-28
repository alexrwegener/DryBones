package com.github.ihal20.drybones;

import android.app.Application;
import android.support.annotation.Nullable;
import com.github.ihal20.drybones.flowmortar.FlowBundler;
import com.github.ihal20.drybones.flowmortar.GsonParceler;
import com.github.ihal20.drybones.ui.ActivityHierarchyServer;
import com.github.ihal20.drybones.ui.helloworld.HelloWorldScreen;
import com.google.gson.Gson;
import dagger.ObjectGraph;
import flow.Backstack;
import javax.inject.Inject;
import mortar.Mortar;
import mortar.MortarScope;
import mortar.dagger1support.Dagger1;
import timber.log.Timber;

public class DryBonesApp extends Application {

  @Inject ActivityHierarchyServer activityHierarchyServer;

  private final FlowBundler flowBundler = new FlowBundler(new GsonParceler(new Gson())) {
    @Override protected Backstack getColdStartBackstack(@Nullable Backstack restoredBackstack) {
      return restoredBackstack == null ? Backstack.single(new HelloWorldScreen()) : restoredBackstack; //TODO Place home screen here
    }
  };

  private MortarScope rootScope;

  @Override
  public void onCreate() {
    super.onCreate();
    if (BuildConfig.DEBUG) {
      Timber.plant(new Timber.DebugTree());
    }
    Timber.d("Initializing App");
    buildObjectGraphAndInject();
    registerActivityLifecycleCallbacks(activityHierarchyServer);
  }

  public void buildObjectGraphAndInject() {
    rootScope = Dagger1.createRootScope(ObjectGraph.create(Modules.list(this)));
    Dagger1.inject(this, this);
  }

  public void rebuildObjectGraphAndInject() {
    Mortar.destroyRootScope(rootScope);
    buildObjectGraphAndInject();
  }

  public FlowBundler getFlowBundler() {
    return flowBundler;
  }

  @Override
  public Object getSystemService(String name) {
    if (Mortar.isScopeSystemService(name)) {
      return rootScope;
    }
    return super.getSystemService(name);
  }
}

