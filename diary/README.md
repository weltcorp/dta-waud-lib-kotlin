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

    val whoList = arrayListOf<Who>()
    whoList.add(Who.FRIEND)
    whoList.add(Who.LOVER)

    val uris = arrayListOf<String>()
    uris.add("gs://user_file/17770/12/sample_image_file_01.png_1678187399")
    uris.add("gs://user_file/17770/12/sample_image_file_02.png_1678187399")

    val food = DiaryFood.Builder()
        .text("밥")
        .uris(uris)
        .build()

    // Case 1: text is not null
    val event = DiaryFeelingEvent.Builder()
        .event(FeelingEvent.FE_6)
        .text("xxxxxxxxxxxx")
        .build()

    // Case 2: text is null
//    val event = DiaryFeelingEvent.Builder()
//        .event(FeelingEvent.FE_1)
//        .build()

    val feeling = DiaryFeeling.Builder()
        .score(5)
        .event(event)
        .build()

    val significants = arrayListOf<Significant>()
    significants.add(Significant.S_2)
    significants.add(Significant.S_5)

    val diaryData = DiaryData.Builder()
        .type(DiaryType.TYPE_1)
        .date(1677037112)
        .time("08:15")
        .whoList(whoList)
        .where(Where.TABLE)
        .food(food)
        .beforeHungryScore(3)
        .afterHungryScore(4)
        .feeling(feeling)
        .significants(significants)
        .isSkip(false)
        .build()

    diaryRemoteDataSource.createDiary(diaryData)
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

### Enums
```kotlin
package com.weltcorp.dta.waud.lib.diary.domain.model

enum class DiaryType {
    TYPE_0, // 아침
    TYPE_1, // 간식
    TYPE_2, // 점심
    TYPE_3, // 간식
    TYPE_4, // 저녁
    TYPE_5, // 간식
    TYPE_6, // 추가 간식
}

enum class Who {
    ALONE, // 혼자
    FAMILY, // 가족
    FRIEND, // 친구
    COLLEAGUE, // 직장동료
    LOVER, // 연인
    OTHER, // 그 외
}

enum class Where {
    TABLE,         // 식탁에 잘 차려서
    NOT_TABLE,     // 식탁 이외의 장소
    SCHOOL_OFFICE, // 학교/직장
    RESTAURANT,    //음식점
    ETC,           // 그 외
}

enum class FeelingScoreText {
    FST_0, // 최악이었어
    FST_1, // 마음이 무거웠어
    FST_2, // 담담했어
    FST_3, // 만족스러웠어
    FST_4, // 더할 나위 없이 좋았어
}

enum class FeelingEvent {
    FE_0, // 가족과 갈등이 있었어
    FE_1, // 학교/직장에서 안 좋은 일이 있었어.
    FE_2, // 내 외모를 의식하게 됐어.
    FE_3, // 친구/연인과 안좋은 일이 있었어.
    FE_4, // 생각해 둔 메뉴를 먹지 못했어.
    FE_5, // 너무 많이 먹어 버렸다는 생각이 들었어.
    FE_6, // 직접 입력하기
}

enum class Significant {
    S_0, // 폭식
    S_1, // 먹토
    S_2, // 씹뱉
    S_3, // 과도한 운동
    S_4, // 변비약
    S_5, // 다이어트 보조제
    S_6, // 바디체크
    S_7, // 음주
}
```
### Date Format
Date format is UnixTimestamp
* ex. 1677037112 (waud Feb 22 2023 12:38:32 GMT+0900)