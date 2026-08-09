plugins {
    `kotlin-dsl`
    alias(libs.plugins.kotlin.jvm) apply false
    alias(libs.plugins.klibs.gradle.detekt) apply true
}
