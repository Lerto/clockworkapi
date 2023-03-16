package one.clockwork.vita.data.db

import android.content.Context
import androidx.room.*
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import one.clockwork.apimodule.models.Model
import java.lang.reflect.Type

@Entity
@TypeConverters(DataConverter::class)
data class Address(
    @PrimaryKey(autoGenerate = false) var id: Int,
    @ColumnInfo(name = "address") var address: ArrayList<Model.Address>?
) {
    constructor() : this(
        1,
        ArrayList<Model.Address>()
    )
}

@Dao
interface AddressDao {
    @Query("SELECT * FROM Address WHERE id = :id LIMIT 1")
    suspend fun getToken(id: Int = 1): Address

    @TypeConverters(DataConverter::class)
    @Query("UPDATE Address SET address = :address WHERE id = 1")
    suspend fun updateAddress(address: ArrayList<Model.Address>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun addAddress(vararg address: Address)

    @Query("DELETE FROM Address WHERE id = 1")
    suspend fun wipeUser()
}

@Database(entities = [Address::class], version = 9)
abstract class AddressDB : RoomDatabase() {

    abstract fun addressDao(): AddressDao

    companion object {

        private var INSTANCE: AddressDB? = null

        fun getInstance(context: Context): AddressDB? {
            if (INSTANCE == null) {
                synchronized(AddressDB::class) {
                    INSTANCE = Room.databaseBuilder(
                        context.applicationContext,
                        AddressDB::class.java, "address.db"
                    )
                        .fallbackToDestructiveMigration()
                        .build()
                }
            }
            return INSTANCE
        }

        fun destroyInstance() {
            INSTANCE = null
        }
    }
}


class DataConverter {
    @TypeConverter
    fun stringToAddress(data: String?): ArrayList<Model.Address?>? {
        val gson = Gson()
        if (data == null) {
            return ArrayList<Model.Address?>()
        }
        val listType: Type? = object : TypeToken<ArrayList<Model.Address?>?>() {}.type
        return gson.fromJson(data, listType)
    }

    @TypeConverter
    fun addressToString(myObjects: ArrayList<Model.Address?>?): String? {
        val gson = Gson()
        return gson.toJson(myObjects)
    }
}