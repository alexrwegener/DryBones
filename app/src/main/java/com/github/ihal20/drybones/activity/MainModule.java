package com.github.ihal20.drybones.activity;

import com.github.ihal20.drybones.DryBonesModule;
import dagger.Module;

//Activity wide dependencies
@Module(
    addsTo = DryBonesModule.class,
    injects = {
        MainActivity.class
    },
    library = true)
public final class MainModule {

}
