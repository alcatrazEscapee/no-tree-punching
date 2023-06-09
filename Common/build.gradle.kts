plugins {
    java
    id("org.spongepowered.gradle.vanilla") version "0.2.1-SNAPSHOT"
}

// From gradle.properties
val modId: String by extra;
val minecraftVersion: String by extra
val epsilonVersion: String by extra

repositories {
    fun exclusiveMaven(url: String, filter: Action<InclusiveRepositoryContentDescriptor>) =
        exclusiveContent {
            forRepository { maven(url) }
            filter(filter)
        }

    exclusiveMaven("https://alcatrazescapee.jfrog.io/artifactory/mods") { includeGroup("com.alcatrazescapee") }
}

minecraft {
    version(minecraftVersion)
    accessWideners(project.file("src/main/resources/${modId}.common.accesswidener"))
}

dependencies {
    compileOnly(group = "org.spongepowered", name = "mixin", version = "0.8.5")
    compileOnly(group = "com.alcatrazescapee", name = "epsilon", version = epsilonVersion)
}
