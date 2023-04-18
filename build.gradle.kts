plugins {
    kotlin("jvm") version "1.8.0"
    application
    `maven-publish`
}

val sdkVersion = "1.0.18"
allprojects {
    group = "com.weltcorp.dta.waud.lib"
    version = sdkVersion

    repositories {
        mavenCentral()
    }
}

val rxjavaVersion = "3.0.11"
val grpcVersion = "3.19.4"

dependencies {
    implementation(project(mapOf("path" to ":core")))
    implementation(project(mapOf("path" to ":diary")))
    implementation(project(mapOf("path" to ":learning")))
    implementation("io.reactivex.rxjava3:rxjava:$rxjavaVersion")
    implementation("com.google.protobuf:protobuf-kotlin:$grpcVersion")
    implementation("com.google.protobuf:protobuf-java:$grpcVersion")
}

subprojects {
    apply(plugin = "kotlin")
    apply(plugin = "application")
    apply(plugin = "maven-publish")

    tasks.test {
        useJUnitPlatform()
    }

    dependencies {
        testImplementation(kotlin("test"))
    }
}

kotlin {
    jvmToolchain(8)
}

application {
    mainClass.set("MainKt")
}
