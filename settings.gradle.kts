pluginManagement {
    repositories {
        google {
            content {
                includeGroupByRegex("com\\.android.*")
                includeGroupByRegex("com\\.google.*")
                includeGroupByRegex("androidx.*")
                maven(url = uri("https://jitpack.io"))
            }
        }
        mavenCentral()
        gradlePluginPortal()
    }
}
dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    repositories {
        maven(url = "https://jitpack.io") // Nếu cần JitPack
        google()
        mavenCentral()
    }
}

rootProject.name = "DoAn_Nhom9_Sang6"
include(":app")
 