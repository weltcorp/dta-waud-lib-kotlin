plugins {
    kotlin("jvm") version "1.8.0"
    application
    `maven-publish`
}

allprojects {
    group = "com.weltcorp.dta.waud.lib"
    version = sdkVersion

    repositories {
        mavenCentral()
    }
}

val sdkVersion = "1.0.16"
val rxjavaVersion = "3.0.11"
val grpcVersion = "3.19.4"

dependencies {
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

//publishing {
//    publications {
//        create<MavenPublication>("maven") {
//            groupId = "com.github.weltcorp"
//            artifactId = "dta-waud-lib-kotlin"
//            version = sdkVersion
//
//            from(components["java"])
//        }
//    }
//}
