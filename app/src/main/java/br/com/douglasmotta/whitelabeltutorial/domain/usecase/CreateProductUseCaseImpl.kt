package br.com.douglasmotta.whitelabeltutorial.domain.usecase

import android.net.Uri
import br.com.douglasmotta.whitelabeltutorial.data.ProductRepository
import br.com.douglasmotta.whitelabeltutorial.domain.model.Product
import java.util.UUID

class CreateProductUseCaseImpl(
    private val uploadProductImageUseCase: UploadProductImageUseCase,
    private val productRepository: ProductRepository,
) : CreateProductUseCase {

    override suspend fun invoke(
        description: String,
        price: Double,
        imageUri: Uri
    ): Product {

        return try {
            val imageUrl = uploadProductImageUseCase(imageUri)
            val product = Product(
                id = UUID.randomUUID().toString(),
                description = description,
                price = price,
                imageUrl = imageUrl
            )
            productRepository.createProduct(product)

        }catch (e: Exception) {
            throw e
        }
    }
}