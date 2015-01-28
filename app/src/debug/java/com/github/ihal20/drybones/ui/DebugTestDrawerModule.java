package com.github.ihal20.drybones.ui;

import com.github.ihal20.drybones.AppContainer;
import com.github.ihal20.drybones.ui.debug.DebugAppContainer;
import com.github.ihal20.drybones.ui.debug.SocketActivityHierarchyServer;
import dagger.Module;
import dagger.Provides;
import javax.inject.Singleton;

@Module(
    injects = DebugAppContainer.class,
    complete = false,
    library = true,
    overrides = true)
public class DebugTestDrawerModule {
  @Provides @Singleton AppContainer provideAppContainer(DebugAppContainer debugAppContainer) {
    return debugAppContainer;
  }

  @Provides @Singleton ActivityHierarchyServer provideActivityHierarchyServer() {
    return new SocketActivityHierarchyServer();
  }
}
