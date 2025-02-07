package com.tzh.sneakerland.domain.repository

import coil.size.Size
import com.google.android.gms.tasks.Task
import com.google.firebase.firestore.DocumentReference
import com.tzh.sneakerland.data.model.SneakerModel
import kotlinx.coroutines.flow.StateFlow

interface EcommerceRepository {

    val sneakerList: StateFlow<Result<List<SneakerModel>>>

    suspend fun updateFavourite(sneakerModel: SneakerModel): Result<Unit>

    suspend fun getSneakerById(
        id: Int,
        onSuccess: (SneakerModel) -> Unit,
        onFailure: (String) -> Unit
    )

    suspend fun addToCart(
        sneakerModel: SneakerModel,
        color: String, size: Double,
        qty: Int
    ): Result<Task<DocumentReference>>
}