package com.cgaraydev.enciclopediadearda.core.designsystem.components

import android.text.style.URLSpan
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clipToBounds
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.IntSize
import androidx.compose.ui.unit.dp
import androidx.core.text.HtmlCompat
import coil3.compose.SubcomposeAsyncImage
import coil3.request.ImageRequest
import coil3.request.crossfade
import com.cgaraydev.enciclopediadearda.R
import com.cgaraydev.enciclopediadearda.core.model.CatalogImage
import com.cgaraydev.enciclopediadearda.ui.theme.TolkienGold
import com.cgaraydev.enciclopediadearda.ui.theme.TolkienGray04
import androidx.compose.foundation.gestures.rememberTransformableState
import androidx.compose.foundation.gestures.transformable

@Composable
fun ImageCarousel(
    images: List<CatalogImage>,
    onLinkClick: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    if (images.isEmpty()) return

    val pagerState = rememberPagerState(
        pageCount = { images.size }
    )
    val currentPage = pagerState.currentPage

    var scale by remember(images, currentPage) {
        mutableFloatStateOf(1f)
    }
    var offsetX by remember(images, currentPage) {
        mutableFloatStateOf(0f)
    }
    var offsetY by remember(images, currentPage) {
        mutableFloatStateOf(0f)
    }


    val context = LocalContext.current

    Column(modifier = modifier.fillMaxWidth()) {
        CategoriesHeader(textRes = R.string.images)

        HorizontalPager(
            state = pagerState,
            userScrollEnabled = scale == 1f,
            modifier = Modifier.fillMaxWidth()
        ) { pageIndex ->
            val image = images[pageIndex]
            val isCurrentPage = pageIndex == currentPage

            var viewportSize by remember {
                mutableStateOf(IntSize.Zero)
            }

            val transformState = rememberTransformableState { zoomChange, panChange, _ ->
                if (isCurrentPage) {
                    val previousScale = scale
                    val newScale = (previousScale * zoomChange)
                        .coerceIn(1f, 5f)
                    val scaleRatio = newScale / previousScale

                    val maxX = (newScale - 1f) * viewportSize.width / 2f
                    val maxY = (newScale - 1f) * viewportSize.height / 2f

                    offsetX = (offsetX * scaleRatio + panChange.x)
                        .coerceIn(-maxX, maxX)
                    offsetY = (offsetY * scaleRatio + panChange.y)
                        .coerceIn(-maxY, maxY)

                    scale = newScale
                }
            }

            val credits = remember(image.artistHtml) {
                HtmlCompat.fromHtml(
                    image.artistHtml.orEmpty(),
                    HtmlCompat.FROM_HTML_MODE_COMPACT
                )
            }
            val hasExternalLink = remember(credits) {
                credits.getSpans(
                    0,
                    credits.length,
                    URLSpan::class.java
                ).any { span ->
                    span.url.startsWith("https://") ||
                            span.url.startsWith("http://")
                }
            }

            val baseDescription = stringResource(
                R.string.gallery_image_description,
                pageIndex + 1,
                images.size
            )
            val creditText = credits.toString().trim()
            val description = if (creditText.isEmpty()) {
                baseDescription
            } else {
                "$baseDescription. $creditText"
            }

            val request = remember(context, image.imagePath) {
                ImageRequest.Builder(context)
                    .data(
                        image.imagePath
                            ?.trim()
                            ?.takeIf { it.isNotEmpty() }
                            ?.let { IMAGE_BASE_URL + it.trimStart('/') }
                    )
                    .crossfade(true)
                    .build()
            }

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 8.dp)
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(350.dp)
                        .clipToBounds()
                        .onSizeChanged { viewportSize = it }
                        .pointerInput(isCurrentPage) {
                            detectTapGestures(
                                onDoubleTap = {
                                    if (isCurrentPage) {
                                        scale = when {
                                            scale < 2f -> 2f
                                            scale < 4f -> 4f
                                            else -> 1f
                                        }
                                        offsetX = 0f
                                        offsetY = 0f
                                    }
                                }
                            )
                        }
                        .transformable(
                            state = transformState,
                            canPan = { scale > 1f },
                            enabled = isCurrentPage,
                            lockRotationOnZoomPan = true
                        )
                ) {
                    SubcomposeAsyncImage(
                        model = request,
                        contentDescription = description,
                        contentScale = ContentScale.Fit,
                        modifier = Modifier
                            .fillMaxSize()
                            .graphicsLayer {
                                scaleX = if (isCurrentPage) scale else 1f
                                scaleY = if (isCurrentPage) scale else 1f
                                translationX = if (isCurrentPage) offsetX else 0f
                                translationY = if (isCurrentPage) offsetY else 0f
                            },
                        loading = {
                            Box(
                                modifier = Modifier.fillMaxSize(),
                                contentAlignment = Alignment.Center
                            ) {
                                LoadingIndicator()
                            }
                        },
                        error = {
                            Box(
                                modifier = Modifier.fillMaxSize(),
                                contentAlignment = Alignment.Center
                            ) {
                                Icon(
                                    painter = painterResource(
                                        R.drawable.ic_unknown
                                    ),
                                    contentDescription = stringResource(
                                        R.string.error_loading_image
                                    ),
                                    tint = TolkienGray04,
                                    modifier = Modifier.size(48.dp)
                                )
                            }
                        }
                    )
                }

                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(64.dp)
                        .padding(horizontal = 12.dp, vertical = 8.dp),
                    contentAlignment = Alignment.Center
                ) {
                    image.artistHtml
                        ?.takeIf { it.isNotBlank() }
                        ?.let { artist ->
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(
                                    10.dp
                                )
                            ) {
                                HtmlText(
                                    html = artist,
                                    onLinkClick = onLinkClick,
                                    modifier = Modifier.weight(1f),
                                    textAlign = TextAlign.End
                                )

                                if (hasExternalLink) {
                                    Icon(
                                        painter = painterResource(
                                            R.drawable.ic_link
                                        ),
                                        contentDescription = null,
                                        tint = TolkienGold,
                                        modifier = Modifier.size(16.dp)
                                    )
                                }
                            }
                        }
                }
            }
        }

        Spacer(Modifier.height(20.dp))

        Row(
            modifier = Modifier.align(Alignment.CenterHorizontally),
            horizontalArrangement = Arrangement.spacedBy(6.dp)
        ) {
            repeat(images.size) { index ->
                val color = if (index == currentPage) {
                    TolkienGold
                } else {
                    TolkienGray04
                }

                Box(
                    modifier = Modifier
                        .size(8.dp)
                        .drawBehind { drawCircle(color = color) }
                )
            }
        }

        Spacer(Modifier.height(20.dp))
    }
}