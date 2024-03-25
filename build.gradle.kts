import org.jetbrains.dokka.DokkaConfiguration.Visibility
import java.lang.Thread.sleep
import java.net.URI

val GPG_PRIVATE_KEY = System.getenv("GPG_PRIVATE_KEY")
val GPG_PRIVATE_PASSWORD = System.getenv("GPG_PRIVATE_PASSWORD")
val SONATYPE_USERNAME = System.getenv("SONATYPE_USERNAME")
val SONATYPE_PASSWORD = System.getenv("SONATYPE_PASSWORD")

plugins {
    signing
    `java-library`
    `maven-publish`
    `jvm-test-suite`
    kotlin("jvm") version "1.9.22"
    kotlin("plugin.serialization") version "1.9.22"
    id("org.jetbrains.dokka") version "1.9.10"
    id("com.adarshr.test-logger") version "4.0.0"
    id("org.jetbrains.kotlinx.kover") version "0.7.6"
    id("io.github.gradle-nexus.publish-plugin") version "1.3.0"
}

group = "com.urosjarc"
version = "0.0.1"
val github = "https://github.com/urosjarc/db-messiah-extra"

kotlin {
    explicitApi()
    jvmToolchain(19)
}
java {
    withSourcesJar()
}
repositories {
    mavenCentral()
}

testlogger {
    this.setTheme("mocha")
}

koverReport {
    filters {
        excludes { classes("*.Test_*") }
        includes { classes("com.urosjarc.dbmessiah.extra.*") }
    }
}

tasks.register<GradleBuild>("github") {
    this.group = "verification"
    this.doFirst {
        println("Waiting for services to warm up...")
        sleep(60 * 1000)
        println("Start with testing...")
    }
    this.tasks = listOf("test")
}

signing {
    useInMemoryPgpKeys(GPG_PRIVATE_KEY, GPG_PRIVATE_PASSWORD)
    sign(publishing.publications)
}

val dokkaHtml by tasks.getting(org.jetbrains.dokka.gradle.DokkaTask::class)

val javadocJar: TaskProvider<Jar> by tasks.registering(Jar::class) {
    dependsOn(dokkaHtml)
    archiveClassifier.set("javadoc")
    from(dokkaHtml.outputDirectory)
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
    val dbMessiah = "0.0.1"

    compileOnly("com.urosjarc:db-messiah:$dbMessiah")
    compileOnly("org.jetbrains.kotlinx:kotlinx-datetime:0.4.0")
    compileOnly("org.jetbrains.kotlinx:kotlinx-serialization-json:1.5.0")

    testRuntimeOnly("com.ibm.db2:jcc:11.5.9.0")
    testRuntimeOnly("com.h2database:h2:2.2.224")
    testRuntimeOnly("org.apache.derby:derby:10.17.1.0")
    testRuntimeOnly("org.mariadb.jdbc:mariadb-java-client:3.3.2")
    testRuntimeOnly("org.xerial:sqlite-jdbc:3.44.1.0")
    testRuntimeOnly("com.mysql:mysql-connector-j:8.2.0")
    testRuntimeOnly("com.microsoft.sqlserver:mssql-jdbc:12.4.2.jre11")
    testRuntimeOnly("org.postgresql:postgresql:42.7.1")
    testRuntimeOnly("com.oracle.database.jdbc:ojdbc11:23.3.0.23.09")

    testImplementation("org.jetbrains.kotlinx:kotlinx-datetime:0.4.0")
    testImplementation("org.jetbrains.kotlinx:kotlinx-serialization-json:1.5.0")
    testImplementation("org.apache.logging.log4j:log4j-slf4j2-impl:2.20.0")
    testImplementation("org.jetbrains.kotlin:kotlin-test")
    testImplementation("com.urosjarc:db-messiah:$dbMessiah")
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
            artifact(javadocJar)
            pom {
                name = "Db Messiah Extra Utils"
                description = "Extra Utils for Db Messiah, kotlin lib. for enterprise database development"
                url = github
                issueManagement {
                    system = "Github"
                    url = "$github/issues"
                }
                licenses {
                    license {
                        name = "The Apache License, Version 2.0"
                        url = "http://www.apache.org/licenses/LICENSE-2.0.txt"
                    }
                }
                developers {
                    developer {
                        id = "urosjarc"
                        name = "Uroš Jarc"
                        email = "jar.fmf@gmail.com"
                    }
                }
                scm {
                    connection.set("scm:git:$github")
                    developerConnection.set("scm:git:$github")
                    url.set(github)
                }
            }
        }
    }
    repositories {
        maven {
            name = "snapshot"
            setUrl { "https://oss.sonatype.org/content/repositories/snapshots/" }
            credentials {
                username = SONATYPE_USERNAME
                password = SONATYPE_PASSWORD
            }
        }
    }

}
