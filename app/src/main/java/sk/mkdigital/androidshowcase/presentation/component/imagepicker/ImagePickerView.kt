package sk.mkdigital.androidshowcase.presentation.component.imagepicker

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberUpdatedState
import sk.mkdigital.androidshowcase.presentation.component.camera.rememberCameraManager
import sk.mkdigital.androidshowcase.presentation.component.galery.rememberGalleryManager
import sk.mkdigital.androidshowcase.presentation.component.permission.PermissionType
import sk.mkdigital.androidshowcase.presentation.component.permission.PermissionView

@Composable
fun ImagePickerView(
    state: ImagePickerState,
    onDialogDismiss: () -> Unit,
    onActionSelect: (PickerAction) -> Unit,
    onActionReset: () -> Unit,
    onImagePick: (ImageResult?) -> Unit,
) {
    val cameraManager = rememberCameraManager(onImagePick)
    val galleryManager = rememberGalleryManager(onImagePick)
    val currentActionReset by rememberUpdatedState(onActionReset)

    if (state.showOptionDialog) {
        ImageSourceOptionDialog(
            onDismissRequest = onDialogDismiss,
            onAction = onActionSelect,
        )
    }

    when (state.action) {
        PickerAction.Camera -> PermissionView(
            permission = PermissionType.CAMERA,
            onDeniedDialogDismiss = onActionReset,
        ) {
            cameraManager.launch()
            onActionReset()
        }

        PickerAction.Gallery -> {
            LaunchedEffect(state.action) {
                galleryManager.launch()
                currentActionReset()
            }
        }

        PickerAction.None -> Unit
    }
}
