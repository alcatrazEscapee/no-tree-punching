plugins {
    java
    idea
    id("fabric-loom") version "0.12-SNAPSHOT"
    id("io.github.juuxel.loom-quiltflower") version("1.7.2")
    id("com.github.johnrengelman.shadow") version("7.1.2")
}

// From gradle.properties
val modId: String by extra
val modName: String by extra
val modGroup: String by extra

val minecraftVersion: String by extra
val parchmentVersion: String by extra
val parchmentMinecraftVersion: String by extra
val fabricVersion: String by extra
val fabricLoaderVersion: String by extra
val epsilonVersion: String by extra

val shadowLibrary: Configuration by configurations.creating

configurations {
    implementation.get().extendsFrom(shadowLibrary)
}

base {
    archivesName.set("${modId}-fabric-${minecraftVersion}")
}

repositories {
    fun exclusiveMaven(url: String, filter: Action<InclusiveRepositoryContentDescriptor>) =
        exclusiveContent {
            forRepository { maven(url) }
            filter(filter)
        }

    exclusiveMaven("https://alcatrazescapee.jfrog.io/artifactory/mods") { includeGroup("com.alcatrazescapee") }
    exclusiveMaven("https://maven.parchmentmc.org") { includeGroupByRegex("org\\.parchmentmc.*") }
}

dependencies {
    minecraft(group = "com.mojang", name = "minecraft", version = minecraftVersion)

    @Suppress("UnstableApiUsage")
    mappings(loom.layered {
        officialMojangMappings()
        parchment("org.parchmentmc.data:parchment-${parchmentMinecraftVersion}:${parchmentVersion}@zip")
    })

    modImplementation(group = "net.fabricmc", name = "fabric-loader", version = fabricLoaderVersion)
    modImplementation(group = "net.fabricmc.fabric-api", name = "fabric-api", version = fabricVersion)

    shadowLibrary(group = "com.alcatrazescapee", name = "epsilon", version = epsilonVersion)

    implementation(project(":Common"))
    implementation(group = "org.jetbrains", name = "annotations", version = "23.0.0")
}

loom {

    @Suppress("UnstableApiUsage")
    mixin {
        defaultRefmapName.set("${modId}.refmap.json")
    }

    runs {
        named("client") {
            client()
            configName = "Fabric Client"
            ideConfigGenerated(true)
            runDir("run")
        }
        named("server") {
            server()
            configName = "Fabric Server"
            ideConfigGenerated(true)
            runDir("run")
        }
    }
}

tasks.withType<JavaCompile> {
    source(project(":Common").sourceSets.main.get().allSource)
}

tasks {

    jar {
        archiveClassifier.set("slim")
        from("LICENSE") {
            rename { "${it}_${modName}" }
        }
    }

    shadowJar {
        from(sourceSets.main.get().output)
        configurations = listOf(shadowLibrary)
        dependencies {
            exclude(dependency(KotlinClosure1<ResolvedDependency, Boolean>({
                moduleGroup != modGroup
            })))
        }
        relocate("com.alcatrazescapee.epsilon", "${modGroup}.${modId}.epsilon")
    }

    remapJar {
        dependsOn(shadowJar)
        inputFile.set(shadowJar.get().archiveFile)
    }

    processResources {
        from(project(":Common").sourceSets.main.get().resources)
    }
}

idea {
    module {
        excludeDirs.add(file("run"))
    }
}
