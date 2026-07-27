# ProGuard rules for HKTaxiNaviMap

# Keep Amap SDK
-keep class com.amap.api.** { *; }
-keep class com.autonavi.** { *; }
-keep class com.loc.** { *; }
-dontwarn com.amap.api.**
-dontwarn com.autonavi.**

# Keep Retrofit
-keepattributes Signature
-keepattributes *Annotation*
-keep class com.nicklam.hktaxinavi.data.api.** { *; }

# Keep Gson serialized classes
-keep class com.nicklam.hktaxinavi.data.model.** { *; }
