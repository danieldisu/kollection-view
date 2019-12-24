package com.danieldisu.kollectionview

import android.content.Context
import android.util.AttributeSet
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.LinearSmoothScroller
import androidx.recyclerview.widget.RecyclerView
import com.danieldisu.kollectionview.internal.KollectionAdapter
import com.danieldisu.kollectionview.internal.KollectionItemView
import kotlin.reflect.KClass

private const val DEFAULT_VIEW_TYPE = 0

/**
 * This View can be used to represent a List of elements of one or more types. It uses RecyclerView under the hood to lay out items.
 * Updates will use DiffUtils in order to make the least changes possible to the list to represent the new state.
 *
 * One of the biggest differences in usage from a typical RecyclerView is that you should always pass the whole list of elements that you
 * want to display, meaning that state should live in another piece of the app.
 */
class KollectionView<VM : Any, IV : KollectionItemView<VM>> @JvmOverloads constructor(
        context: Context,
        attrs: AttributeSet? = null,
        defStyleAttr: Int = 0
) : RecyclerView(context, attrs, defStyleAttr) {

    private var adapter: KollectionAdapter<VM, IV>? = null

    init {
        layoutManager = object : LinearLayoutManager(getContext()) {
            override fun generateDefaultLayoutParams() =
                    LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT)
        }
        disableAnimations()
    }

    /**
     * This method should be called before calling update.
     *
     * @param viewFactory Function that receives the ViewType and should return a new View for that ViewType
     * @param viewTypeFactory Function that receives a ViewModel and should return the ViewType for that ViewModel
     * @param viewModelComparisonFunction (optional) Function that receives 2 ViewModels and should return true if those ViewModels are the
     * same
     * @param diffInMainThread Enable this to run the diffing function in the main thread.
     */
    fun configure(
        viewFactory: (Int) -> IV,
        viewTypeFactory: (VM) -> Int = { DEFAULT_VIEW_TYPE },
        viewModelComparisonFunction: ((VM, VM) -> Boolean)? = null,
        diffInMainThread: Boolean = false
    ) {
        initAdapter(viewFactory, viewTypeFactory, viewModelComparisonFunction, diffInMainThread)
    }

    /**
     * The list of viewModels that you want to display, it won't add these to the current list but override the current list with this list.
     */
    fun update(viewModels: List<VM>) {
        checkAdapterInit()

        adapter?.submitList(viewModels)
    }

    /**
     * This will smoothly scroll the view to the first instance of the ViewModel class that you pass
     */
    fun smoothScrollTo(
            viewModelClass: KClass<out VM>,
            atStart: Boolean,
            padding: Int
    ) {
        val indexOfVM = adapter?.findItemPosition(viewModelClass) ?: 0
        smoothScrollTo(indexOfVM, atStart, padding)
    }

    /**
     * This will smoothly scroll the view to the position that you pass.
     * @param atStart true if you want to scroll to the top of the view
     * @param padding amout of pixels starting from the top of the item that you want to scroll
     */
    fun smoothScrollTo(
            position: Int,
            atStart: Boolean,
            padding: Int
    ) {
        val smoothScroller = smoothScroller(atStart, padding)
        smoothScroller.targetPosition = position
        layoutManager?.startSmoothScroll(smoothScroller)
    }

    private fun checkAdapterInit() {
        if (adapter == null) {
            throw IllegalStateException("You should call configure method before calling update in a KollectionView")
        }
    }

    private fun initAdapter(
        viewFactory: (Int) -> IV,
        viewTypeFactory: (VM) -> Int,
        viewModelComparisonFunction: ((VM, VM) -> Boolean)? = null,
        diffInMainThread: Boolean
    ) {
        adapter = KollectionAdapter(viewFactory, viewTypeFactory, viewModelComparisonFunction , diffInMainThread)
        setAdapter(adapter)
    }

    private fun smoothScroller(
            atStart: Boolean,
            padding: Int
    ): LinearSmoothScroller = object : LinearSmoothScroller(context) {
        override fun getVerticalSnapPreference(): Int {
            if (atStart) {
                return LinearSmoothScroller.SNAP_TO_START
            }
            return LinearSmoothScroller.SNAP_TO_END
        }

        override fun calculateDyToMakeVisible(view: View?, snapPreference: Int): Int {
            if (padding != 0) {
                return super.calculateDyToMakeVisible(view, snapPreference) + padding
            } else {
                return super.calculateDyToMakeVisible(view, snapPreference)
            }
        }
    }

    private fun disableAnimations() {
        itemAnimator = null
    }
}
