plugins {
    `java-library`
}

buildscript {
    val kotlinVersion = "1.3.41"
    val gdxVersion = "1.9.9"

    repositories {
        mavenLocal()
        mavenCentral()
        maven("https://plugins.gradle.org/m2/")
        maven("https://oss.sonatype.org/content/repositories/snapshots/")
        jcenter()
        google()
    }
    dependencies {
        classpath("com.android.tools.build:gradle:3.4.1")
        classpath("com.mobidevelop.robovm:robovm-gradle-plugin:2.3.6")
        classpath("org.jetbrains.kotlin:kotlin-gradle-plugin:$kotlinVersion")
        classpath("com.badlogicgames.gdx:gdx-tools:$gdxVersion")
    }
}

object DependencyVersions {
    const val appName = "physics-puzzle-platformer"
    const val kotlinVersion = "1.3.41"
    const val gdxVersion = "1.9.9"
    const val roboVMVersion = "2.3.6"
    const val box2DLightsVersion = "1.4"
    const val ashleyVersion = "1.7.3"
    const val aiVersion = "1.8.0"
    const val ktxVersion = "$gdxVersion-b2"
    const val koinVersion = "2.0.1"
    const val junitVersion = "5.4.2"
    const val assertJVersion = "3.12.2"
    const val thirdPartyKotlinXmlBuilder = "1.5.1"
}

//import com.badlogic.gdx.tools.texturepacker.TexturePacker

allprojects {
    apply(plugin = "eclipse")
    apply(plugin = "idea")

    version = "1.0"

    repositories {
        mavenLocal()
        mavenCentral()
        jcenter()
        google()
        maven("https://oss.sonatype.org/content/repositories/snapshots/")
        maven("https://oss.sonatype.org/content/repositories/releases/")
    }
}


project(":desktop") {
    apply(plugin = "kotlin")
    apply(plugin = "java-library")

    dependencies {
        api(project(":core"))
        api("com.badlogicgames.gdx:gdx-backend-lwjgl:${DependencyVersions.gdxVersion}")
        api("com.badlogicgames.gdx:gdx-platform:${DependencyVersions.gdxVersion}:natives-desktop")
        api("com.badlogicgames.gdx:gdx-box2d-platform:${DependencyVersions.gdxVersion}:natives-desktop")
        api("org.jetbrains.kotlin:kotlin-stdlib:${DependencyVersions.kotlinVersion}")
        api("org.redundent:kotlin-xml-builder:${DependencyVersions.thirdPartyKotlinXmlBuilder}")

        testImplementation("org.junit.jupiter:junit-jupiter-api:${DependencyVersions.junitVersion}")
        testRuntimeOnly("org.junit.jupiter:junit-jupiter-engine:${DependencyVersions.junitVersion}")

        testImplementation("org.assertj:assertj-core:${DependencyVersions.assertJVersion}")
    }
}

project(":android") {
    apply(plugin = "android")
    apply(plugin = "kotlin-android")

    val natives: Configuration by configurations.creating

    dependencies {
        api(project(":core"))
        api("com.badlogicgames.gdx:gdx-backend-android:${DependencyVersions.gdxVersion}")
        natives ("com.badlogicgames.gdx:gdx-platform:${DependencyVersions.gdxVersion}:natives-armeabi")
        natives("com.badlogicgames.gdx:gdx-platform:${DependencyVersions.gdxVersion}:natives-armeabi-v7a")
        natives("com.badlogicgames.gdx:gdx-platform:${DependencyVersions.gdxVersion}:natives-arm64-v8a")
        natives("com.badlogicgames.gdx:gdx-platform:${DependencyVersions.gdxVersion}:natives-x86")
        natives("com.badlogicgames.gdx:gdx-platform:${DependencyVersions.gdxVersion}:natives-x86_64")
        api("com.badlogicgames.gdx:gdx-box2d:${DependencyVersions.gdxVersion}")
        natives("com.badlogicgames.gdx:gdx-box2d-platform:${DependencyVersions.gdxVersion}:natives-armeabi")
        natives("com.badlogicgames.gdx:gdx-box2d-platform:${DependencyVersions.gdxVersion}:natives-armeabi-v7a")
        natives("com.badlogicgames.gdx:gdx-box2d-platform:${DependencyVersions.gdxVersion}:natives-arm64-v8a")
        natives("com.badlogicgames.gdx:gdx-box2d-platform:${DependencyVersions.gdxVersion}:natives-x86")
        natives("com.badlogicgames.gdx:gdx-box2d-platform:${DependencyVersions.gdxVersion}:natives-x86_64")
        api("com.badlogicgames.ashley:ashley:${DependencyVersions.ashleyVersion}")
        api("org.jetbrains.kotlin:kotlin-stdlib:${DependencyVersions.kotlinVersion}")
    }
}

