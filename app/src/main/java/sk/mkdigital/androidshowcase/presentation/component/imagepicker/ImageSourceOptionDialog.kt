package sk.mkdigital.androidshowcase.presentation.component.imagepicker

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Image
import androidx.compose.material.icons.outlined.PhotoCamera
import androidx.compose.material3.minimumInteractiveComponentSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.Role
import sk.mkdigital.androidshowcase.R
import sk.mkdigital.androidshowcase.presentation.component.AppAlertDialog
import sk.mkdigital.androidshowcase.presentation.component.image.AppIconPrimary
import sk.mkdigital.androidshowcase.presentation.component.spacers.RowSpacer.Spacer2
import sk.mkdigital.androidshowcase.presentation.component.text.bodyMedium.TextBodyMediumNeutral100
import sk.mkdigital.androidshowcase.presentation.foundation.space4

@Composable
fun ImageSourceOptionDialog(
    onDismissRequest: () -> Unit,
    onAction: (PickerAction) -> Unit,
    title: String = stringResource(R.string.imagepicker_title),
) {
    AppAlertDialog(
        onDismissRequest = onDismissRequest,
        title = title,
        content = {
            Column {
                OptionRow(Icons.Outlined.PhotoCamera, stringResource(R.string.imagepicker_camera)) {
                    onAction(PickerAction.Camera)
                }
                OptionRow(Icons.Outlined.Image, stringResource(R.string.imagepicker_gallery)) {
                    onAction(PickerAction.Gallery)
                }
            }
        }
    )
}

@Composable
private fun OptionRow(
    icon: ImageVector,
    label: String,
    onClick: () -> Unit,
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .minimumInteractiveComponentSize()
            .clickable(role = Role.Button, onClick = onClick)
            .padding(space4),
        verticalAlignment = Alignment.CenterVertically
    ) {
        AppIconPrimary(icon)
        Spacer2()
        TextBodyMediumNeutral100(text = label)
    }
}
