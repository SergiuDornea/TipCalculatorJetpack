package com.example.tipappjet.roundbuttons

import android.graphics.drawable.shapes.Shape
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CardElevation
import androidx.compose.material3.Icon
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.modifier.modifierLocalConsumer
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.example.tipappjet.R


@Composable
fun RoundButton(
        modifier: Modifier = Modifier,
        icon : ImageVector,
        onClick: () -> Unit,
        tint: Color,
        backgroundColor : Color


    ){
    val IconButtonSizeModifier = Modifier.size(40.dp)
    Card(
        modifier = Modifier
//             create an "animation" when clicked
            .clickable { onClick.invoke() }
            .then(IconButtonSizeModifier),
        shape = CircleShape,
        colors = CardDefaults.cardColors(containerColor = backgroundColor)


    ){
        Box(
            contentAlignment = Alignment.Center,
            modifier = Modifier.fillMaxSize()
        ) {
            Icon(
                imageVector = icon,
                contentDescription = "Plus or minus icon",
                tint = tint,
                modifier = Modifier
                    .background(backgroundColor)

            )
        }
    }
}

@Preview
@Composable
fun RoundButtonPreview(){
    RoundButton(
        icon = ImageVector.vectorResource(id = R.drawable.ic_dollar),
        tint = colorResource(id = R.color.navy_blue),
        backgroundColor = colorResource(id = R.color.peach),
        onClick = { /*TODO*/ },
        )
}