package com.cgaraydev.enciclopediadearda.core.designsystem.components

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.heading
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.cgaraydev.enciclopediadearda.ui.theme.TolkienBlack
import com.cgaraydev.enciclopediadearda.ui.theme.TolkienWhite

@Composable
fun ScreenHeader(
    @StringRes labelRes: Int,
    modifier: Modifier = Modifier,
    @DrawableRes imageRes: Int? = null,
    color: Color = TolkienWhite,
    height: Dp = 140.dp
) {
    Box(
        modifier = modifier
            .fillMaxWidth()
            .height(height)
            .background(
                if (imageRes == null) TolkienBlack else Color.Transparent
            )
    ) {
        imageRes?.let { resId ->
            Image(
                painter = painterResource(resId),
                contentDescription = null,
                contentScale = ContentScale.FillBounds,
                modifier = Modifier
                    .fillMaxSize()
                    .alpha(0.4f)
            )
        }

        Text(
            text = stringResource(labelRes).lowercase(),
            style = MaterialTheme.typography.displayMedium.copy(
                textAlign = TextAlign.Center
            ),
            modifier = Modifier
                .align(Alignment.Center)
                .padding(horizontal = 28.dp)
                .semantics { heading() },
            color = if (imageRes == null) color else TolkienWhite
        )
    }
}