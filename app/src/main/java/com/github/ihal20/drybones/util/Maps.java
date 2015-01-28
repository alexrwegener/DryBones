package com.github.ihal20.drybones.util;

import java.util.HashSet;

public final class Maps {
  private Maps() {
    // No instances.
  }

  public static <E> HashSet<E> newHashSet() {
    return new HashSet<E>();
  }
}
