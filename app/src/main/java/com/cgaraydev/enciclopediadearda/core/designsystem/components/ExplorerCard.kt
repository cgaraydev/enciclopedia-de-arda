package com.cgaraydev.enciclopediadearda.core.designsystem.components

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.minimumInteractiveComponentSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawWithContent
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.BlendMode
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.graphics.Shadow
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.cgaraydev.enciclopediadearda.R
import com.cgaraydev.enciclopediadearda.ui.theme.TolkienBackground
import com.cgaraydev.enciclopediadearda.ui.theme.TolkienGold
import com.cgaraydev.enciclopediadearda.ui.theme.TolkienWhite

@Composable
private fun ExplorerCard(
    modifier: Modifier = Modifier,
    iconRes: Int,
    label: String,
    rotateIcon: Boolean = false,
    onClick: () -> Unit
) {
    var isAnimating by remember { mutableStateOf(false) }
    val rotation = remember { Animatable(0f) }
    val shimmerBrush = tolkienShimmerBrush(duration = 2000)

    LaunchedEffect(isAnimating) {
        if (isAnimating && rotateIcon) {
            rotation.animateTo(
                targetValue = 1440f,
                animationSpec = tween(durationMillis = 1000, easing = LinearEasing)
            )
            rotation.snapTo(0f)
            isAnimating = false
            onClick()
        } else if (isAnimating) {
            isAnimating = false
            onClick()
        }
    }

    Box(
        modifier = modifier
            .fillMaxWidth()
            .minimumInteractiveComponentSize()
            .padding(horizontal = 24.dp)
            .shadow(4.dp, RectangleShape)
            .drawWithContent {
                drawContent()
                drawRect(
                    brush = shimmerBrush,
                    style = Stroke(width = 2.dp.toPx())
                )
            }
            .background(TolkienBackground, RectangleShape)
            .clickable(
                role = Role.Button,
                onClick = { if (!isAnimating) isAnimating = true },
                onClickLabel = label
            )
            .padding(vertical = 12.dp)
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center,
            modifier = Modifier.fillMaxWidth()
        ) {
            Icon(
                painter = painterResource(iconRes),
                contentDescription = null,
                modifier = Modifier
                    .size(48.dp)
                    .graphicsLayer { if (rotateIcon) rotationZ = rotation.value }
                    .drawWithContent {
                        drawContent()
                        drawRect(brush = shimmerBrush, blendMode = BlendMode.Modulate)
                    },
                tint = TolkienWhite
            )
            Spacer(modifier = Modifier.width(20.dp))
            Text(
                text = label,
                style = TextStyle(
                    fontFamily = FontFamily(Font(R.font.ring_bearer_medium)),
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Medium,
                    brush = shimmerBrush,
                    shadow = Shadow(
                        color = TolkienGold.copy(alpha = 0.7f),
                        offset = Offset(0f, 0f),
                        blurRadius = 16f
                    )
                ),
                modifier = Modifier.padding(4.dp)
            )
        }
    }
}

@Composable
fun RandomExplorerCard(
    modifier: Modifier = Modifier,
    onClick: () -> Unit
) {
    ExplorerCard(
        modifier = modifier,
        iconRes = R.drawable.ic_ring,
        label = stringResource(R.string.random_article),
        rotateIcon = true,
        onClick = onClick
    )
}

@Composable
fun SearchExplorerCard(
    modifier: Modifier = Modifier,
    onClick: () -> Unit
) {
    ExplorerCard(
        modifier = modifier,
        iconRes = R.drawable.ic_search,
        label = stringResource(R.string.search_article),
        onClick = onClick
    )
}
