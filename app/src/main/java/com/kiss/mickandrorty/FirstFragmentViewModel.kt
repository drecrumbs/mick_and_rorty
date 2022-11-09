package com.kiss.mickandrorty

import android.util.Log
import androidx.annotation.MainThread
import androidx.collection.ArrayMap
import androidx.collection.arrayMapOf
import androidx.databinding.Bindable
import androidx.lifecycle.viewModelScope
import com.kiss.mickandrorty.com.kiss.mickandrorty.model.character.CharactersResponse
import com.kiss.mickandrorty.model.character.Character
import com.skydoves.bindables.BindingViewModel
import com.skydoves.bindables.asBindingProperty
import com.skydoves.bindables.bindingProperty
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.*

@OptIn(ExperimentalCoroutinesApi::class)
class FirstFragmentViewModel : BindingViewModel() {
    private val repo = Repository(client = AppHttpClient, ioDispatcher = Dispatchers.IO)

    @get:Bindable
    var isLoading: Boolean by bindingProperty(false)
        private set

    @get:Bindable
    var toastMessage: String? by bindingProperty(null)
        private set

    private val characterFetchingIndex: MutableStateFlow<Int> = MutableStateFlow(1)
    private val characterListFlow = characterFetchingIndex
        .flatMapLatest { page ->
            repo.fetchCharacters(
                page = page,
                onStart = { isLoading = true },
                onComplete = { isLoading = false },
                onError = { toastMessage = it }
            )
        }

    @get:Bindable
    val characterList: List<Character> by characterListFlow.asBindingProperty(
        viewModelScope,
        emptyList()
    )

    @MainThread
    fun fetchNextCharacterList() {
        if (!isLoading) {
            characterFetchingIndex.value++
        }
    }
}

interface RickAndMortyApi {
    suspend fun fetchCharacters(
        page: Int,
        onStart: () -> Unit,
        onComplete: () -> Unit,
        onError: (String?) -> Unit
    ): Flow<List<Character>>
}

class Repository(
    private val client: AppHttpClient = AppHttpClient,
    private val ioDispatcher: CoroutineDispatcher = Dispatchers.IO
) : RickAndMortyApi {

    companion object {
        private val pageMap: ArrayMap<Int, List<Character>> = arrayMapOf()
        private val fetchedCharacters = arrayListOf<Character>()
    }

    override suspend fun fetchCharacters(
        page: Int,
        onStart: () -> Unit,
        onComplete: () -> Unit,
        onError: (String?) -> Unit
    ): Flow<List<Character>> = flow {
        if (pageMap[page].isNullOrEmpty()) {
            client.safeGet<CharactersResponse>(
                urlString = "https://rickandmortyapi.com/api/character?page=$page",
                onSuccess = {
                    pageMap[page] = it.results
                    fetchedCharacters.addAll(it.results)
                    emit(fetchedCharacters)
                    Log.d(javaClass.simpleName, "Emitting new page - $page")
                    Log.d(javaClass.simpleName, "Page Map - ${pageMap.map { it.key }}")
                },
                onError = { onError(it) }
            )
        } else {
            emit(fetchedCharacters)
            Log.d(javaClass.simpleName, "Emitting cached page")
        }

    }
        .onStart { onStart() }
        .onCompletion { onComplete() }
        .flowOn(ioDispatcher)

}