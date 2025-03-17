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
    implementation("com.vanniktech:gradle-maven-publish-plugin:0.31.0")
    implementation(libs.ssh)
}

gradlePlugin {
    website.set(projectWeb)
    vcsUrl.set(projectWeb)
    description = projectDescription
    plugins {
        create("gradleftp") {
            id = "$projectGroup.$name"
            implementationClass = "$projectGroup.plugin.FtpPlugin"
            displayName = "KLibs detekt plugin"
            description = "Default setup for detekt plugin"
            tags.set(listOf("klibs"))
        }
    }
}
