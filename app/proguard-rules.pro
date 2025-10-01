# Add project specific ProGuard rules here.
# You can control the set of applied configuration files using the
# proguardFiles setting in build.gradle.
#
# For more details, see
#   http://developer.android.com/guide/developing/tools/proguard.html

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
# Keep all Kotlin metadata for reflection
-keep class kotlin.** { *; }

# Keep Jetpack Compose classes and runtime annotations
-keep class androidx.compose.** { *; }
-keep @androidx.compose.runtime.Composable class * { *; }

# Preserve Jetpack Compose-generated functions and methods
-keepclassmembers class * {
    @androidx.compose.runtime.Composable <methods>;
}

# Keep all classes and methods in your app package
-keep class com.honeypot.app.** { *; }

# Keep custom UI components and inner classes
-keep class com.honeypot.app.ui.** { *; }

# Ensure Composer constructor is not removed or obfuscated
-keepclassmembers class * {
    public <init>(androidx.compose.runtime.Composer, int, int);
}

# Preserve generic types if needed
-keepnames class * { *; }

# Optional: Debugging
# Suppress warnings from libraries you trust (optional)
-dontwarn androidx.compose.**
-dontwarn kotlinx.**

# Enable this if your app uses reflection
-keepattributes *Annotation*
-keep class kotlinx.coroutines.internal.** { *; }
-keep class kotlinx.coroutines.** { *; }

# Optimize only if you're sure
-optimizations !code/simplification/arithmetic,!field/*,!class/merging/*

# Remove log calls in release builds
-assumenosideeffects class android.util.Log {
    public static *** d(...);
    public static *** i(...);
    public static *** w(...);
    public static *** e(...);
    public static *** v(...);
}
