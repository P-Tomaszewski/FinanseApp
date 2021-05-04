package pl.edu.pjatk.finanseapp.provider

import android.content.ContentProvider
import android.content.ContentUris
import android.content.ContentValues
import android.content.UriMatcher
import android.database.Cursor
import android.net.Uri
import androidx.sqlite.db.SupportSQLiteQueryBuilder
import pl.edu.pjatk.finanseapp.App
import pl.edu.pjatk.finanseapp.PaymentViewHolder
import pl.edu.pjatk.finanseapp.database.PaymentDatabase
import java.lang.IllegalArgumentException
import java.lang.RuntimeException

const val PROVIDER_AUTHORITY = "pl.edu.pjatk.finanseapp"

class contentProvider: ContentProvider() {


    private val sUriMather = UriMatcher(UriMatcher.NO_MATCH).apply {
        addURI(PROVIDER_AUTHORITY, "payment", 1)
        addURI(PROVIDER_AUTHORITY, "payment/#", 2)
    }

    override fun query(uri: Uri, projection: Array<String>?, selection: String?, selectionArgs: Array<String>?, sortOrder: String?): Cursor {
        val db = (context?.applicationContext as? App)?.db
                ?: throw RuntimeException("cannot get app context")

        return when(sUriMather.match(uri)){
            1-> db.query(
                    SupportSQLiteQueryBuilder.builder("payment")
                            .columns(projection)
                            .selection(selection, selectionArgs)
                            .orderBy(sortOrder)
                            .create()
            )
//            2 -> {
//                val id = ContentUris.parseId(uri)
//                db.payments.getPaymentById(id)
//            }
            else -> throw IllegalArgumentException()
        }
    }

    override fun getType(uri: Uri): String? = when (sUriMather.match(uri)) {
        1 -> "vnd.android.cursor.dir/vnd.${PROVIDER_AUTHORITY}.paymen"
        2 -> "vnd.android.cursor.item/vnd.${PROVIDER_AUTHORITY}.paymen"
        else -> throw IllegalArgumentException()
    }

    override fun onCreate(): Boolean {
        return true
    }

    override fun insert(uri: Uri, values: ContentValues?): Uri? {
        throw NotImplementedError()
    }

    override fun delete(uri: Uri, selection: String?, selectionArgs: Array<out String>?): Int {
        throw NotImplementedError()
    }

    override fun update(uri: Uri, values: ContentValues?, selection: String?, selectionArgs: Array<out String>?): Int {
        throw NotImplementedError()
    }
}