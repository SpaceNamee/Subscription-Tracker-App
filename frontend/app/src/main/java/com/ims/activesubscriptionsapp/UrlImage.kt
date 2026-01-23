package com.example.activesubscriptionsapp

import androidx.compose.foundation.Image
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import coil.compose.rememberAsyncImagePainter

@Composable
fun UrlImage(
    url: String,
    modifier: Modifier = Modifier
) {
    Image(
        painter = rememberAsyncImagePainter(url),
        contentDescription = null,
        modifier = modifier,
        contentScale = ContentScale.Crop
    )
}


