package com.moragar.catbreeds.core.presentation.uicomponents

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import catbreeds.composeapp.generated.resources.Res
import catbreeds.composeapp.generated.resources.error_no_internet
import com.moragar.catbreeds.core.presentation.styles.ErrorRed
import org.jetbrains.compose.resources.stringResource

@Composable
fun OfflineBanner(
    showWarning: Boolean
) {
    AnimatedVisibility(
        visible = showWarning,
        enter = slideInVertically(initialOffsetY = { -it }) + fadeIn(),
        exit = slideOutVertically(targetOffsetY = { -it }) + fadeOut()
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .background(ErrorRed)
                .padding(12.dp),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = stringResource( Res.string.error_no_internet),
                color = Color.White,
                style = MaterialTheme.typography.bodyMedium
            )
        }
    }
}