# SImgurUploader
> **Step 1.** Add the JitPack repository to your build file
```gradle
allprojects {
    repositories {
	...
	maven { url 'https://jitpack.io' }
    }
}
```
> **Step 2.** Add the dependency
```gradle
dependencies {
    implementation 'com.github.smith8h:SImgurUploader:1.0'
}
```

# Documentation
```java
String clientId = "your-client-id";
Steing accessToken = "your-access-token";
SImgurUploader siu = new SImgurUploader(clientId, accessToken);
siu.setMaxRetries(5);
siu.setMediaType(SImgurUploader.MEDIA_TYPE_PNG); // MEDIA_TYPE_JPG
siu.uploadImage(new File("path/to/image"), new SImgurResponse() {
    @Override public void onSuccess(String imageURL) {
        // use imageURL
    }
    
    @Override public void onError(String message) {
        // use message
    }
});
```
