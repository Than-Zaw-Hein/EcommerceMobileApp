package com.tzh.sneakerland.data.repository

import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.SetOptions
import com.google.firebase.firestore.toObject
import com.tzh.sneakerland.data.model.SneakerModel
import com.tzh.sneakerland.data.model.dummySneakerList
import com.tzh.sneakerland.domain.repository.EcommerceRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class EcommerceRepositoryImpl @Inject constructor(
    private val firestore: FirebaseFirestore,
) : EcommerceRepository {
    private val sneakersCollection = firestore.collection("sneakers")

    // Create a MutableStateFlow to hold the sneaker list state
    private val _sneakerList =
        MutableStateFlow<Result<List<SneakerModel>>>(Result.success(emptyList()))
    override val sneakerList: StateFlow<Result<List<SneakerModel>>> get() = _sneakerList

    init {
        fetchSneakerList() // Load data initially and listen for real-time updates
    }

    private fun fetchSneakerList() {
        sneakersCollection.addSnapshotListener { snapshot, e ->
            if (e != null) {
                _sneakerList.value = Result.failure(e)
                return@addSnapshotListener
            }
            if (snapshot != null) {
                val sneakers = snapshot.documents.mapNotNull { it.toObject<SneakerModel>() }
                _sneakerList.value = Result.success(sneakers)
            } else {
                _sneakerList.value = Result.success(emptyList())
            }
        }
    }

    override suspend fun updateFavourite(sneakerModel: SneakerModel): Result<Unit> {
        return updateSneaker(sneakerModel)
    }

    suspend fun updateSneaker(sneakerModel: SneakerModel): Result<Unit> {
        return try {
            sneakersCollection
                .document(sneakerModel.id.toString())
                .set(
                    sneakerModel,
                    SetOptions.merge()
                )  // This will merge fields with the existing document
                .await()
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun getSneakerById(
        id: Int,
        onSuccess: (SneakerModel) -> Unit,
        onFailure: (String) -> Unit
    ) {

        val docRef = sneakersCollection.document(id.toString())

        docRef.addSnapshotListener { documentSnapshot, e ->
            if (e != null) {
                onFailure(e.toString())
                return@addSnapshotListener
            }

            if (documentSnapshot != null && documentSnapshot.exists()) {
                val sneakerModel = documentSnapshot.toObject<SneakerModel>()
                if (sneakerModel != null) {
                    onSuccess(sneakerModel)
                }
            } else {
                onFailure("Not Found") // or Result.failure(Throwable("Sneaker not found"))
            }
        }
    }

    override suspend fun addToCart(
        sneakerModel: SneakerModel,
        color: String,
        size: Double,
        qty: Int
    ): Result<Unit> {
        return try {
            val cartCollection = firestore
                .collection("cart")

            // Create a map to represent the cart item
            val cartItem = hashMapOf(
                "sneakerId" to sneakerModel.id,
                "name" to sneakerModel.name,
                "brand" to sneakerModel.brand,
                "gender" to sneakerModel.gender.toString(), // Convert Enum to String
                "description" to sneakerModel.description,
                "image" to sneakerModel.image,
                "price" to sneakerModel.price,
                "rating" to sneakerModel.rating,
                "isFavourite" to sneakerModel.isFavourite,
                "color" to color,
                "size" to size,
                "qty" to qty
            )

            // Add the cart item to the collection
            cartCollection.add(cartItem).await()

            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override var detailSneaker: SneakerModel? = null
}