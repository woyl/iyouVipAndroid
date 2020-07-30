package com.github.iielse.imageviewer

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

object ViewerActions {
    const val SET_CURRENT_ITEM = "setCurrentItem"
    const val DISMISS = "dismiss"
}

class ImageViewerActionViewModel : ViewModel() {

    val actionEvent = MutableLiveData<Pair<String, Any?>>()
    var dialogFragment : ImageViewerDialogFragment?= null

    fun setCurrentItem(pos: Int) = internalHandle(ViewerActions.SET_CURRENT_ITEM, pos)
    fun dismiss() = internalHandle(ViewerActions.DISMISS, null)
    fun setData(dialogFragment:ImageViewerDialogFragment){
        this.dialogFragment = dialogFragment;
    }

    private fun internalHandle(action: String, extra: Any?) {
        actionEvent.value = Pair(action, extra)
        dialogFragment?.dismiss()
        actionEvent.value = null
    }
}