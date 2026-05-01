import ru.astrainteractive.gradleplugin.property.util.requireProjectInfo
import ru.astrainteractive.gradleplugin.property.util.requirePublishInfo

plugins {
    `kotlin-dsl`
    id("java-gradle-plugin")
    id("com.vanniktech.maven.publish")
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
            displayName = "KLibs detekt plugin"
            description = "Default setup for detekt plugin"
            tags.set(listOf("klibs"))
        }
    }
}
