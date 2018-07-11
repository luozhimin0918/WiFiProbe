# Add project specific ProGuard rules here.
# By default, the flags in this file are appended to flags specified
# in D:\Android\sdk/tools/proguard/proguard-android.txt
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

# Uncomment this to preserve the line number information for
# debugging stack traces.
#-keepattributes SourceFile,LineNumberTable

# If you keep the line number information, uncomment this to
# hide the original source file name.
#-renamesourcefileattribute SourceFile


# 保留我们使用的四大组件，自定义的Application等等这些类不被混淆
# 因为这些子类都有可能被外部调用
-keep public class * extends android.app.Activity
-keep public class * extends android.app.Appliction
-keep public class * extends android.app.Service
-keep public class * extends android.content.BroadcastReceiver
-keep public class * extends android.content.ContentProvider
-keep public class * extends android.app.backup.BackupAgentHelper
-keep public class * extends android.preference.Preference
-keep public class * extends android.view.View
-keep public class com.android.vending.licensing.ILicensingService


# 保留support下的所有类及其内部类
-keep class android.support.** {*;}

# 保留继承的
-keep public class * extends android.support.v4.**
-keep public class * extends android.support.v7.**
-keep public class * extends android.support.annotation.**

# 保留R下面的资源
-keep class **.R$* {*;}

# 保留本地native方法不被混淆
-keepclasseswithmembernames class * {
    native <methods>;
}

# 保留在Activity中的方法参数是view的方法，
# 这样以来我们在layout中写的onClick就不会被影响
-keepclassmembers class * extends android.app.Activity{
    public void *(android.view.View);
}

# 保留枚举类不被混淆
-keepclassmembers enum * {
    public static **[] values();
    public static ** valueOf(java.lang.String);
}

# 保留我们自定义控件（继承自View）不被混淆
-keep public class * extends android.view.View{
    *** get*();
    void set*(***);
    public <init>(android.content.Context);
    public <init>(android.content.Context, android.util.AttributeSet);
    public <init>(android.content.Context, android.util.AttributeSet, int);
}

# 保留Parcelable序列化类不被混淆
-keep class * implements android.os.Parcelable {
    public static final android.os.Parcelable$Creator *;
}

# 保留Serializable序列化的类不被混淆
-keepclassmembers class * implements java.io.Serializable {
    static final long serialVersionUID;
    private static final java.io.ObjectStreamField[] serialPersistentFields;
    !static !transient <fields>;
    !private <fields>;
    !private <methods>;
    private void writeObject(java.io.ObjectOutputStream);
    private void readObject(java.io.ObjectInputStream);
    java.lang.Object writeReplace();
    java.lang.Object readResolve();
}

# 对于带有回调函数的onXXEvent、**On*Listener的，不能被混淆
-keepclassmembers class * {
    void *(**On*Event);
    void *(**On*Listener);
}

# Gson
-keepattributes *Annotation*
-keep class sun.misc.Unsafe.** { *; }
-keep class com.google.gson.stream.** { *; }
# 使用Gson时需要配置Gson的解析对象及变量都不混淆。不然Gson会找不到变量。
# 将下面替换成自己的实体类
-keep class com.ums.wifiprobe.net.** { *; }
-keep class com.ums.wifiprobe.service.greendao.** { *; }
#-keep class com.ums.wifiprobe.service.probeentity.** { *; }

-dontwarn com.ums.anypay.service.**
-keep class com.ums.anypay.service.** {*;}

-dontwarn com.ums.cashier.**
-keep class com.ums.cashier.** {*;}

-dontwarn com.ums.plugin.**
-keep class com.ums.plugin.** {*;}

-dontwarn com.ums.upos.**
-keep class com.ums.upos.** {*;}

-dontwarn com.appinterface.update.**
-keep class com.appinterface.update.** {*;}

-dontwarn retrofit2.**
-keep class retrofit2.** {*;}

-dontwarn net.sqlcipher.**
-keep class net.sqlcipher.** {*;}

-dontwarn com.bigkoo.pickerview.**
-keep class com.bigkoo.pickerview.** {*;}

-dontwarn butterknife.**
-keep class butterknife.** {*;}

-dontwarn com.google.common.**
-keep class com.google.common.** {*;}

-dontwarn org.greenrobot.greendao.**
-keep class org.greenrobot.greendao.** {*;}

-dontwarn org.hamcrest.**
-keep class org.hamcrest.** {*;}

-dontwarn com.squareup.javawriter.**
-keep class com.squareup.javawriter.** {*;}

-dontwarn com.nineoldandroids.**
-keep class com.nineoldandroids.** {*;}

-dontwarn com.github.mikephil.charting.**
-keep class com.github.mikephil.charting.** {*;}

-dontwarn okhttp3.**
-keep class okhttp3.** {*;}

-dontwarn okio.**
-keep class okio.** {*;}

-dontwarn com.squareup.picasso.**
-keep class com.squareup.picasso.** {*;}

-dontwarn com.codbking.widget.**
-keep class com.codbking.widget.** {*;}

-dontwarn in.srain.cube.views.ptr.**
-keep class in.srain.cube.views.ptr.** {*;}

-dontwarn rx.**
-keep class rx.** {*;}

-dontwarn com.marswin89.marsdaemon.**
-keep class com.marswin89.marsdaemon.** {*;}