plugins {
    id("fabric-loom")
    id("com.modrinth.minotaur")
    id("maven-publish")
}

val modrinthDebug: Boolean by ext
val modrinthId: String by ext
val modrinthToken: String? by ext
val modrinthType: String by ext

// Java Properties
val targetJavaVersion = 21

// Mod Properties
val modVersion: String by ext
val mavenGroup: String by ext
val modArchivesBaseName: String by ext

// Fabric Properties
// check these on https://modmuss50.me/fabric.html
val minecraftVersion = "1.21"
val yarnMappings = "1.21+build.9"
val loaderVersion = "0.16.5"

// Dependencies
val fabricVersion = "0.102.0+1.21"

version = "$modVersion+$minecraftVersion"
group = mavenGroup

base {
    archivesName = modArchivesBaseName
}

repositories {
    // Add repositories to retrieve artifacts from in here.
    // You should only use this when depending on other mods because
    // Loom adds the essential maven repositories to download Minecraft and libraries from automatically.
    // See https://docs.gradle.org/current/userguide/declaring_repositories.html
    // for more information about repositories.
}

dependencies {
    // To change the versions see the gradle.properties file
    minecraft("com.mojang:minecraft:${minecraftVersion}")
    mappings("net.fabricmc:yarn:${yarnMappings}:v2")
    modImplementation("net.fabricmc:fabric-loader:${loaderVersion}")

    // Fabric API. This is technically optional, but you probably want it anyway.
    modImplementation("net.fabricmc.fabric-api:fabric-api:${fabricVersion}")
}

modrinth {
    token.set(modrinthToken)
    projectId.set(modrinthId)
    versionNumber.set("$modVersion+$minecraftVersion")
    versionType.set(modrinthType)
    uploadFile.set(tasks.remapJar)
    gameVersions.addAll(minecraftVersion)
    loaders.add("fabric")
    debugMode.set(modrinthDebug)
    dependencies {
        required.project("fabric-api")
    }
}

tasks {
    processResources {
        inputs.property("version", "$modVersion+$minecraftVersion")
        inputs.property("minecraft_version", minecraftVersion)
        inputs.property("loader_version", loaderVersion)
        filteringCharset = "UTF-8"

        filesMatching("fabric.mod.json") {
            expand(
                mapOf(
                    "version" to "$modVersion+$minecraftVersion",
                    "minecraft_version" to minecraftVersion,
                    "loader_version" to loaderVersion
                )
            )
        }
    }

    jar {
        from("LICENSE") {
            rename { "${it}_${modArchivesBaseName}" }
        }
    }

    withType<JavaCompile>() {
        // ensure that the encoding is set to UTF-8, no matter what the system default is
        // this fixes some edge cases with special characters not displaying correctly
        // see http://yodaconditions.net/blog/fix-for-java-file-encoding-problems-with-gradle.html
        // If Javadoc is generated, this must be specified in that task too.
        options.encoding = "UTF-8"
        if (targetJavaVersion >= 10 || JavaVersion.current().isJava10Compatible) {
            options.release.set(targetJavaVersion)
        }
    }
}

java {
    val javaVersion = JavaVersion.toVersion(targetJavaVersion)
    if (JavaVersion.current() < javaVersion) {
        toolchain.languageVersion = JavaLanguageVersion.of(targetJavaVersion)
    }
    // Loom will automatically attach sourcesJar to a RemapSourcesJar task and to the "build" task
    // if it is present.
    // If you remove this line, sources will not be generated.
    withSourcesJar()

    sourceCompatibility = JavaVersion.toVersion(targetJavaVersion)
    targetCompatibility = JavaVersion.toVersion(targetJavaVersion)
}
