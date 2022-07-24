# Image Compressor

An Android image compress library, **image compressor**, is small and effective. With very little or no image quality degradation, a compressor enables you to reduce the size of large photos into smaller photos.

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


