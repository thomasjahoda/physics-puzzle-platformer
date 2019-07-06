import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

apply(plugin = "kotlin")
apply(plugin = "java")
apply(plugin = "java-library")

tasks.withType<JavaCompile> {
    sourceCompatibility = "7"
    options.encoding = "UTF-8"
}
tasks.withType<KotlinCompile> {
    kotlinOptions.freeCompilerArgs += "-XXLanguage:+InlineClasses"
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
