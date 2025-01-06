pluginManagement {
    repositories {
        gradlePluginPortal()
        mavenLocal()
        google()
        mavenCentral()
        //maven { url = uri("http://maven.ghostscript.com") }
        maven {
            url = uri("https://maven.aliyun.com/repository/jcenter")
        }
        maven {
            url = uri("https://maven.aliyun.com/repository/google")
        }
        maven {
            url = uri("https://repo1.maven.org/maven2/")
        }
        maven {
            url = uri("https://jitpack.io")
        }
    }
}

dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    repositories {
        google()
        mavenLocal()
        mavenCentral()
        //maven { url = uri("http://maven.ghostscript.com") }
        maven {
            url = uri("https://maven.aliyun.com/repository/jcenter")
        }
        maven {
            url = uri("https://maven.aliyun.com/repository/google")
        }
        maven {
            url = uri("https://repo1.maven.org/maven2/")
        }
        maven {
            url = uri("https://jitpack.io")
        }
    }
}

rootProject.name = "pdfviewer"

include(":app")
include(":vudroid")
