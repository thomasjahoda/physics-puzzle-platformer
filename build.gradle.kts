plugins {
    `java-library`
    kotlin("jvm") version "1.3.41"
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
    const val mockKVersion = "1.9.3"
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
        testImplementation("io.mockk:mockk:${DependencyVersions.mockKVersion}")
    }
}

project(":android") {
    apply(plugin = "android")
    apply(plugin = "kotlin-android")

    val natives: Configuration by configurations.creating

    dependencies {
        api(project(":core"))
        api("com.badlogicgames.gdx:gdx-backend-android:${DependencyVersions.gdxVersion}")
        natives("com.badlogicgames.gdx:gdx-platform:${DependencyVersions.gdxVersion}:natives-armeabi")
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

    extra["texturePackerDirectory"] = "$projectDir/src/main/texture-atlas-parts"
    extra["texturePackerOutputDirectory"] = sourceSets["main"].resources.sourceDirectories.singleFile.path + "/atlas"
    extra["texturePackerAtlasName"] = "textures.atlas"

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

        testApi("com.badlogicgames.gdx:gdx-backend-headless:${DependencyVersions.gdxVersion}")
        testApi("com.badlogicgames.gdx:gdx-platform:${DependencyVersions.gdxVersion}:natives-desktop")
        testApi("com.badlogicgames.gdx:gdx-box2d-platform:${DependencyVersions.gdxVersion}:natives-desktop")

        testImplementation("org.junit.jupiter:junit-jupiter-api:${DependencyVersions.junitVersion}")
        testRuntimeOnly("org.junit.jupiter:junit-jupiter-engine:${DependencyVersions.junitVersion}")

        testImplementation("org.assertj:assertj-core:${DependencyVersions.assertJVersion}")
        testImplementation("io.mockk:mockk:${DependencyVersions.mockKVersion}")
    }

    tasks.test {
        useJUnitPlatform()
    }

    task("texturePacker") {
        inputs.dir(project.extra["texturePackerDirectory"] as String)
        outputs.dir(project.extra["texturePackerOutputDirectory"] as String)
        doLast {
            logger.info("Calling TexturePacker:")
            com.badlogic.gdx.tools.texturepacker.TexturePacker.process(
                project.extra["texturePackerDirectory"] as String,
                project.extra["texturePackerOutputDirectory"] as String,
                project.extra["texturePackerAtlasName"] as String)
        }
    }
    tasks.clean {
        delete(fileTree(project.extra["texturePackerOutputDirectory"] as String) {
            exclude(".gitignore")
        })
    }
    tasks.classes {
        dependsOn("texturePacker")
    }


    fun configureTaskForExportingMaps(task: Task, inputDir: String, outputDir: String) {
        task.inputs.dir(inputDir)
        task.outputs.dir(outputDir)
        task.doLast {
            logger.debug("Exporting 'Tiled' maps from directory $inputDir to $outputDir")
            val tmxFiles = project.file(inputDir).listFiles { dir, name -> name.endsWith(".tmx") }
            @Suppress("RECEIVER_NULLABILITY_MISMATCH_BASED_ON_JAVA_ANNOTATIONS")
            tmxFiles.forEach {
                logger.info("Exporting ${it.name}")
                project.exec {
                    val outputFile = project.file(outputDir).absolutePath + "/" + it.name
                    commandLine = listOf("tiled", "--export-map", "--detach-templates", it.absolutePath, outputFile)
                }
            }
        }
    }

    task("exportMaps") {
        val inputDir = "$projectDir/src/main/tiledmaps"
        val outputDir = "$projectDir/src/main/resources/maps"
        configureTaskForExportingMaps(this, inputDir, outputDir)
    }
    tasks.clean {
        delete(fileTree("$projectDir/src/main/resources/maps") {
            exclude(".gitignore")
        })
    }
    tasks.classes {
        dependsOn("exportMaps")
    }
    
    task("testExportMaps") {
        val inputDir = "$projectDir/src/test/tiledmaps"
        val outputDir = "$projectDir/src/test/resources/maps"
        configureTaskForExportingMaps(this, inputDir, outputDir)
    }
    tasks.clean {
        delete(fileTree("$projectDir/src/test/resources/maps") {
            exclude(".gitignore")
        })
    }
    tasks.testClasses {
        dependsOn("testExportMaps")
    }
}
