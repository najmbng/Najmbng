package com.example.ui.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.example.ui.theme.SleekBorder
import com.example.ui.theme.SleekSurface

@Composable
fun CardFrame(
    modifier: Modifier = Modifier,
    backgroundColor: Color = SleekSurface,
    borderColor: Color = SleekBorder,
    shape: Shape = RoundedCornerShape(20.dp),
    tonalElevation: Dp = 1.dp,
    shadowElevation: Dp = 2.dp,
    content: @Composable () -> Unit
) {
    Surface(
        modifier = modifier.fillMaxWidth(),
        color = backgroundColor,
        shape = shape,
        border = BorderStroke(1.dp, borderColor),
        tonalElevation = tonalElevation,
        shadowElevation = shadowElevation,
        content = content
    )
}
