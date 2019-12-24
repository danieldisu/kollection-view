package com.danieldisu.kollectionview.internal

import android.view.View
import androidx.recyclerview.widget.RecyclerView

/**
 * Implementation of the RecyclerView ViewHolder that holds a KollectionItemView. KollectionViewHolders won't be used for different
 * ViewTypes.
 * @param kollectionItemView the View that represents the ViewModel
 * @param viewType the type of the view that holds this ViewHolder
 */
class KollectionViewHolder<VM, IV : KollectionItemView<VM>>(
        private val kollectionItemView: IV,
        private val viewType: Int
) : RecyclerView.ViewHolder(kollectionItemView as View) {

    fun bind(viewModel: VM) {
        kollectionItemView.bind(viewModel)
    }
}

