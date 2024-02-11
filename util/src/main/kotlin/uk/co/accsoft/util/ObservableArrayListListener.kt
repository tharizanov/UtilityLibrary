package uk.co.accsoft.util

import androidx.annotation.CallSuper
import androidx.databinding.ObservableArrayList
import androidx.databinding.ObservableList

@Suppress("unused")
abstract class ObservableArrayListListener<T : Any?> : ObservableList.OnListChangedCallback<ObservableArrayList<T>>() {

    override fun onChanged(list: ObservableArrayList<T>?) {}

    /**
     * Called whenever an item has been inserted into the list.
     * @param list The changing list.
     * @param position The insertion index.
     */
    open fun onItemInserted(list: ObservableArrayList<T>, position: Int) {}

    override fun onItemRangeChanged(list: ObservableArrayList<T>?, positionStart: Int, itemCount: Int) {}

    @CallSuper
    override fun onItemRangeInserted(list: ObservableArrayList<T>?, positionStart: Int, itemCount: Int) {
        if (list != null)
            for (i in 0 until itemCount)
                onItemInserted(list, positionStart + i)
    }

    override fun onItemRangeMoved(list: ObservableArrayList<T>?, fromPosition: Int, toPosition: Int, itemCount: Int) {}

    override fun onItemRangeRemoved(list: ObservableArrayList<T>?, positionStart: Int, itemCount: Int) {}

}