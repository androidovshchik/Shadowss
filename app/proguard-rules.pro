-keepattributes SourceFile,LineNumberTable
-renamesourcefileattribute SourceFile
-repackageclasses

-keepnames class defpackage.marsh.*
-keep class defpackage.marsh.* {
    <init>(...);
    *** marshal(...);
    *** next();
}