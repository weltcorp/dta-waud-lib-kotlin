# Diary

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
val config = DiaryApiConfig.Builder()
  .host("<HOST>") // optional, default localhost
  .port(9090) // optional, default 9090
  .auth("<YOUR-ACCESS-TOKEN>")
  .userId(1)
  .build()

val diaryRemoteDataSource = DiaryRemoteDataSourceGrpcImpl(config)
```

## Create a new diary
```kotlin

import com.weltcorp.dta.waud.lib.diary.DiaryApiConfig
import com.weltcorp.dta.waud.lib.diary.datasource.DiaryRemoteDataSourceGrpcImpl
import com.weltcorp.dta.waud.lib.diary.domain.model.*

suspend fun main(args: Array<String>) {
    val config = DiaryApiConfig.Builder()
        .host("dta-waud-prod.weltcorp.com")
        .auth("<YOUR-TOKEN>")
        .userId(1)
        .build()

    val diaryRemoteDataSource = DiaryRemoteDataSourceGrpcImpl(config)

    var date = 1679238000 // Mon Mar 20 2023 00:00:00 GMT+0900 (한국 표준시)

    val diaryData = DiaryData.Builder()
        .alcoholCravingScore(1)
        .alcoholConsumed(true)
        .alcoholTypeAndAmount("맥주 1병")
        .sleepQualityScore(1)
        .appetiteScore(1)
        .emotionScore(1)
        .build()

    diaryRemoteDataSource.putDiary(date, diaryData)
}
```

### Configurations
#### Host
| Environment | Host |
| --- | --- |
| dev | dta-waud-dev.weltcorp.com |
| stage | dta-waud-stage.weltcorp.com |
| prod | dta-waud-prod.weltcorp.com |

#### UserId
* UserId is required to create a new diary.

----

### Date Format
Date format is UnixTimestamp
* ex. 1677037112 (waud Feb 22 2023 12:38:32 GMT+0900)