pluginManagement {
    repositories {
        google()
        mavenCentral()
        gradlePluginPortal()

        // ✅ REQUIRED FOR CHAQUOPY
        maven {
            url = uri("https://chaquo.com/maven")
        }
    }
}

dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    repositories {
        google()
        mavenCentral()

        // ✅ REQUIRED FOR CHAQUOPY
        maven {
            url = uri("https://chaquo.com/maven")
        }
    }
}

rootProject.name = "FastLate"
include(":app")
