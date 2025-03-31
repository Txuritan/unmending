plugins {
    id("fabric-loom").version("1.10-SNAPSHOT").apply(false)
    id("com.modrinth.minotaur").version("2.+").apply(false)

    id("co.uzzu.dotenv.gradle").version("4.0.0")
}

subprojects {
    ext {
        set("modrinthDebug", env.MODRINTH_DEBUG.orElse("false").toBoolean())
        set("modrinthId", env.MODRINTH_ID.orNull())
        set("modrinthToken", env.MODRINTH_TOKEN.orNull())
        set("modrinthType", env.MODRINTH_TYPE.orElse("release"))

        set("modVersion", "1.0.5")
        set("mavenGroup", "dev.txuritan")
        set("modArchivesBaseName", "unmending")
    }
}
