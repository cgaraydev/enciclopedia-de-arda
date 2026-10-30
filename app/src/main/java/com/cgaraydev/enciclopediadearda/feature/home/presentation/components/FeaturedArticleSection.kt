package com.cgaraydev.enciclopediadearda.feature.home.presentation.components

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.LocalIndication
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsPressedAsState
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.widthIn
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clipToBounds
import androidx.compose.ui.draw.drawWithContent
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shadow
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil3.compose.AsyncImage
import coil3.request.ImageRequest
import coil3.request.crossfade
import com.cgaraydev.enciclopediadearda.R
import com.cgaraydev.enciclopediadearda.core.designsystem.components.CategoriesHeader
import com.cgaraydev.enciclopediadearda.core.designsystem.components.tolkienShimmerBrush
import com.cgaraydev.enciclopediadearda.core.images.CatalogImageUrls
import com.cgaraydev.enciclopediadearda.feature.characters.domain.Character
import com.cgaraydev.enciclopediadearda.ui.theme.RingBearer
import com.cgaraydev.enciclopediadearda.ui.theme.TolkienBlack
import com.cgaraydev.enciclopediadearda.ui.theme.TolkienGold
import com.cgaraydev.enciclopediadearda.core.model.ArticleReference
import com.cgaraydev.enciclopediadearda.core.model.CategoryItem

@Composable
fun FeaturedArticleSection(
    article: CategoryItem,
    onArticleClick: (ArticleReference) -> Unit,
    modifier: Modifier = Modifier
) {
    Column(modifier = modifier.fillMaxWidth()) {
        CategoriesHeader(textRes = R.string.featured_article)

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 8.dp, vertical = 8.dp),
            contentAlignment = Alignment.Center
        ) {
            FeaturedCard(
                imagePath = article.imagePath,
                title = article.name,
                onClick = {
                    onArticleClick(article.reference)
                }
            )
        }

        Spacer(Modifier.height(40.dp))
    }
}

@Composable
private fun FeaturedCard(
    imagePath: String?,
    title: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    val interactionSource = remember { MutableInteractionSource() }
    val isPressed by interactionSource.collectIsPressedAsState()

    val scale by animateFloatAsState(
        targetValue = if (isPressed) 0.97f else 1f,
        animationSpec = tween(120),
        label = "featuredPress"
    )

    val shimmerBrush = tolkienShimmerBrush(duration = 3000)
    val placeholder = painterResource(R.drawable.placeholder02)

    val request = remember(context, imagePath) {
        ImageRequest.Builder(context)
            .data(CatalogImageUrls.resolve(imagePath))
            .crossfade(true)
            .build()
    }

    Box(
        modifier = modifier
            .widthIn(max = 300.dp)
            .fillMaxWidth()
            .aspectRatio(1f)
            .graphicsLayer {
                scaleX = scale
                scaleY = scale
            }
            .clipToBounds()
            .clickable(
                interactionSource = interactionSource,
                indication = LocalIndication.current,
                role = Role.Button,
                onClickLabel = stringResource(
                    R.string.view_details,
                    title
                ),
                onClick = onClick
            )
            .drawWithContent {
                drawContent()

                val borderWidth = 2.dp.toPx()

                drawRect(
                    brush = shimmerBrush,
                    topLeft = Offset(
                        borderWidth / 2f,
                        borderWidth / 2f
                    ),
                    size = Size(
                        size.width - borderWidth,
                        size.height - borderWidth
                    ),
                    style = Stroke(width = borderWidth)
                )
            },
        contentAlignment = Alignment.BottomCenter
    ) {
        AsyncImage(
            model = request,
            contentDescription = null,
            placeholder = placeholder,
            error = placeholder,
            fallback = placeholder,
            contentScale = ContentScale.FillBounds,
            modifier = Modifier.fillMaxSize()
        )

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .fillMaxHeight(0.4f)
                .background(
                    Brush.verticalGradient(
                        colors = listOf(
                            Color.Transparent,
                            TolkienBlack.copy(alpha = 0.7f),
                            TolkienBlack.copy(alpha = 0.8f),
                            TolkienBlack.copy(alpha = 0.9f)
                        )
                    )
                )
                .padding(12.dp),
            contentAlignment = Alignment.BottomCenter
        ) {
            Text(
                text = title.lowercase(),
                style = TextStyle(
                    fontFamily = RingBearer,
                    fontSize = 18.sp,
                    letterSpacing = 1.sp,
                    brush = shimmerBrush,
                    shadow = Shadow(
                        color = TolkienGold.copy(alpha = 0.7f),
                        offset = Offset.Zero,
                        blurRadius = 16f
                    )
                ),
                maxLines = 2,
                overflow = TextOverflow.Ellipsis,
                textAlign = TextAlign.Center
            )
        }
    }
}