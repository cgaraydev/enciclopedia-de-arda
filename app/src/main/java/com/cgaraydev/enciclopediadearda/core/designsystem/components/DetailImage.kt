package com.cgaraydev.enciclopediadearda.core.designsystem.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clipToBounds
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import coil3.compose.SubcomposeAsyncImage
import coil3.request.ImageRequest
import coil3.request.crossfade
import com.cgaraydev.enciclopediadearda.R

private const val IMAGE_BASE_URL =
    "https://enciclopediadearda-repo.github.io/app-images/"

@Composable
fun DetailImage(
    imagePath: String?,
    detailName: String,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    val description = stringResource(
        R.string.image_content_description,
        detailName
    )
    val path = imagePath?.trim()?.takeIf { it.isNotEmpty() }

    if (path == null) {
        DetailImagePlaceholder(
            description = description,
            modifier = modifier
        )
        return
    }

    val request = remember(context, path) {
        ImageRequest.Builder(context)
            .data(IMAGE_BASE_URL + path.trimStart('/'))
            .crossfade(true)
            .build()
    }

    SubcomposeAsyncImage(
        model = request,
        contentDescription = description,
        contentScale = ContentScale.FillWidth,
        modifier = modifier
            .fillMaxWidth()
            .clipToBounds(),
        loading = {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 24.dp),
                contentAlignment = Alignment.Center
            ) {
                LoadingIndicator()
            }
        },
        error = {
            DetailImagePlaceholder(description = null)
        }
    )
}

@Composable
private fun DetailImagePlaceholder(
    description: String?,
    modifier: Modifier = Modifier
) {
    Image(
        painter = painterResource(R.drawable.placeholder02),
        contentDescription = description,
        contentScale = ContentScale.FillWidth,
        modifier = modifier.fillMaxWidth()
    )
}