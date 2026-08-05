import dev.detekt.gradle.extensions.DetektExtension

plugins {
    alias(libs.plugins.android.application) apply false
    alias(libs.plugins.android.library) apply false
    alias(libs.plugins.kotlin.android) apply false
    alias(libs.plugins.kotlin.serialization) apply false
    alias(libs.plugins.compose.compiler) apply false
    alias(libs.plugins.ksp) apply false
    alias(libs.plugins.hilt) apply false
    alias(libs.plugins.detekt) apply false
    alias(libs.plugins.google.services) apply false
    alias(libs.plugins.fb.crashlytics) apply false
    alias(libs.plugins.firebase.distribution) apply false
}

subprojects {
    // Per subproject: detekt derives type-resolution tasks from Kotlin compilations, and the root has none.
    apply(plugin = "dev.detekt")

    extensions.configure<DetektExtension> {
        buildUponDefaultConfig = true
        allRules = false
        config.setFrom(rootProject.files("config/detekt/detekt.yml"))
        baseline = rootProject.file("config/detekt/baseline.xml")
        parallel = true
        autoCorrect = true
    }

    dependencies {
        add("detektPlugins", rootProject.libs.detekt.compose)
    }
}
