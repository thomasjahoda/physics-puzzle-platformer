import java.util.*

plugins {
    id("com.android.application")
    id("kotlin-android")
    id("kotlin-android-extensions")
    id("kotlin-kapt")
}

android {
    buildToolsVersion("28.0.3")
    compileSdkVersion(28)

    sourceSets {
        getByName("main") {
            manifest.srcFile("AndroidManifest.xml")
            assets.srcDirs("assets")
            jniLibs.srcDirs("libs")
        }
    }
    packagingOptions {
        exclude("META-INF/robovm/ios/robovm.xml")
    }
    defaultConfig {
        applicationId = "com.jafleck.game"
        minSdkVersion(21)
        targetSdkVersion(28)
        versionCode = 1
        versionName = "1.0"
    }
    buildTypes {
        getByName("release") {
            isMinifyEnabled = false
            proguardFiles(getDefaultProguardFile("proguard-android.txt"), "proguard-rules.pro")
        }
    }
}


// called every time gradle gets executed, takes the native dependencies of
// the natives configuration, and extracts them to the proper libs/ folders
// so they get packed with the APK.
task("copyAndroidNatives") {
    doFirst {
        file("libs/armeabi/").mkdirs()
        file("libs/armeabi-v7a/").mkdirs()
        file("libs/arm64-v8a/").mkdirs()
        file("libs/x86_64/").mkdirs()
        file("libs/x86/").mkdirs()

        configurations.natives.get().files.forEach { jar ->
            var outputDir: File? = null
            if (jar.name.endsWith("natives-arm64-v8a.jar")) outputDir = file("libs/arm64-v8a")
            if (jar.name.endsWith("natives-armeabi-v7a.jar")) outputDir = file("libs/armeabi-v7a")
            if (jar.name.endsWith("natives-armeabi.jar")) outputDir = file("libs/armeabi")
            if (jar.name.endsWith("natives-x86_64.jar")) outputDir = file("libs/x86_64")
            if (jar.name.endsWith("natives-x86.jar")) outputDir = file("libs/x86")
            if (outputDir != null) {
                copy {
                    from(zipTree(jar))
                    into(outputDir)
                    include("*.so")
                }
            }
        }
    }
}

tasks.configureEach {
    if (name.contains("package")) {
        dependsOn("copyAndroidNatives")
    }
}

task<Copy>("copyAssetsFromCore") {
    from(project(":core").sourceSets["main"].resources.sourceDirectories.singleFile) {
        exclude("README.md")
    }
    into(file("assets"))
}
tasks.build {
    dependsOn("copyAssetsFromCore")
}
tasks.named("preBuild") {
    dependsOn("copyAssetsFromCore")
}
tasks.clean {
    delete(fileTree("assets") {
        exclude(".gitignore")
    })
}

task<Exec>("run") {
    dependsOn("build")
    // copy of parts from https://android.googlesource.com/platform/tools/build/+/d69964104aed4cfae5052028b5c5e57580441ae8/gradle/src/main/groovy/com/android/build/gradle/internal/Sdk.groovy#findLocation
    var path: String? = null
    val localProperties = project.file("../local.properties")
    if (localProperties.exists()) {
        val properties = Properties()
        properties.load(localProperties.inputStream())
        val sdkDir = properties.getProperty("sdk.dir")
        if (sdkDir != null) {
            path = sdkDir
        } else {
            path = System.getenv("ANDROID_HOME")
        }
    } else {
        path = System.getenv("ANDROID_HOME")
    }

    val adb = "$path/platform-tools/adb"
    commandLine(adb, "shell", "am", "start", "-n", "com.jafleck.game/com.jafleck.game.AndroidLauncher")
}
