package io.github.kt2tssample

import kotlin.metadata.ClassName
import kotlin.metadata.Visibility
import kotlin.metadata.hasAnnotations
import kotlin.metadata.internal.common.BuiltInExtensionsAccessor.annotations
import kotlin.metadata.jvm.JvmMetadataVersion
import kotlin.metadata.jvm.KotlinClassMetadata

fun main() {
//    val classes = ClassLoader.getSystemClassLoader()
//        .definedPackages
//        .flatMap { it.declaredClasses.toList() }
stuff()
}

    fun stuff() {
    val clazz = SomeSealedClass::class.java
    clazz.annotations.forEach {
        println(it)
    }
    val metadataAnnotation: Metadata = clazz.getAnnotation(Metadata::class.java)
//    val metadataAnnotation = Metadata(
//        // pass arguments here
//    )
    val metadata = KotlinClassMetadata.readStrict(metadataAnnotation)// as KotlinClassMetadata.Class
//    val newAnnotation: Metadata = classMetadata.write()
//    println(newAnnotation)
    println(SomeSealedClass::class.java.protectionDomain.codeSource.location)
    when(metadata) {
        is KotlinClassMetadata.Class -> handle(metadata)
        is KotlinClassMetadata.FileFacade -> TODO()
        is KotlinClassMetadata.MultiFileClassFacade -> TODO()
        is KotlinClassMetadata.MultiFileClassPart -> TODO()
        is KotlinClassMetadata.SyntheticClass -> TODO()
        is KotlinClassMetadata.Unknown -> TODO()
    }
}

fun handle(klass: KotlinClassMetadata.Class){
    val k = klass.kmClass
    println(k.name)
    println(k.hasAnnotations)
    println(k.sealedSubclasses)
    val s1: ClassName = k.sealedSubclasses.get(0)
    val s1Meta = Class.forName(s1.replace("/", ".")).getAnnotation(Metadata::class.java)
    val metadata = KotlinClassMetadata.readStrict(s1Meta)  as KotlinClassMetadata.Class
    metadata.kmClass.properties.forEach {
        println(it.name)
    }
//    val m = KotlinClassMetadata.Class(klass.kmClass, JvmMetadataVersion.LATEST_STABLE_SUPPORTED, 0)
//    println(m.kmClass.hasAnnotations)
//    println(m.kmClass.annotations)

}
