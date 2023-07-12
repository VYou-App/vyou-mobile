dependencyResolutionManagement {
//    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    repositories {
//        maven {
//            isAllowInsecureProtocol = true
//            url = uri("http://192.168.1.35:8081/repository/maven-releases/")
//        }
//        maven {
//            url = uri("https://vyou-sdks.s3.eu-west-1.amazonaws.com/releases/")
//        }
        google()
        mavenCentral()
    }
}

rootProject.name = "vyou-mobile"
include(":vyou-android-compose-sample")
include(":vyou-kmm-client")