project(":ios") {
    apply(plugin = "kotlin")
    apply(plugin = "robovm")

    dependencies {
        api(project(":core"))
        api("com.mobidevelop.robovm:robovm-rt:${DependencyVersions.roboVMVersion}")
        api("com.mobidevelop.robovm:robovm-cocoatouch:${DependencyVersions.roboVMVersion}")
        api("com.badlogicgames.gdx:gdx-backend-robovm:${DependencyVersions.gdxVersion}")
        api("com.badlogicgames.gdx:gdx-platform:${DependencyVersions.gdxVersion}:natives-ios")
        api("com.badlogicgames.gdx:gdx-box2d-platform:${DependencyVersions.gdxVersion}:natives-ios")
        api("org.jetbrains.kotlin:kotlin-stdlib:${DependencyVersions.kotlinVersion}")
    }
}

project(":core") {
    apply(plugin = "kotlin")
    apply(plugin = "java-library")

    ext {
        set("texturePackerDirectory", "$projectDir/textures")
        set("texturePackerOutputDirectory", "$rootDir/android/assets/atlas")
        set("texturePackerAtlasName", "textures.atlas")
    }

    dependencies {
        api("com.badlogicgames.gdx:gdx:${DependencyVersions.gdxVersion}")
        api("com.badlogicgames.gdx:gdx-box2d:${DependencyVersions.gdxVersion}")
        api("com.badlogicgames.ashley:ashley:${DependencyVersions.ashleyVersion}")
        api("org.jetbrains.kotlin:kotlin-stdlib:${DependencyVersions.kotlinVersion}")
        api("org.jetbrains.kotlin:kotlin-reflect:${DependencyVersions.kotlinVersion}")

        api("io.github.libktx:ktx-actors:${DependencyVersions.ktxVersion}")
        api("io.github.libktx:ktx-app:${DependencyVersions.ktxVersion}")
        api("io.github.libktx:ktx-assets:${DependencyVersions.ktxVersion}")
        api("io.github.libktx:ktx-ashley:${DependencyVersions.ktxVersion}")
        api("io.github.libktx:ktx-collections:${DependencyVersions.ktxVersion}")
        api("io.github.libktx:ktx-graphics:${DependencyVersions.ktxVersion}")
        api("io.github.libktx:ktx-log:${DependencyVersions.ktxVersion}")
        api("io.github.libktx:ktx-math:${DependencyVersions.ktxVersion}")
        api("io.github.libktx:ktx-scene2d:${DependencyVersions.ktxVersion}")
        api("io.github.libktx:ktx-box2d:${DependencyVersions.ktxVersion}")

        implementation("org.koin:koin-core:${DependencyVersions.koinVersion}")
        testImplementation("org.koin:koin-test:${DependencyVersions.koinVersion}")

        testImplementation("org.junit.jupiter:junit-jupiter-api:${DependencyVersions.junitVersion}")
        testRuntimeOnly("org.junit.jupiter:junit-jupiter-engine:${DependencyVersions.junitVersion}")

        testImplementation("org.assertj:assertj-core:${DependencyVersions.assertJVersion}")
    }

    tasks.test {
        useJUnitPlatform()
    }

//    tasks.texturePacker {
//        if (project.ext.has("texturePackerDirectory")) {
//            inputs.dir(texturePackerDirectory)
//            outputs.dir(texturePackerOutputDirectory)
//            doLast {
//                logger.info("Calling TexturePacker:")
//                TexturePacker.process(texturePackerDirectory, texturePackerOutputDirectory, texturePackerAtlasName)
//            }
//        }
//    }

//    tasks.classes.dependsOn(tasks.texturePacker)
}
//
//tasks.eclipse.doLast {
//    delete ".project"
//}
