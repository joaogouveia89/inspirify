package io.github.joaogouveia89.inspirify.data.local.daos

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import io.github.joaogouveia89.inspirify.data.local.GET_FAVORITES_ALL
import io.github.joaogouveia89.inspirify.data.local.entities.Favorite

@Dao
interface FavoriteDao {
    @Query(GET_FAVORITES_ALL)
    suspend fun getAll(): List<Favorite>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun addToFavorites(favorite: Favorite)

    @Delete
    suspend fun deleteFromFavorites(favorite: Favorite)
}