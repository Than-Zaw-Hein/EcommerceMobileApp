package com.tzh.sneakerland.screen.home

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.gson.Gson
import com.tzh.sneakerland.data.model.SneakerModel
import com.tzh.sneakerland.domain.repository.EcommerceRepository
import com.tzh.sneakerland.util.FilterType
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class HomeViewModel @Inject constructor(
    private val ecommerceRepository: EcommerceRepository,
) : ViewModel() {

    val fetchSneakerState = ecommerceRepository.sneakerList

    private val _uiState = MutableStateFlow(HomeUiState())

    val uiState: StateFlow<HomeUiState> =
        combine(fetchSneakerState, _uiState) { sneakerListState, currentState ->
            // Combine the sneaker list state with the current UI state
            val sneakerList = sneakerListState.getOrNull() ?: emptyList()
            val filteredSneakerList = filterSneakers(
                sneakerList,
                query = currentState.query,
                currentState.selectedBrand,
                currentState.filterBy
            )
            currentState.copy(
                sneakerList = filteredSneakerList,
                brandList = sneakerList.map { it.brand }.distinct()
            )
        }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), HomeUiState())


    fun updateFavourite(sneakerModel: SneakerModel) {
        viewModelScope.launch {
            ecommerceRepository.updateFavourite(
                sneakerModel.copy(
                    isFavourite = !sneakerModel.isFavourite
                )
            )
        }
    }

    fun setSneaker(sneaker: SneakerModel) {
        ecommerceRepository.detailSneaker = sneaker
    }


    fun onEvent(event: HomeUIEvent) {
        when (event) {
            is HomeUIEvent.FilterByType -> _uiState.value = _uiState.value.copy(
                filterBy = event.type,
                sneakerList = filterSneakers(
                    query = _uiState.value.query,
                    sneakerList = _uiState.value.sneakerList,
                    selectedBrand = _uiState.value.selectedBrand,
                    filterBy = _uiState.value.filterBy
                )
            )

            is HomeUIEvent.SearchName -> _uiState.value = _uiState.value.copy(
                query = event.name,
                sneakerList = filterSneakers(
                    query = event.name,
                    sneakerList = _uiState.value.sneakerList,
                    selectedBrand = _uiState.value.selectedBrand,
                    filterBy = _uiState.value.filterBy
                )
            )

            is HomeUIEvent.FilterBrand -> _uiState.value = _uiState.value.copy(
                selectedBrand = event.brand,
                sneakerList = filterSneakers(
                    query = _uiState.value.query,
                    sneakerList = _uiState.value.sneakerList,
                    selectedBrand = event.brand,
                    filterBy = _uiState.value.filterBy
                )
            )
        }
    }

    private fun filterSneakers(
        sneakerList: List<SneakerModel>,
        query: String,
        selectedBrand: String,
        filterBy: FilterType
    ): List<SneakerModel> {
        return when (filterBy) {
            FilterType.PRICE -> {
                sneakerList.filter { sneaker ->
                    if (query.isEmpty()) true else sneaker.name.lowercase()
                        .contains(query.lowercase(), true)
                }.sortedBy { it.price }
                    .filter { sneaker ->
                        if (selectedBrand.isEmpty()) {
                            true
                        } else {
                            (sneaker.brand.lowercase()
                                .equals(selectedBrand.lowercase(), ignoreCase = true))
                        }
                    }
            }

            FilterType.NAME -> {
                sneakerList.filter { sneaker ->
                    if (query.isEmpty()) true else sneaker.name.lowercase()
                        .contains(query.lowercase(), true)
                }.sortedBy { it.name }

                    .filter { sneaker ->
                        if (selectedBrand.isEmpty()) {
                            true
                        } else {
                            (sneaker.brand.lowercase()
                                .equals(selectedBrand.lowercase(), ignoreCase = true))
                        }
                    }

            }
        }


    }
}


data class HomeUiState(
    val query: String = "",
    val sneakerList: List<SneakerModel> = emptyList(),
    val brandList: List<String> = emptyList(),
    val selectedBrand: String = "",
    val filterBy: FilterType = FilterType.NAME,
)

sealed class HomeUIEvent {
    data class FilterByType(val type: FilterType) : HomeUIEvent()
    data class SearchName(val name: String) : HomeUIEvent()
    data class FilterBrand(val brand: String) : HomeUIEvent()
}

