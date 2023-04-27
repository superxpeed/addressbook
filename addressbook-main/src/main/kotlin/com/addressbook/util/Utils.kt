package com.addressbook.util

import com.addressbook.dto.DocumentDto
import java.nio.file.Files
import java.nio.file.Path
import java.util.zip.CRC32

object Utils {
    fun fillUrls(documents: List<DocumentDto>, origin: String): List<DocumentDto> {
        documents.forEach { documentDto -> documentDto.url = origin + "/rest/document/" + documentDto.id }
        return documents
    }

    fun calculateCrc32(path: Path): String {
        val crc = CRC32()
        Files.newInputStream(path).use { `in` ->
            var c: Int
            while (`in`.read().also { c = it } != -1) crc.update(c)
        }
        return String.format("%08X", crc.value);
    }
}