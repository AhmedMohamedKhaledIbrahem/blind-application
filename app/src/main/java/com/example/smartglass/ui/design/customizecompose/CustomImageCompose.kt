package com.example.smartglass.ui.design.customizecompose

import androidx.annotation.DrawableRes
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.composed
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.smartglass.theme.BlueGray
import com.example.smartglass.theme.LightBlueWhite
import com.example.smartglass.ui.design.getCustomSwitchCompose
import com.example.smartglass.ui.design.getCustomTextInstance

data class CustomImageCompose(
    private var modifierClipShapeRow: Int = 4,
    private var modifierHeightRow: Int = 40,
    private var modifierSizeImage: Int = 16,
    private var modifierWidthSpacerText: Int = 5,
    private var modifierWidthSpacerSwitch: Int = 5,
    private var backgroundModifierShape: Int = 4,
    private var colorDarkThem: Color = Color.Transparent,
    private var colorLightThem: Color = LightBlueWhite,
    private var fontSizeTextImage: Int = 10,
    private var fontWeightTextImage: FontWeight = FontWeight.Normal,
    private var horizontalArrangement: Arrangement.Horizontal = Arrangement.Start,
    private var contentDescriptions :String ="",

) {
    private val getCustomTextCompose: CustomTextCompose = getCustomTextInstance()
    private val switchCompose: CustomSwitchCompose = getCustomSwitchCompose()

    @Composable
    fun CustomizeImage(
        modifier: Modifier = Modifier,
        @DrawableRes icon: Int,
        text: String,
        onClick: () -> Unit,

        ) {
        Row(
            modifier = modifier
                .clip(RoundedCornerShape(modifierClipShapeRow.dp))
                .customizeImage()
                .clickable { onClick() }
                .height(modifierHeightRow.dp)
                .fillMaxWidth(),
            horizontalArrangement = horizontalArrangement,
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Image(
                painter = painterResource(id = icon),
                contentDescription = contentDescriptions,
                modifier = Modifier.size(modifierSizeImage.dp)
            )
            Spacer(modifier = Modifier.width(modifierWidthSpacerText.dp))
            getCustomTextCompose.copy(
                fontSize = fontSizeTextImage,
                fontWeight = fontWeightTextImage
            ).CustomizeTextImage(text = text)

        }

    }

    @Composable
    fun CustomizeImageWithoutOnClick(
        modifier: Modifier = Modifier,
        @DrawableRes icon: Int,
        text: String,
        onCheckedChange: ((Boolean) -> Unit)?
    ) {
        Row(
            modifier = modifier
                .clip(RoundedCornerShape(modifierClipShapeRow.dp))
                .CustomizeImageWithoutOnClick()
                .height(modifierHeightRow.dp)
                .fillMaxWidth(),
            horizontalArrangement = horizontalArrangement,
            verticalAlignment = Alignment.CenterVertically,

            ) {
            Image(
                painter = painterResource(id = icon),
                contentDescription = contentDescriptions,
                modifier = Modifier.size(modifierSizeImage.dp)
            )
            Spacer(modifier = Modifier.width(modifierWidthSpacerText.dp))
            getCustomTextCompose.copy(
                fontSize = fontSizeTextImage,
                fontWeight = fontWeightTextImage
            )
                .CustomizeTextImage(text = text)
            Spacer(modifier = Modifier.width(modifierWidthSpacerSwitch.dp))
            switchCompose.CustomizeSwitchImage(onCheckedChange = onCheckedChange)

        }

    }

    //@SuppressLint("ModifierFactoryUnreferencedReceiver")
    private fun Modifier.CustomizeImageWithoutOnClick(): Modifier = composed {
        this.then(
            if (isSystemInDarkTheme()) {
                background(colorDarkThem).border(
                    width = 1.dp,
                    color = BlueGray,
                    shape = RoundedCornerShape(backgroundModifierShape.dp)
                )
            } else {
                background(Color.White)
            }
        )
    }

    private fun Modifier.customizeImage(): Modifier = composed {
        this.then(
            if (isSystemInDarkTheme()) {
                background(colorDarkThem).border(
                    width = 1.dp,
                    color = BlueGray,
                    shape = RoundedCornerShape(backgroundModifierShape.dp)
                )
            } else {
                background(Color.White)
            }
        )
    }


}
