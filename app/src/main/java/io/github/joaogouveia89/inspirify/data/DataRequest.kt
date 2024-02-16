package io.github.joaogouveia89.inspirify.data
@Suppress("ConvertObjectToDataObject")
/*
 * data object to OnProgress is not needed, the advantage of having a
 * hashCode(), equals() and toString() methods, seems to be useless to this
 * specific case
 */
sealed class DataRequest {
    object OnProgress : DataRequest()
    class Success<T>(val data: T) : DataRequest()
    class Failed(val errorMessage: String) : DataRequest()
}