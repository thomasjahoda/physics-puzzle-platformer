package com.jafleck.testutil.preferences

import org.koin.dsl.module


val testPreferencesModule = module {
    single { InMemoryPreferences() }
}
