# PgyPluginApplication

### this is a plugin for upload apk to pgyer

 打包将apk上传到蒲公英，并发送消息到企业微信群

### Usage:

1. `Using the plugins DSL:`

   ```groovy
   plugins {
     id "cn.keepcoded.pgyplugin" version "1.0.0"
   }
   ```

   

2. `Using legacy plugin applicaiton:`

   ```groovy
   buildscript {
     repositories {
       maven {
         url "https://plugins.gradle.org/m2/" //或者使用https://maven.aliyun.com/repository/gradle-plugin
       }
     }
     dependencies {
       classpath "cn.keepcoded:pgyplugin:1.0.0"
     }
   }
   ```

3. 在app目录下的build.gradle文件添加如下代码：
   ```groovy
   uploadPgy {
       apiKey  "蒲公英的_api_key"
       webHookUrlKey  "钉钉机器人WebHook地址key"
   }
   ```

4. 使用：

   ```
   ./gradlew uploadPgyTaskddebug
   ```



