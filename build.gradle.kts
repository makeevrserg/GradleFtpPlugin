plugins {
    `kotlin-dsl`
    alias(libs.plugins.kotlin.jvm) apply false
    alias(libs.plugins.klibs.rules.linter) apply true
}

detekt {
    baseline.set(file("convention/detekt-baseline.xml"))
}
