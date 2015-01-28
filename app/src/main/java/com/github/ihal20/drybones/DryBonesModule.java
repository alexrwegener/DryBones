package com.github.ihal20.drybones;

import android.app.Application;
import com.github.ihal20.drybones.android.AndroidModule;
import com.github.ihal20.drybones.data.DataModule;
import com.github.ihal20.drybones.flowmortar.GsonParceler;
import com.github.ihal20.drybones.ui.TestDrawerModule;
import com.google.gson.FieldNamingPolicy;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import dagger.Module;
import dagger.Provides;
import flow.Parceler;
import javax.inject.Singleton;

@Module(
    library = true,
    includes = {
        AndroidModule.class, TestDrawerModule.class, DataModule.class
    },
    injects = {
        DryBonesApp.class,
    })
public class DryBonesModule {

  private DryBonesApp dryBonesApp;

  public DryBonesModule(final DryBonesApp dryBonesApp) {
    this.dryBonesApp = dryBonesApp;
  }

  @Provides @Singleton DryBonesApp provideDryBonesApp() {
    return dryBonesApp;
  }

  @Provides @Singleton Application provideApplication() {
    return provideDryBonesApp();
  }

  @Provides @Singleton Gson provideGson() {
    return new GsonBuilder().setFieldNamingPolicy(FieldNamingPolicy.LOWER_CASE_WITH_UNDERSCORES).create();
  }

  @Provides @Singleton Parceler provideParcer(Gson gson) {
    return new GsonParceler(gson);
  }
}
