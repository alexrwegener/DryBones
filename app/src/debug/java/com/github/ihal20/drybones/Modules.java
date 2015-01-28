package com.github.ihal20.drybones;

final class Modules {

  private Modules() {
  }

  static Object[] list(final DryBonesApp app) {
    return new Object[] {
        new DryBonesModule(app), new DebugDryBonesModule()
    };
  }
}
