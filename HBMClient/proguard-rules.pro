# Add project specific ProGuard rules here.
# By default, the flags in this file are appended to flags specified
# in E:\Jing\worksoft\sdk/tools/proguard/proguard-android.txt
# You can edit the include path and order by changing the proguardFiles
# directive in build.gradle.
#
# For more details, see
#   http://developer.android.com/guide/developing/tools/proguard.html

# Add any project specific keep options here:

# If your project uses WebView with JS, uncomment the following
# and specify the fully qualified class name to the JavaScript interface
# class:
#-keepclassmembers class fqcn.of.javascript.interface.for.webview {
#   public *;
#}

# tencet bugly start
-dontwarn com.tencent.bugly.**
-keep public class com.tencent.bugly.**{*;}
# tencet bugly end

-optimizationpasses 5
-dontusemixedcaseclassnames
-dontskipnonpubliclibraryclasses
-dontpreverify
-ignorewarnings
-verbose
-optimizations !code/simplification/arithmetic,!field/*,!class/merging/*

-keep public class * extends android.app.Activity
-keep public class * extends android.app.Application
-keep public class * extends android.app.Service
-keep public class * extends android.content.BroadcastReceiver
-keep public class * extends android.content.ContentProvider
-keep public class * extends android.app.backup.BackupAgentHelper
-keep public class * extends android.preference.Preference
-keep public class com.android.vending.licensing.ILicensingService
-keep class com.google.gson.** {*;}
-keep class org.apache.http.** {*;}
-keep class android.support.v4.os.** {*;}
-keep class com.mob.** {*;}
-keep class cn.sharesdk.** {*;}

-keep class org.apache.cordova.** { *; }
-keep public class * extends org.apache.cordova.CordovaPlugin
-keep public class * extends org.apache.cordova.CordovaActivity
-keepclassmembers class fqcn.of.javascript.interface.for.webview {
   public *;
}



-repackageclasses '' -allowaccessmodification -optimizations !code/simplification/arithmetic
-keepattributes *Annotation*

-keepclassmembers class com.bonc.mobile.hbmclient.TestActivity {
   public *;
}

-keepattributes Signature
-keepattributes JavascriptInterface

-keepclassmembers class com.bonc.mobile.hbmclient.view.KpiJSInterface {
   public *;
}

#http strat
-dontwarn org.apache.commons.**
-keep class org.apache.http.** { *; }
-dontwarn org.apache.http.**
-dontwarn org.apache.**
-keep class org.apache.**{*;}
#http end

-dontwarn com.fasterxml.jackson.databind.**
-dontwarn org.apache.http.**
-dontwarn android.net.http.AndroidHttpClient

-keep public class com.bonc.mobile.hbmclient.util.JsonUtil {
 *;
}


-keep class com.bonc.mobile.hbmclient.util.JsonUtil$* {
*;
}

-keepclasseswithmembernames class com.bonc.mobile.hbmclient.aop.dynamic_proxy.business_logic.** {
	*;
}

-keepclasseswithmembernames class com.bonc.mobile.hbmclient.aop.dynamic_proxy.handler.** {
	*;
}

-keepclasseswithmembernames class com.bonc.mobile.hbmclient.aop.dynamic_proxy.interceptor.** {
	*;
}

-keepclasseswithmembernames class * {
    native <methods>;
}

-keepclasseswithmembers class * {
    public <init>(android.content.Context, android.util.AttributeSet);
}

-keepclasseswithmembers class * {
    public <init>(android.content.Context, android.util.AttributeSet, int);
}

-keepclassmembers class * extends android.app.Activity {
   public void *(android.view.View);
}

-keepclassmembers enum * {
    public static **[] values();
    public static ** valueOf(java.lang.String);
}

-keep class * implements android.os.Parcelable {
  public static final android.os.Parcelable$Creator *;
}