# CAU_CCUDA

## Configuration

Create `Config.java` on the package source directory which contains:

```java
package com.example.ccuda;

public class Config {
    public static final String SERVER_URL = "http://your.server.com";
    public final static String VALIDATION_EMAIL_ADDRESS = "your@email.com";
    public final static String VALIDATION_EMAIL_PASSWORD = "youremailpassword" ;
}
```

## kakao SDK

create `kakao_app_key.xml` on the ../res/values which contains:

```xml
<?xml version="1.0" encoding="utf-8"?>
<resources>
    <string name="kakao_app_key">your kakao_native_key</string>
    <string name="kakao_scheme">your kakao key</string>
</resources>
```


## 사용자 매뉴얼
[사용자 매뉴얼](https://github.com/yhb1834/CAU_CCUDA/사용자메뉴얼.pdf)
