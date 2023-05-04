package com.addressbook.util

import com.addressbook.dto.DocumentDto
import com.google.common.hash.Hashing
import java.nio.file.Path
import java.text.CharacterIterator
import java.text.StringCharacterIterator

object Utils {
    fun fillUrls(documents: List<DocumentDto>, origin: String): List<DocumentDto> {
        documents.forEach { documentDto -> documentDto.url = origin + "/rest/document/" + documentDto.id }
        return documents
    }

    fun calculateSha256(path: Path): String {
        val byteSource = com.google.common.io.Files.asByteSource(path.toFile())
        return byteSource.hash(Hashing.sha256()).toString()
    }

    fun humanReadableByteCount(size: Long): String {
        var bytes = size
        if (-1000 < bytes && bytes < 1000) {
            return "$bytes B"
        }
        val ci: CharacterIterator = StringCharacterIterator("KMGTPE")
        while (bytes <= -999950 || bytes >= 999950) {
            bytes /= 1000
            ci.next()
        }
        return String.format("%.1f %cB", bytes / 1000.0, ci.current())
    }
}