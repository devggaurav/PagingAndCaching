package com.gc.pagingandcaching.data.mappers

import com.gc.pagingandcaching.data.local.BeerEntity
import com.gc.pagingandcaching.data.remote.BeerDto
import com.gc.pagingandcaching.domain.Beer

fun BeerDto.toBeerEntity(): BeerEntity {
    return BeerEntity(
        id = id,
        name = name,
        tagLine = tagLine,
        description = description,
        firstBrewed = first_brewed,
        imageUrl = image_url
    )


}


fun BeerEntity.toBeer(): Beer {
    return Beer(
        id = id,
        name = name,
        tagline = tagLine,
        description = description,
        firstBrewed = firstBrewed,
        imageUrl = imageUrl

    )


}