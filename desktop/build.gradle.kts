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
    test {
        java {
            setSrcDirs(listOf("test/"))
        }
    }
}


val mainClassName = "com.jafleck.game.desktop.DesktopLauncher"
val assetsDir = File("../android/assets")

task<JavaExec>("run") {
    dependsOn(tasks.classes)
    main = mainClassName
    classpath = sourceSets.main.get().runtimeClasspath
    standardInput = System.`in`
    workingDir = assetsDir
    isIgnoreExitValue = true
}

task<JavaExec>("debug") {
    dependsOn(tasks.classes)
    main = mainClassName
    classpath = sourceSets.main.get().runtimeClasspath
    standardInput = System.`in`
    workingDir = assetsDir
    isIgnoreExitValue = true
    debug = true
}

task<Jar>("dist") {
    dependsOn(tasks.classes)
    from(files(sourceSets.main.get().output.classesDirs))
    from(files(sourceSets.main.get().output.resourcesDir))
    configurations.compileClasspath.get().map { if(it.isDirectory) it else zipTree(it) }.forEach {
        from(it)
    }
    from(assetsDir)

    manifest {
        attributes("Main-Class" to mainClassName)
    }
}
