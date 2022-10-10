# Image Compressor

An Android image compress library, **image compressor**, is small and effective. With very little or no image quality degradation, a compressor enables you to reduce the size of large photos into smaller size photos.

[![Google DevLibrary - VinodBaste](https://img.shields.io/badge/Google_DevLibrary-VinodBaste-ea9f2d?logo=<svg+role%3D"img"+viewBox%3D"0+0+24+24"+xmlns%3D"http%3A%2F%2Fwww.w3.org%2F2000%2Fsvg"><title>Android<%2Ftitle><path+d%3D"M17.523+15.3414c-.5511+0-.9993-.4486-.9993-.9997s.4483-.9993.9993-.9993c.5511+0+.9993.4483.9993.9993.0001.5511-.4482.9997-.9993.9997m-11.046+0c-.5511+0-.9993-.4486-.9993-.9997s.4482-.9993.9993-.9993c.5511+0+.9993.4483.9993.9993+0+.5511-.4483.9997-.9993.9997m11.4045-6.02l1.9973-3.4592a.416.416+0+00-.1521-.5676.416.416+0+00-.5676.1521l-2.0223+3.503C15.5902+8.2439+13.8533+7.8508+12+7.8508s-3.5902.3931-5.1367+1.0989L4.841+5.4467a.4161.4161+0+00-.5677-.1521.4157.4157+0+00-.1521.5676l1.9973+3.4592C2.6889+11.1867.3432+14.6589+0+18.761h24c-.3435-4.1021-2.6892-7.5743-6.1185-9.4396"%2F><%2Fsvg>&logoColor=30DC80)](https://devlibrary.withgoogle.com/products/android/repos/vinodbaste-Image-compressor)

[![API](https://img.shields.io/badge/API-21%2B-brightgreen.svg?style=flat)](https://android-arsenal.com/api?level=21)
[![GitHub tag](https://img.shields.io/github/tag/vinodbaste/image-compressor?include_prereleases=&sort=semver&color=blue)](https://github.com/vinodbaste/image-compressor/releases/)
[![License](https://img.shields.io/badge/License-Apache_2.0-blue)](#license)
[![News - Android Weekly](https://img.shields.io/badge/News-Android_Weekly-d36f21)](https://androidweekly.net/issues/issue-326)
[![Story - Medium](https://img.shields.io/badge/Story-Medium-2ea44f)](https://medium.com/codex/image-compressor-13dbfd0445a3)
[![GitHub - VinodBaste](https://img.shields.io/badge/GitHub-VinodBaste-4664c6)](https://github.com/vinodbaste/Image-compressor)

<img src = "https://raw.githubusercontent.com/vinodbaste/ImageCompressor/main/image%20(2)_google-pixel4xl-clearlywhite-portrait.jpg" width = 250 height = 500 />

# How to implement
To get a Git project into your build:
## Gradle
` Step 1:` Add it in your **root build.gradle**  at the end of repositories:
```kotlin
allprojects {
		repositories {
			...
			maven { url 'https://jitpack.io' }
		}
	}
```

`Step 2:` Add the dependency in your **project build.gradle**
```kotlin
dependencies {
	        implementation 'com.github.vinodbaste:ImageCompressor:1.0.1'
}
```
# Let's compress the image size!
#### Compress Image File at the specified `imagePath`
When **compressing** a picture, add the following block of code to the **activity or fragment**. Either after taking a **picture with a camera** or selecting one **from a gallery**.
```kotlin
ImageCompressUtils.compressImage(
            context = this,
            imagePath = "actualImagePath",
            imageName = "imageName",
            imageQuality = 50
        )
```
**compressImage** takes 4 parameters where the **last one is optional**
* **context**, the current/active state of the application.
* **imagePath** parameter takes the absolute image path.
* **imageName** is completely up to the user.
* **imageQuality** is set to 50 by default. The max can be set to 100.

#### Compress Image File at the specified `imagePath` and return the `compressed ImagePath`
```kotlin
val compressedImagePath = ImageCompressUtils.compressImage(
            context = this,
            imagePath = "actualImagePath",
            imageName = "imageName",
            imageQuality = 50
        )
```
With the image name supplied, the code block above returns the path to the compressed picture.
**compressedImagePath** has the imagePath with the imageName specified.

# Example
An illustration of how the code block can be utilized.
```kotlin
 	 //absolute path of the image
        val imagePath= "actualImagePath"

        val imageFileBc = File(imagePath)
        val imageSizeBc = imageFileBc.length() / 1024 // In BYTES
        Log.d("image_before_compress", imageSizeBc.toString())

        ImageCompressUtils.compressImage(
            context = this,
            imagePath = imagePath,
            imageName = "imageName",
            imageQuality = 50
        )

        val imageFileAc = File(imagePath)
        val imageSizeAC = imageFileAc.length() / 1024 // In BYTES
        Log.d("image_after_compress", imageSizeAC.toString())

        //your function to play with compressed image
        loadCompressedImage(imagePath) 
```
# proguard-rules
```
-keepclassmembers class com.android.imagecompressor.compressImageUtils

-keep class * extends com.android.imagecompressor.compressImageUtils {
 <init>(...);
}

-dontwarn javax.annotation.Nullable
-dontwarn javax.annotation.ParametersAreNonnullByDefault
```
# Support my work
<a href="https://www.buymeacoffee.com/bastevinod" target="_blank"><img src="https://cdn.buymeacoffee.com/buttons/default-orange.png" alt="Buy Me A Coffee" height="41" width="174"></a>

# Note
```
> In the event that an image is selected from a gallery, make a copy of it and follow the path.
> The original picture path is used to compress and rewrite the image.
```
# License
```
Copyright [2022] [Vinod Baste]

Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at

    http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.
```


