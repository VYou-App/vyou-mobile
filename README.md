# Android SDK for [VYou](https://www.vyou-app.com/)

This is the Android SDK for [VYou](https://www.vyou-app.com/), a service to provide user identity to your applications. This library includes a client SDK along with some artifacts and a sample to streamline your workflow.

The SDK is written in Kotlin using Kotlin Cross-Platform to provide support for Android and iOS applications.

## Overview

- [KMM-Client](#client): Client to manage operations with VYou authentication server.
-

To support iOS implementation, the KMM Client generates a framework that is pushed to another repository along with the ios artifacts. If you are interested in the iOS implementation, you can go [here](https://github.com/VYou-App/vyou-ios).

Additionally, we created an [application sample](https://github.com/VYou-App/vyou-mobile/blob/develop/vyou-android-compose-sample) using Jetpack Compose to check how can be implemented.

# Client

## Installation

The library is available from Amazon S3, before updating your repositories in the `settings.gradle` file include this repository:

```groovy
dependencyResolutionManagement {
    repositories {
        // Your repositories
        maven {
            url = uri("https://vyou-sdks.s3.eu-west-1.amazonaws.com/releases")
        }
    }
}
```

If you are using an older project configuration, add the repository in your `build.gradle` file at project level.

```groovy
allprojects {
    repositories {
        // Your repositories
        maven {
            url = uri("https://vyou-sdks.s3.eu-west-1.amazonaws.com/releases")
        }
    }
}
```

To add the client library to your app, open your module's `build.gradle` file and add the following:

```groovy
dependencies {
    // Your dependencies
    implementation "com.vyou:android-client:$vyou_version"
}
```

## Getting Started

Before we start, we need to update the module's `AndroidManifest.xml` file and add the following permission, to perform network operations:

```xml

<uses-permission android:name="android.permission.INTERNET" />
```

### Initialize the client

The first step is to initialise the `VYou` class, which is the main entry point for all the operations in the library. `VYou` is a class that contains all the operations you can perform using the SDK; you will use this method once, generate a single instance and use the rest of the methods in your
application.

To create the client, we need to invoke the builder class related with `VYou` class and pass it the required parameters:

```kotlin
VYou.Builder(clientId = "$VYOU_CLIENT_ID", serverUrl = "$VYOU_SERVER_URL") {
    androidContext(applicationContext)
}
```

These variables are provided by the VYou staff, the approach used in the samples is including this data through the application level `build.gradle` file, such as:

```groovy
resValue "string", "vyou_client_id", "${VYOU_CLIENT_ID}"
resValue "string", "vyou_server_url", "${VYOU_SERVER_URL}"
```

This allows you to setup those values by environment read them from any external file (such as local.properties) in case you need to.

Also, notice that the builder needs a `KoinAppDeclaration` because we use [Koin](https://insert-koin.io/) as our Dependency Injector in the client, the only needed is to pass a lambda with the `androidContext` as explained previously.

Additionally, the builder can be configurated with some optional functions. If you are using `Koin` in your project, you must initialize `Koin` from our implementation and you can import your own `koin` modules through our `addModule` and `addModules` functions and you can add as many as you want.

```kotlin
builder.addModule(module)
//also
builder.addModules(listOfModules)
```

Also to help you to track the network operations you can change the log level, by default is `none`.

```kotlin
builder.enableNetworkLogs(VYouLogLevel.all)
```

VYouLogLevel is a `enum class` and you can select between `all`, `info`, `headers`, `body` or `none` they will affect to our [Ktor](https://ktor.io/docs/client-logging.html) implementation used by the client.

Lastly, we have two callbacks, first we have `addOnRefreshTokenFailure` when the refresh token is invalidated by the server and `addOnSignOut` after the signOut is completed successfully. Both callbacks aims to help you manage the application status when these events occur.

```kotlin
builder.addOnRefreshTokenFailure { throwable ->
    //To do after event
}
builder.addOnSignOut {
    //To do after event
}
```

Even if you use the optional functions or not, to finally initialize the client you need to invoke the `initialize` function of the builder.

```kotlin
builder.initialize()
```

A recommended practice is to initialize `VYou` in the `Application` class:

```kotlin
class App : Application() {
    override fun onCreate() {
        super.onCreate()
        initializeVYou()
    }

    private fun initializeVYou() {
        VYou.Builder(clientId = "{VYOU_CLIENT_ID}", serverUrl = "{VYOU_SERVER_URL}") {
            androidContext(applicationContext)
        }
            //additional functions if needed
            .initialize()
    }
}
```

### Using the client

After initialize the client, we can use it through the application calling `VYou.instance()`. If the client is not initialized before call instance method, will throw an error related. The client provides functionality to authenticate an user and manage the user's information.

All the methods related with network operations are `suspend` functions using `coroutines` in order to use SDK, you must use coroutines in the application.

### Sign in

```kotlin
//Ex: VYou.instance().signIn(params)

//VYouSignInProvider.UserPassword(username, password)
suspend fun signIn(params: VYouSignInProvider.UserPassword): VYouCredentials

//VYouSignInProvider.Google(googleIdToken)
suspend fun signInGoogle(params: VYouSignInProvider.Google): VYouCredentials

//VYouSignInProvider.Facebook(facebookAccessToken)
suspend fun signInFacebook(params: VYouSignInProvider.Facebook): VYouCredentials
```

Each method returns a `VYouCredentials` class that contains the information to authorize any call covered by VYou auth server. Internally, this information is being saved in a encrypted shared preferences and can be recovered through these credentials methods along helper methods to check the user's
session.

```kotlin
//Ex: VYou.instance().accessToken()
fun isLoggedIn(): Boolean
fun tokenType(): String
fun accessToken(): String
fun credentials(): VYouCredentials
fun isValidToken(): Boolean
```

If you have already a Google or Facebook implementation in your application, you can use these methods to sign in into VYou, further we will explain the social artifacts mentioned at the [overview](#overview) that covers the implementation of social libraries.

### Sign up

To register a new user in the platform, the following three steps are required. Firstly, the new user is registered by providing an email along with the acceptance of the required terms of use and privacy policy provided by the application, optionally a boolean information can be added for any
marketing purpose.

```kotlin
//Ex: VYou.instance().signUp(params)

//VYouSignUpParams(email, termsOfUseAccepted, privacyPolicyAccepted, infoAccepted)
suspend fun signUp(params: VYouSignUpParams)
```

After signing up, the user will receive a confirmation code in the email provided in the previous step. The user must copy this code and enter it to verify registration.

```kotlin
//Ex: VYou.instance().signUpVerify(params)

//VYouSignUpVerifyParams(code)
suspend fun signUpVerify(params: VYouSignUpVerifyParams)
```

Lastly, if the code provided is valid, the user must register a password related with the email provided using this method:

```kotlin
//Ex: VYou.instance().signUpPassword(params)

//VYouSignUpPasswordParams(password)
suspend fun signUpPassword(params: VYouSignUpPasswordParams)
```

All these steps are needed to ensure all security steps related to the OAuth protocol. After password successful registration, the user can use [sign in](#sign-in) method to log in to the platform.

## Profile

Users have access to the information they provide to the application via VYou. To retrieve the current information they have the following method.

```kotlin
//Ex: VYou.instance().getProfile()

suspend fun getProfile(): VYouProfile
```

`VYouProfile` is a class containing the registered email, custom fields and the compliance status related to the tenant. If the status is false, the application should request the required fields from the user.

To update your information you have another method to modify the custom fields related to the tenant.

```kotlin
//Ex: VYou.instance().editProfile(params)

//VYouEditProfileParams(fields)
suspend fun editProfile(params: VYouEditProfileParams): VYouProfile
```

Is not required to provide all the fields, only the ones you want to be modified.

## Other methods

To **log out** from the platform and clear the saved credentials, there is a sign out method

```kotlin
//Ex: VYou.instance().signOut()

suspend fun signOut()
```

Users can also **reset password** using the following method, after a successful call they will receive an email with a confirmation token and they must proceed as in the [sign up](#sign-up) process after register the email.

```kotlin
//Ex: VYou.instance().resetPassword(params)

//VYouResetPasswordParams(email)
suspend fun resetPassword(params: VYouResetPasswordParams)
```

If any call returns a `401 - Forbidden` http code, it means that the access token used is expired and should be refresh, to do that must use the **refresh token** method

```kotlin
//Ex: VYou.instance().refreshToken()

suspend fun refreshToken(): VYouCredentials
```

This must be implemented on a network interceptor to provide a seamless user experience, if the refresh token fails, the user must log in again to retrieve a new access token.
