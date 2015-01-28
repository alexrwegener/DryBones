package com.github.ihal20.drybones.android;

import android.app.Application;
import android.content.ContentResolver;
import android.content.SharedPreferences;
import com.github.ihal20.drybones.BuildConfig;
import dagger.Module;
import dagger.Provides;
import javax.inject.Singleton;

import static android.content.Context.MODE_PRIVATE;

//Declaring app wide android dependencies
@Module(library = true, complete = false)
public class AndroidModule {

  @Provides @Singleton AppBarOwner provideActionBarPresenter() {
    return new AppBarOwner();
  }

  @Provides @Singleton SharedPreferences provideSharedPreferences(Application app) {
    return app.getSharedPreferences(BuildConfig.APPLICATION_ID, MODE_PRIVATE);
  }

  @Provides @Singleton ContentResolver provideContentResolver(Application app) {
    return app.getContentResolver();
  }
}