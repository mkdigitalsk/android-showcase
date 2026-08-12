package sk.mkdigital.androidshowcase.presentation.component.ext

import androidx.compose.foundation.clickable
import androidx.compose.ui.Modifier

fun Modifier.noRippleClickable(onClick: () -> Unit): Modifier = clickable(
    interactionSource = null,
    indication = null,
    onClick = onClick
)
