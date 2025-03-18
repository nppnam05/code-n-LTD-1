package Nam_Database.DAO
import Nam_Database.Table.Review
import androidx.room.*

@Dao
interface ReviewDAO {
    @Insert
    suspend fun insertReview(review: Review)

    @Update
    suspend fun updateReview(review: Review)

    @Delete
    suspend fun deleteReview(review: Review)

    @Query("SELECT * FROM Reviews WHERE id = :reviewId")
    suspend fun getReviewById(reviewId: Int): Review?

    @Query("SELECT * FROM Reviews WHERE productId = :productId")
    suspend fun getReviewsByProductId(productId: Int): List<Review>

    @Query("SELECT * FROM Reviews WHERE userId = :userId")
    suspend fun getReviewsByUserId(userId: Int): List<Review>

    @Query("SELECT * FROM Reviews")
    suspend fun getAllReviews(): List<Review>
}