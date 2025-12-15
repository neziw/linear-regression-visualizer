import com.github.jengelman.gradle.plugins.shadow.tasks.ShadowJar

plugins {
    id("java")
    id("application")
    id("com.gradleup.shadow") version "9.3.0"
}

group = "ovh.neziw.visualizer"
version = "1.0-SNAPSHOT"

tasks.withType<JavaCompile> {
    options.compilerArgs = listOf("-Xlint:deprecation")
    options.encoding = "UTF-8"
}

tasks.withType<ShadowJar> {
    archiveFileName.set("${project.name} ${project.version}.jar")
    exclude(
        "org/intellij/lang/annotations/**",
        "org/jetbrains/annotations/**",
        "org/checkerframework/**",
        "META-INF/DEPENDENCIES",
        "META-INF/LICENSE",
        "META-INF/LICENSE.txt",
        "META-INF/NOTICE",
        "META-INF/NOTICE.txt",
        "javax/**"
    )
    manifest {
        attributes["Main-Class"] = "ovh.neziw.visualizer.VisualizerMain"
    }
    mergeServiceFiles()
}

java {
    withSourcesJar()
    sourceCompatibility = JavaVersion.VERSION_11
    targetCompatibility = JavaVersion.VERSION_11
}

application {
    mainClass.set("ovh.neziw.visualizer.VisualizerMain")
}

repositories {
    gradlePluginPortal()
    mavenLocal()
    mavenCentral()
}

dependencies {
    implementation("org.jfree:jfreechart:1.5.6")
    implementation("com.formdev:flatlaf:3.7")
    implementation("com.google.code.gson:gson:2.13.2")
    implementation("org.apache.poi:poi:5.5.1")
    implementation("org.apache.poi:poi-ooxml:5.5.1")
}

tasks.build {
    dependsOn(tasks.shadowJar)
}
