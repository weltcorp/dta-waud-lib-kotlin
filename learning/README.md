# Learning

## Documentation
The documentation for the dta-waud API can be found [here]().

## Versions
[![](https://jitpack.io/v/weltcorp/dta-waud-lib-kotlin.svg)](https://jitpack.io/#weltcorp/dta-waud-lib-kotlin)

## Installation
To get a Git project into your build:
### Gradle
#### Step 1. 
Add the JitPack repository to your build file
Add it in your root build.gradle at the end of repositories:
```gradle
allprojects {
    repositories {
        ...
        maven { url 'https://jitpack.io' }
    }
}
```
#### Step 2. 
Add the dependency

```gradle
  dependencies {
    implementation 'com.github.weltcorp:dta-waud-lib-kotlin:Tag'
  }
```
### Maven
#### Step 1.
Add the JitPack repository to your build file
```xml
	<repositories>
      <repository>
        <id>jitpack.io</id>
        <url>https://jitpack.io</url>
      </repository>
	</repositories>
```
#### Step 2.
Add the dependency

```gradle
	<dependency>
      <groupId>com.github.weltcorp</groupId>
      <artifactId>dta-waud-lib-kotlin</artifactId>
      <version>Tag</version>
	</dependency>
```

## Quickstart

### Initialize the Client
```kotlin
val config = LearningApiConfig.Builder()
  .host("<HOST>") // optional, default localhost
  .port(9090) // optional, default 9090
  .auth("<YOUR-ACCESS-TOKEN>")
  .userId(1)
  .build()

val learningRemoteDataSource = LearningRemoteDataSourceGrpcImpl(config)
```

## Get Learning Session Item
```kotlin
import com.weltcorp.dta.waud.lib.learning.LearningApiConfig
import com.weltcorp.dta.waud.lib.learning.datasource.LearningRemoteDataSourceGrpcImpl

suspend fun main(args: Array<String>) {
    val config = LearningApiConfig.Builder()
        .host("dta-waud-api-dev.weltcorp.com")
        .port(443) // The port can be removed, if you don't need.
        .auth("<YOUR-TOKEN>")
        .build()

    val learningRemoteDataSource = LearningRemoteDataSourceGrpcImpl(config)

    val userId = 100
    
    val res = learningRemoteDataSource.getSessionItems(userId)
    
    println(res)
}
```

### Configurations
#### Host
| Environment | Host                            |
| --- |---------------------------------|
| dev | dta-waud-api-dev.weltcorp.com   |
| stage | dta-waud-api-stage.weltcorp.com |
| prod | dta-waud-api-prod.weltcorp.com     |

#### UserId
* UserId is required to get sessionItems.

