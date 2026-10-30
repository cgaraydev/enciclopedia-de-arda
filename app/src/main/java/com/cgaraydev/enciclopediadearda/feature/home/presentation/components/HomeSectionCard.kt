package com.cgaraydev.enciclopediadearda.feature.home.presentation.components

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Shadow
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.cgaraydev.enciclopediadearda.R
import com.cgaraydev.enciclopediadearda.core.designsystem.components.tolkienShimmerBrush
import com.cgaraydev.enciclopediadearda.ui.theme.RingBearer
import com.cgaraydev.enciclopediadearda.ui.theme.TolkienBlack
import com.cgaraydev.enciclopediadearda.ui.theme.TolkienGold

@Composable
fun HomeSectionCard(
    @StringRes titleRes: Int,
    @DrawableRes imageRes: Int,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val title = stringResource(titleRes)
    val navigationLabel = stringResource(
        R.string.navigate_to_section,
        title
    )
    val shimmerBrush = tolkienShimmerBrush(duration = 2000)

    Box(
        modifier = modifier
            .fillMaxWidth()
            .height(80.dp)
            .clickable(
                role = Role.Button,
                onClickLabel = navigationLabel,
                onClick = onClick
            ),
        contentAlignment = Alignment.Center
    ) {
        Image(
            painter = painterResource(imageRes),
            contentDescription = null,
            contentScale = ContentScale.FillBounds,
            modifier = Modifier.fillMaxSize()
        )

        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(TolkienBlack.copy(alpha = 0.5f))
        )

        Text(
            text = title,
            style = TextStyle(
                fontFamily = RingBearer,
                fontSize = 20.sp,
                fontWeight = FontWeight.Medium,
                brush = shimmerBrush,
                shadow = Shadow(
                    color = TolkienGold.copy(alpha = 0.7f),
                    offset = Offset.Zero,
                    blurRadius = 16f
                )
            )
        )
    }
}