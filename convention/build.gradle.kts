import ru.astrainteractive.gradleplugin.property.util.requireProjectInfo
import ru.astrainteractive.gradleplugin.property.util.requirePublishInfo

plugins {
    `kotlin-dsl`
    id("java-gradle-plugin")
    id("ru.astrainteractive.gradleplugin.publication")
}

dependencies {
    compileOnly(libs.android.toolsBuild)
    compileOnly(libs.kotlin.gradlePlugin)
    compileOnly(files(libs.javaClass.superclass.protectionDomain.codeSource.location))
    implementation(libs.lint.detekt.gradle)
    implementation(libs.dokka.gradle.plugin)
    implementation(libs.dokka.core)
    implementation(libs.dokka.base)
    implementation(libs.ssh)
}

gradlePlugin {
    website.set(requireProjectInfo.url)
    vcsUrl.set(requirePublishInfo.gitHubUrl)
    description = requireProjectInfo.description
    plugins {
        create("gradleftp") {
            id = "${requireProjectInfo.group}.$name"
            implementationClass = "${requireProjectInfo.group}.plugin.FtpPlugin"
            displayName = "Gradle FTP Plugin"
            description = "Uploads and removes files on a remote host over SFTP from Gradle tasks"
            tags.set(listOf("ftp", "sftp", "upload", "klibs"))
        }
    }
}
