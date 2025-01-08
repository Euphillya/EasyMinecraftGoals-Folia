import java.io.ByteArrayOutputStream

plugins {
    id("java-library")
    id("java")
    id("maven-publish")
    id("io.github.goooler.shadow") version "8.1.8"
    id("io.papermc.paperweight.userdev") version "1.7.7" apply false
}

val paperRepo = "https://repo.papermc.io/repository/maven-public/";
val sonatypeRepo = "https://oss.sonatype.org/content/groups/public/";
val engineHubRepo = "https://maven.enginehub.org/repo/";
val jitpack = "https://jitpack.io";
val mojang = "https://libraries.minecraft.net";
val euphyFolia = "https://github.com/Euphillya/FoliaDevBundle/raw/gh-pages/";

dependencies {
    implementation(project(":core"))
    implementation(project(":v1_20_R1"))
    implementation(project(":v1_20_R2"))
    implementation(project(":v1_20_R3"))
    implementation(project(":v1_20_R4"))
    implementation(project(":v1_21_R1"))
    implementation(project(":v1_21_R2"))
    implementation(project(":v1_21_R3"))
}

allprojects {
    group = "com.magmaguy";
    version = "1.0-" + (System.getenv("GITHUB_RUN_NUMBER") ?: getGitCommitHash())

    apply(plugin = "java-library")
    apply(plugin = "io.github.goooler.shadow")
    apply(plugin = "maven-publish")

    repositories {
        mavenLocal()
        mavenCentral()
        maven(paperRepo)
        maven(sonatypeRepo)
        maven(engineHubRepo)
        maven(mojang)
        maven(jitpack)
        maven(euphyFolia)
    }

    dependencies {
        compileOnly("org.projectlombok:lombok:1.18.32")
        compileOnly("org.checkerframework:checker-qual:3.4.0")
        compileOnly("net.kyori:adventure-api:4.18.0")
    }

    tasks {

        compileJava {
            options.encoding = "UTF-8"
        }
        processResources {
            filesMatching("**/paper-plugin.yml") {
                expand(rootProject.project.properties)
            }

            // Always re-run this task
            outputs.upToDateWhen { false }
        }

    }
}

tasks.test {
    useJUnitPlatform()
}


java {
    sourceCompatibility = JavaVersion.VERSION_21
    targetCompatibility = JavaVersion.VERSION_21
}

fun getGitCommitHash(): String {
    val stdout = ByteArrayOutputStream()
    exec {
        commandLine("git", "rev-parse", "--short", "HEAD") // "--short" retourne les premières lettres du commit
        standardOutput = stdout
    }
    return stdout.toString().trim()
}