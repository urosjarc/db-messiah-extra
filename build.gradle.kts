import org.jetbrains.dokka.DokkaConfiguration.Visibility
import java.net.URI

plugins {
    `java-library`
    `maven-publish`
    `jvm-test-suite`
    kotlin("jvm") version "1.9.22"
    kotlin("plugin.serialization") version "1.9.22"
    id("org.jetbrains.dokka") version "1.9.10"
    id("com.adarshr.test-logger") version "4.0.0"
    id("org.jetbrains.kotlinx.kover") version "0.7.6"
}

group = "com.urosjarc"
version = "0.0.2-SNAPSHOT"

kotlin {
    explicitApi()
    jvmToolchain(19)
}

repositories {
    mavenCentral()
    maven { url = URI("https://jitpack.io") }
}

testlogger {
    this.setTheme("mocha")
}

koverReport {
    filters {
        excludes { classes("*.Test_*") }
        includes { classes("com.urosjarc.dbmessiah.*") }
    }
}

tasks.register<GradleBuild>("github") {
    this.group = "verification"
    this.doFirst {
        println("Waiting for services to warm up...")
        Thread.sleep(60 * 1000)
        println("Start with testing...")
    }
    this.tasks = listOf("test")
}

tasks.dokkaHtml {
    dokkaSourceSets {
        configureEach {
            documentedVisibilities.set(
                setOf(
                    Visibility.PUBLIC, // Same for both Kotlin and Java
                    Visibility.PRIVATE, // Same for both Kotlin and Java
                    Visibility.PROTECTED, // Same for both Kotlin and Java
                    Visibility.INTERNAL, // Kotlin-specific internal modifier
                    Visibility.PACKAGE, // Java-specific package-private visibility
                )
            )
            includeNonPublic.set(true)
            jdkVersion.set(19)
            reportUndocumented.set(true)
            skipEmptyPackages.set(false)
        }
    }
}

dependencies {
    implementation(kotlin("reflect"))
    implementation(project(":db-messiah"))
    implementation("org.apache.logging.log4j:log4j-api-kotlin:1.4.0")
    implementation("org.jetbrains.kotlinx:kotlinx-datetime:0.4.0")
    implementation("org.jetbrains.kotlinx:kotlinx-serialization-json:1.5.0")

    testRuntimeOnly("com.ibm.db2:jcc:11.5.9.0")
    testRuntimeOnly("com.h2database:h2:2.2.224")
    testRuntimeOnly("org.apache.derby:derby:10.17.1.0")
    testRuntimeOnly("org.mariadb.jdbc:mariadb-java-client:3.3.2")
    testRuntimeOnly("org.xerial:sqlite-jdbc:3.44.1.0")
    testRuntimeOnly("com.mysql:mysql-connector-j:8.2.0")
    testRuntimeOnly("com.microsoft.sqlserver:mssql-jdbc:12.4.2.jre11")
    testRuntimeOnly("org.postgresql:postgresql:42.7.1")
    testRuntimeOnly("com.oracle.database.jdbc:ojdbc11:23.3.0.23.09")

    testImplementation("org.apache.logging.log4j:log4j-slf4j2-impl:2.20.0")
    testImplementation("org.jetbrains.kotlin:kotlin-test")
}

tasks.test {
    useJUnitPlatform()
}

publishing {
    publications {
        create<MavenPublication>("maven") {
            groupId = rootProject.group as String
            artifactId = rootProject.name
            version = rootProject.version as String
            from(components["java"])

            pom {
                name = "Db Messiah Extra Utils"
                description = "Extra Utils for Db Messiah, kotlin lib. for enterprise database development"
                url = "https://github.com/urosjarc/db-messiah-extra"
                licenses {
                    license {
                        name = "The Apache License, Version 2.0"
                        url = "http://www.apache.org/licenses/LICENSE-2.0.txt"
                    }
                }
                developers {
                    developer {
                        id = "urosjarc"
                        name = "Uro� Jarc"
                        email = "jar.fmf@gmail.com"
                    }
                }
            }
        }
    }
}
