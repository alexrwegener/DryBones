package com.github.ihal20.drybones.android;

import android.os.Bundle;
import mortar.MortarScope;
import mortar.Presenter;
import rx.functions.Action0;

/**
 * Allows shared configuration of the Android ActionBar.
 */
public class AppBarOwner extends Presenter<AppBarOwner.Activity> {
  public interface Activity {
    void setShowHomeEnabled(boolean enabled);

    void setUpButtonEnabled(boolean enabled);

    void setVisibility(boolean visible);

    void setTitle(CharSequence title);

    void setMenu(MenuAction action);

    MortarScope getScope();
  }

  //Default config
  private Config config = new Config(true, true, "", null, true);

  AppBarOwner() {
  }

  @Override protected MortarScope extractScope(Activity activity) {
    return activity.getScope();
  }

  @Override
  public void onLoad(Bundle savedInstanceState) {
    super.onLoad(savedInstanceState);
    if (config != null) {
      update();
    }
  }

  private void update() {
    if (!hasView()) {
      return;
    }
    Activity activity = getView();
    activity.setVisibility(config.isVisible);
    activity.setTitle(config.title);
    activity.setMenu(config.action);
    activity.setShowHomeEnabled(config.showHomeEnabled);
    activity.setUpButtonEnabled(config.upButtonEnabled);
  }

  public Config getConfig() {
    return config;
  }

  public void setConfig(Config config) {
    this.config = config;
    update();
  }

  public static class Config {
    public final boolean showHomeEnabled;
    public final boolean isVisible;
    public final boolean upButtonEnabled;
    public final CharSequence title;
    public final MenuAction action;

    public Config(boolean showHomeEnabled, boolean upButtonEnabled, CharSequence title, MenuAction action, boolean isVisible) {
      this.showHomeEnabled = showHomeEnabled;
      this.upButtonEnabled = upButtonEnabled;
      this.title = title;
      this.action = action;
      this.isVisible = isVisible;
    }

    public Config withMenuAction(MenuAction action) {
      return new Config(showHomeEnabled, upButtonEnabled, title, action, isVisible);
    }

    public Config withTitle(final String title) {
      return new Config(showHomeEnabled, upButtonEnabled, title, action, isVisible);
    }

    public Config withVisibility(final boolean isVisible) {
      return new Config(showHomeEnabled, upButtonEnabled, title, action, isVisible);
    }
  }

  public static class MenuAction {

    public final CharSequence title;
    public final Action0 action;

    public MenuAction(CharSequence title, Action0 action) {
      this.title = title;
      this.action = action;
    }
  }
}

