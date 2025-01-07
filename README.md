![version](https://img.shields.io/maven-central/v/ru.astrainteractive.gradleplugin/convention?style=flat-square)

## Gradle Ftp Plugin

```toml
[versions]
gradle-ftp = "<version>"
[plugins]
gradle-ftp = { id = "ru.astrainteractive.ftpplugin.gradleftp", version.ref = "gradle-ftp" }
```

```kotlin
plugins {
    alias(libs.plugins.gradle.ftp)
}

configure<SftpConnectionExtension> {
    host = "127.0.0.1"
    port = 2022
    credential = Credential.Password("username", "password")
    hostsStrategy = LoadKnownHostsStrategy.None
    keyVerifierStrategy = KeyVerifierStrategy.Promicious
}

tasks.register<UploadFilesTask> {
    TODO()
}
```

Or let into `local.properties`

```properties
ftp.host=127.0.0.1
ftp.port=2022
ftp.username=username
ftp.password=password
```

```kotlin
tasks.register<ClearFilesTask>("clearFiles") {
    targetFilesPaths.set(listOf("/some/cool/path/myfile.jar"))
}

tasks.register<ClearFolderTask>("clearFolder") {
    targetDirPath.set("/some/cool/path/folder_with_items")
    isRecursive.set(false)
}

tasks.register<ClearFolderTask>("clearFolderRecursive") {
    targetDirPath.set("/some/cool/path/folder_with_items_and_subfoldera")
    isRecursive.set(true)
}

tasks.register<UploadFileTask>("uploadFiles") {
    this.dependsOn(astraShadowJar.requireShadowJarTask)
    this.mustRunAfter(astraShadowJar.requireShadowJarTask)
    inputFiles.set(rootDir.resolve("jars").listFiles().orEmpty().toList())
    outputPath.set("/some/cool/path")
}
```