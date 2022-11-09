package com.kiss.mickandrorty.com.kiss.mickandrorty.binding

import android.util.Log
import androidx.databinding.BindingAdapter
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.kiss.mickandrorty.CharacterAdapter
import com.kiss.mickandrorty.FirstFragmentViewModel
import com.kiss.mickandrorty.model.character.Character
import com.skydoves.bindables.BindingListAdapter
import com.skydoves.whatif.whatIfNotNullAs

object RecyclerViewBinding {

    @JvmStatic
    @BindingAdapter("adapter")
    fun bindAdapter(view: RecyclerView, adapter: RecyclerView.Adapter<*>?) {
        view.adapter = adapter?.apply {
            stateRestorationPolicy = RecyclerView.Adapter.StateRestorationPolicy.PREVENT_WHEN_EMPTY
        }
    }

    @JvmStatic
    @BindingAdapter("submitList")
    fun bindSubmitList(view: RecyclerView, itemList: List<Any>?) {
        view.adapter.whatIfNotNullAs<BindingListAdapter<Any, *>> { adapter ->
            Log.d(javaClass.simpleName, "New list: ${itemList?.map { (it as? Character)?.name }}")
            adapter.submitList(itemList)
        }
    }

    @JvmStatic
    @BindingAdapter("paginatedCharacterList")
    fun paginationCharacterList(view: RecyclerView, viewModel: FirstFragmentViewModel) {
        RecyclerViewPaginator(
            recyclerView = view,
            isLoading = { viewModel.isLoading },
            loadMore = { viewModel.fetchNextCharacterList() },
            onLast = { false }
        )
    }
}

class RecyclerViewPaginator(
    val recyclerView: RecyclerView,
    val isLoading: () -> Boolean,
    val loadMore: (Int) -> Unit,
    val onLast: () -> Boolean = { true }
) : RecyclerView.OnScrollListener() {

    private val threshold = 20
    private var currentPage: Int = 1

    init {
        recyclerView.addOnScrollListener(this)
    }

    override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
        super.onScrolled(recyclerView, dx, dy)

        val layoutManager = recyclerView.layoutManager
        val visibleItemCount = layoutManager!!.childCount
        val totalItemCount = layoutManager.itemCount
        val firstVisibleItemPosition = when (layoutManager) {
            is LinearLayoutManager -> layoutManager.findLastVisibleItemPosition()
            is GridLayoutManager -> layoutManager.findLastVisibleItemPosition()
            else -> return
        }

        if (onLast() || isLoading()) return

        if ((visibleItemCount + firstVisibleItemPosition + threshold) >= totalItemCount) {
            loadMore(++currentPage)
        }
    }

    fun resetCurrentPage() {
        this.currentPage = 0
    }
}