package com.jafleck.testutil.integration

import com.jafleck.game.util.files.AssetsFileHandleResolver
import com.jafleck.testutil.CustomClasspathAssetsFileHandleResolver
import org.koin.dsl.module


val testFilesModule = module {
    single<AssetsFileHandleResolver> { CustomClasspathAssetsFileHandleResolver() }
}
