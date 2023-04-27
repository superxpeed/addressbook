package com.addressbook.util

import com.addressbook.dto.DocumentDto
import com.google.common.hash.Hashing
import java.nio.file.Path

object Utils {
    fun fillUrls(documents: List<DocumentDto>, origin: String): List<DocumentDto> {
        documents.forEach { documentDto -> documentDto.url = origin + "/rest/document/" + documentDto.id }
        return documents
    }

    fun calculateSha256(path: Path): String {
        val byteSource = com.google.common.io.Files.asByteSource(path.toFile())
        val hc = byteSource.hash(Hashing.sha256())
        return hc.toString();
    }
}