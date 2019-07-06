apply(plugin = "kotlin")
apply(plugin = "java")
apply(plugin = "java-library")

tasks.withType<JavaCompile> {
    sourceCompatibility = "7"
    options.encoding = "UTF-8"
}

sourceSets {
    main {
        java {
            setSrcDirs(listOf("src/"))
        }
    }
}

val mainClassName = "com.jafleck.game.IOSLauncher"
val assetsDir = File("../android/assets")
tasks.launchIPhoneSimulator {
    dependsOn(tasks.build)
}
tasks.launchIPadSimulator {
    dependsOn(tasks.build)
}
tasks.launchIOSDevice {
    dependsOn(tasks.build)
}
tasks.createIPA {
    dependsOn(tasks.build)
}

robovm {
    archs = "thumbv7:arm64"
}
