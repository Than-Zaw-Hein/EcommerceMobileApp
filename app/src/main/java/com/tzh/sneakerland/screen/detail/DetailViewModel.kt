package com.tzh.sneakerland.screen.detail

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.tzh.sneakerland.data.model.SneakerModel
import com.tzh.sneakerland.domain.repository.EcommerceRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

@HiltViewModel
class DetailViewModel @Inject constructor(
    private val ecommerceRepository: EcommerceRepository,
) : ViewModel() {

    private val _sneaker =
        MutableStateFlow<SneakerModel?>(null)
    val currentSneaker = _sneaker.asStateFlow()

    fun setSneaker(sneakerModel: SneakerModel) {
        _sneaker.value = sneakerModel
        getSneakerById(sneakerModel.id)
    }

    fun getSneakerById(id: Int) {
        viewModelScope.launch {
            ecommerceRepository.getSneakerById(
                id,
                onSuccess = {
                    _sneaker.value = it
                },
                onFailure = {
                    Log.e("FAIL", it.toString())
                }
            )
        }
    }

    fun addCart(
        onLoading: (Boolean) -> Unit,
        onShowMessage: (String) -> Unit,
        onSuccessMessage: (String) -> Unit
    ) {

        val sneakerModel = _sneaker.value

        if (sneakerModel?.selectedColor == null) {
            onShowMessage("Please select color.")
            return
        }

        if (sneakerModel.selectedSize == null) {
            onShowMessage("Please select size.")
            return
        }

        if (sneakerModel.currentQty <= 0) {
            onShowMessage("Quantity at least 1.")
        }

        onLoading(true)
        viewModelScope.launch {
            ecommerceRepository.addToCart(
                sneakerModel,
                color = sneakerModel.selectedColor!!,
                qty = sneakerModel.currentQty,
                size = sneakerModel.selectedSize!!
            ).onSuccess {
                it.addOnSuccessListener {
                    onSuccessMessage("Successfully added")
                }.addOnFailureListener { e ->
                    onShowMessage(e.toString())
                }.await()
                onLoading(false)
            }.onFailure { e ->
                onShowMessage(e.message.toString())
                onLoading(false)
            }
        }
    }

    fun updateFavourite(sneakerModel: SneakerModel) {
        viewModelScope.launch {
            ecommerceRepository.updateFavourite(sneakerModel)
        }
    }
}
//
//@AssistedFactory
//interface DetailViewModelFactory {
//    fun create(sneakerModel: SneakerModel): DetailViewModel
//}