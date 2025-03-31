plugins {
    id("fabric-loom").version("1.10-SNAPSHOT").apply(false)
    id("com.modrinth.minotaur").version("2.+").apply(false)

    id("co.uzzu.dotenv.gradle").version("4.0.0")
}

subprojects {
    ext {
        set("modrinthId", "QYP9PZUS")
        set("modrinthToken", env.MODRINTH_TOKEN.orNull())

        set("modVersion", "1.0.5")
        set("mavenGroup", "dev.txuritan")
        set("modArchivesBaseName", "unmending")
    }
}
