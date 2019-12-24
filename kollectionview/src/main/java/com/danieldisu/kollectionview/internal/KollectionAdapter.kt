package com.danieldisu.kollectionview.internal

import android.annotation.SuppressLint
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import kotlin.reflect.KClass

/**
 * This Adapter extends from the Android Support implementation of ListAdapter, it will update only the elements that have change
 * using DiffUtils
 * @param viewFactory Function that receives the ViewType and should return a new View for that ViewType
 * @param viewTypeFactory Function that receives a ViewModel and should return the ViewType for that ViewModel
 * @param viewModelComparisonFunction (optional) Function that receives 2 ViewModels and should return true if those ViewModels are the same
 * VM, the default implementation will use the equals of the ViewModel, but you can pass a function that know if the item is the same by
 * comparing by some kind of id. This won't be usually required for smaller lists.
 */
internal class KollectionAdapter<VM : Any, IV : KollectionItemView<VM>>(
    private val viewFactory: (Int) -> IV,
    private val viewTypeFactory: (VM) -> Int,
    private val viewModelComparisonFunction: ((VM, VM) -> Boolean)? = null,
    diffInMainThread: Boolean
) : ListAdapter<VM, KollectionViewHolder<VM, IV>>(
        getDiffConfig<VM>(ViewModelItemCallback(viewModelComparisonFunction), diffInMainThread)
) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): KollectionViewHolder<VM, IV> =
            KollectionViewHolder(viewFactory(viewType), viewType)

    override fun onBindViewHolder(holder: KollectionViewHolder<VM, IV>, position: Int) =
            holder.bind(getItem(position))

    override fun getItemViewType(position: Int): Int =
            viewTypeFactory(getItem(position))

    override fun getItemId(position: Int): Long {
        return getItem(position).hashCode().toLong()
    }

    fun findItemPosition(viewModelClass: KClass<out VM>): Int {
        var indexOf = -1

        forEachItemIndexed { vm, i ->
            if (viewModelClass == vm.javaClass.kotlin) {
                indexOf = i
                return@forEachItemIndexed
            }
        }

        return indexOf
    }

    fun forEachItemIndexed(f: (VM, Int) -> Unit) {
        val lastItem = itemCount - 1
        for (i in 0..lastItem) {
            f(getItem(i), i)
        }
    }

    internal class ViewModelItemCallback<VM : Any>(
            private val viewModelComparisonFunction: ((VM, VM) -> Boolean)?
    ) : DiffUtil.ItemCallback<VM>() {
        override fun areItemsTheSame(oldItem: VM, newItem: VM): Boolean {
            return if (viewModelComparisonFunction != null) viewModelComparisonFunction.invoke(oldItem, newItem)
            else oldItem == newItem
        }

        @SuppressLint("DiffUtilEquals")
        override fun areContentsTheSame(oldItem: VM, newItem: VM): Boolean = oldItem == newItem
    }
}
