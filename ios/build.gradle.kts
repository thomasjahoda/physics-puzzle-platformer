apply(plugin = "kotlin")
apply(plugin = "java")
apply(plugin = "java-library")

tasks.withType<JavaCompile> {
    sourceCompatibility = "7"
    options.encoding = "UTF-8"
}

val mainClassName = "com.jafleck.game.IOSLauncher"
val assetsDir = project(":core").sourceSets["main"].resources.sourceDirectories.singleFile
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
