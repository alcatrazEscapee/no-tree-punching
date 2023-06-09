pluginManagement {
    repositories {
        fun exclusiveMaven(url: String, filter: Action<InclusiveRepositoryContentDescriptor>) =
            exclusiveContent {
                forRepository { maven(url) }
                filter(filter)
            }

        exclusiveMaven("https://maven.minecraftforge.net") {
            includeGroupByRegex("net\\.minecraftforge.*")
        }
        exclusiveMaven("https://maven.parchmentmc.org") {
            includeGroupByRegex("org\\.parchmentmc.*")
        }
        exclusiveMaven("https://maven.fabricmc.net/") {
            includeGroup("net.fabricmc")
            includeGroup("fabric-loom")
        }
        exclusiveMaven("https://repo.spongepowered.org/repository/maven-public/") {
            includeGroupByRegex("org\\.spongepowered.*")
        }
        exclusiveMaven("https://alcatrazescapee.jfrog.io/artifactory/mods") {
            includeGroupByRegex("com\\.alcatrazescapee.*")
        }

        gradlePluginPortal()
    }
    resolutionStrategy {
        eachPlugin {
            if (requested.id.id == "org.spongepowered.mixin") {
                useModule("org.spongepowered:mixingradle:${requested.version}")
            }
        }
    }
}

rootProject.name = "NoTreePunching-1.20"
include("Common", "Fabric", "Forge")