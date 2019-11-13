-keepattributes SourceFile,LineNumberTable
-renamesourcefileattribute SourceFile
-repackageclasses

-keepnames class defpackage.marsh.*
-keep,allowobfuscation class defpackage.marsh.* {
    <init>(...);
    *** marshal(...);
    *** next();
}