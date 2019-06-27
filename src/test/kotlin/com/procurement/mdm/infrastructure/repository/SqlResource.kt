package com.procurement.mdm.infrastructure.repository

import java.nio.charset.Charset
import java.nio.file.Files
import java.nio.file.Paths

fun loadSql(fileName: String): String {
    return ClassPathResource.getFilePath(fileName)
        ?.let { pathToFile ->
            val path = Paths.get(pathToFile)
            val buffer = Files.readAllBytes(path)
            String(buffer, Charset.defaultCharset())
        } ?: throw IllegalArgumentException("Error loading SQL. File by path: $fileName is not found.")
}

private object ClassPathResource {
    fun getFilePath(fileName: String): String? = javaClass.classLoader.getResource(fileName)?.path
}
