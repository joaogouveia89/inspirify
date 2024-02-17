package io.github.joaogouveia89.inspirify.data.local.daos

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import io.github.joaogouveia89.inspirify.data.local.FETCH_QUOTE_LOCALLY
import io.github.joaogouveia89.inspirify.data.local.GET_FAVORITES_ALL
import io.github.joaogouveia89.inspirify.data.local.entities.Favorite

@Dao
interface FavoriteDao {
    @Query(GET_FAVORITES_ALL)
    suspend fun getAll(): List<Favorite>

    @Query(FETCH_QUOTE_LOCALLY)
    suspend fun getQuoteLocally(quote: String, author: String): List<Favorite>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun addToFavorites(favorite: Favorite): Long

    @Delete
    suspend fun deleteFromFavorites(favorite: Favorite): Int
}