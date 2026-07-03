# MVP Notes release rules (minify disabled today; keep for future enablement)

# kotlinx.serialization
-keepattributes *Annotation*, InnerClasses
-dontnote kotlinx.serialization.**
-keepclassmembers class kotlinx.serialization.json.** {
    *** Companion;
}
-keepclasseswithmembers class * {
    @kotlinx.serialization.Serializable <methods>;
}

# App models
-keep class com.takuma.mvpnotes.model.** { *; }
