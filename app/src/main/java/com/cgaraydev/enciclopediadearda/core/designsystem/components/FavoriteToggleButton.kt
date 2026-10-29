package com.cgaraydev.enciclopediadearda.core.designsystem.components

import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.spring
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.material3.IconToggleButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.unit.dp
import com.cgaraydev.enciclopediadearda.R
import com.cgaraydev.enciclopediadearda.ui.theme.TolkienGolden07

@Composable
fun FavoriteToggleButton(
    isFavorite: Boolean,
    enabled: Boolean,
    onFavoriteClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val description = stringResource(
        if (isFavorite) {
            R.string.remove_from_favorites
        } else {
            R.string.add_to_favorites
        }
    )

    val scale by animateFloatAsState(
        targetValue = if (isFavorite) 1.3f else 1f,
        animationSpec = spring(
            dampingRatio = Spring.DampingRatioMediumBouncy,
            stiffness = Spring.StiffnessLow
        ),
        label = "favoriteSpringScale"
    )

    Box(
        modifier = modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp),
        contentAlignment = Alignment.Center
    ) {
        IconToggleButton(
            checked = isFavorite,
            onCheckedChange = { onFavoriteClick() },
            enabled = enabled,
            modifier = Modifier
                .size(48.dp)
                .semantics { contentDescription = description }
        ) {
            Icon(
                painter = painterResource(
                    if (isFavorite) {
                        R.drawable.ic_favorite_filled
                    } else {
                        R.drawable.ic_favorite_border
                    }
                ),
                contentDescription = null,
                tint = TolkienGolden07,
                modifier = Modifier
                    .size(24.dp)
                    .graphicsLayer {
                        scaleX = scale
                        scaleY = scale
                    }
            )
        }
    }
}