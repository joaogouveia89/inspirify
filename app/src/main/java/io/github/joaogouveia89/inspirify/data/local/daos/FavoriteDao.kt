package io.github.joaogouveia89.inspirify.data.local.daos

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import io.github.joaogouveia89.inspirify.data.local.COUNT_REINCIDENCE_OF_QUOTES
import io.github.joaogouveia89.inspirify.data.local.GET_FAVORITES_ALL
import io.github.joaogouveia89.inspirify.data.local.entities.Favorite

@Dao
interface FavoriteDao {
    @Query(GET_FAVORITES_ALL)
    suspend fun getAll(): List<Favorite>

    @Query(COUNT_REINCIDENCE_OF_QUOTES)
    suspend fun countReincidenceOfQuotes(quote: String, author: String): Int

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun addToFavorites(favorite: Favorite): Long

    @Delete
    suspend fun deleteFromFavorites(favorite: Favorite)
}