plugins {
    id("org.gradle.toolchains.foojay-resolver-convention") version "0.5.0"
}
rootProject.name = "db-messiah-extra"

include(":db-messiah")
project(":db-messiah").projectDir = file("../db-jesus")
