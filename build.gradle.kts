plugins {
    java
}

group = "dev.alfiecorthine"
val pluginVersion = providers.gradleProperty("pluginVersion").getOrElse("1.1.0-SNAPSHOT")
version = pluginVersion

val paperApiDependency = providers.gradleProperty("paperApiDependency")
    .getOrElse("com.destroystokyo.paper:paper-api:1.16.1-R0.1-SNAPSHOT")
val bytecodeVersion = providers.gradleProperty("bytecodeVersion")
    .map(String::toInt)
    .getOrElse(8)

repositories {
    mavenCentral()
    maven("https://repo.papermc.io/repository/maven-public/")
}

dependencies {
    // Compile against the oldest supported Paper API so the same jar remains
    // binary-compatible with newer Paper releases.
    compileOnly(paperApiDependency)
}

tasks.withType<JavaCompile>().configureEach {
    options.encoding = "UTF-8"
    sourceCompatibility = JavaVersion.toVersion(bytecodeVersion).toString()
    targetCompatibility = JavaVersion.toVersion(bytecodeVersion).toString()

    if (JavaVersion.current().isJava9Compatible) {
        options.release.set(bytecodeVersion)
    }

    if (bytecodeVersion == 8) {
        options.compilerArgs.add("-Xlint:-options")
    }
}

tasks.processResources {
    filteringCharset = "UTF-8"
    filesMatching("plugin.yml") {
        expand("version" to pluginVersion)
    }
}

tasks.jar {
    archiveBaseName.set("NameMask")
}

tasks.withType<AbstractArchiveTask>().configureEach {
    isPreserveFileTimestamps = false
    isReproducibleFileOrder = true
}

