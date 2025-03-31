pluginManagement {
    repositories {
        maven {
            name = "Fabric"
            url = uri("https://maven.fabricmc.net/")
        }
        mavenCentral()
        gradlePluginPortal()
    }
}

rootProject.name = "unmending"

include(
    "fabric-1.20.1",
    "fabric-1.20.5",
    "fabric-1.21.0",
    "fabric-1.21.1",
    "fabric-1.21.4",
    "fabric-1.21.5",
)
