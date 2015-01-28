package com.github.ihal20.drybones;

import com.github.ihal20.drybones.data.DebugDataModule;
import com.github.ihal20.drybones.ui.DebugTestDrawerModule;
import dagger.Module;

@Module(
    addsTo = DryBonesModule.class,
    includes = {
        DebugTestDrawerModule.class, DebugDataModule.class
    },
    overrides = true)
public class DebugDryBonesModule {
}
