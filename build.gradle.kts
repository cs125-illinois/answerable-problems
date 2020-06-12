import java.net.URI

plugins {
    java
    kotlin("jvm") version "1.3.61"
}

group = "com.github.cs125-illinois"
version = "2020.0.0"


repositories {
    mavenCentral()
    maven(url = "https://jitpack.io")
    maven { url = URI("https://jitpack.io") }
}

dependencies {
    implementation(kotlin("stdlib-jdk8"))
    testCompile("junit", "junit", "4.12")

    api("com.github.cs125-illinois:answerable:fed4393c58")
    implementation("org.junit.jupiter:junit-jupiter:5.6.2")
}

configure<JavaPluginConvention> {
    sourceCompatibility = JavaVersion.VERSION_1_8
}
tasks {
    compileKotlin {
        kotlinOptions.jvmTarget = "1.8"
    }
    compileTestKotlin {
        kotlinOptions.jvmTarget = "1.8"
    }
}