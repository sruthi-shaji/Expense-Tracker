package com.example.midterm.Data

import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import com.example.midterm.Model.Expense

/*
 * Helper function to perform CRUD operations in SQLLite.
 */
class DBHelper(context: Context) : SQLiteOpenHelper(context, DB_name, null, 1) {
    companion object {
        const val DB_name = "Expense.db"
        const val TB_name = "Expenses"
        const val ID = "ID"
        const val USER = "username"
        const val TYPE = "type"
        const val NAME = "name"
        const val AMOUNT = "amount"
        const val DESCRIPTION = "description"
    }

    override fun onCreate(db: SQLiteDatabase?) {
        db?.execSQL("CREATE TABLE $TB_name ($ID INTEGER PRIMARY KEY AUTOINCREMENT, $USER TEXT, $TYPE TEXT, $NAME TEXT, $AMOUNT REAL,$DESCRIPTION TEXT)")
    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
        db?.execSQL("DROP TABLE IF EXISTS $TB_name")
        onCreate(db)
    }

    /*
     * Adds a new Expense
     * takes expense as argument
     */
    fun addExpense(expense : Expense){
        val db = this.writableDatabase
        val values = ContentValues().apply {
            put(TYPE, expense.type)
            put(USER,expense.username)
            put(NAME,expense.name)
            put(AMOUNT, expense.amount)
            put(DESCRIPTION, expense.description)
        }
        db.insert(TB_name, null, values)
    }

    /*
     * Deletes a specific Expense
     * takes expenseId as argument.
     */
    fun deleteExpense(id : Int): Int {
        val db = this.writableDatabase
        return db.delete(TB_name, "$ID = ?", arrayOf(id.toString()))
    }
    /*
     * Updates a specific Expense
     * takes expenseId and expense as arguments
     */
    fun updateExpense(id: Int, expense : Expense) : Int {
        val db = this.writableDatabase
        val contentValues = ContentValues()
        contentValues. put(TYPE, expense.type)
        contentValues.put(USER,expense.username)
        contentValues. put(NAME,expense.name)
        contentValues.put(AMOUNT, expense.amount)
        contentValues.put(DESCRIPTION, expense.description)
        return db.update(TB_name, contentValues, "$ID = ?", arrayOf(id.toString()))
    }
    /*
     * Get a specific Expense
     * takes expenseId as argument
     */
    fun getExpense(expenseId: Int): Expense? {
        val db = writableDatabase
        val selection = "$ID = ?"
        val selectionArgs = arrayOf(expenseId.toString())
        val cursor = db.query(TB_name, null, selection, selectionArgs, null, null, null)

        val expense: Expense? = if (cursor.moveToFirst()) {
            // Extract expense data from cursor
            val expenseId = cursor.getInt(cursor.getColumnIndex(ID) ?: -1)
            val username = cursor.getString(cursor.getColumnIndex(USER) ?: -1)
            val type = cursor.getString(cursor.getColumnIndex(TYPE) ?: -1)
            val name = cursor.getString(cursor.getColumnIndex(NAME) ?: -1)
            val amount = cursor.getFloat(cursor.getColumnIndex(AMOUNT) ?: -1)
            val description = cursor.getString(cursor.getColumnIndex(DESCRIPTION) ?: -1)
            Expense(expenseId,username, type,name, amount, description)
        } else {
            null
        }
        cursor.close()
        return expense
    }
    /*
     * Get expenses list of a specific user
     */
    fun getExpenses(userName : String) : Cursor {
        val db = writableDatabase
        val selection = "$USER = ?"
        val selectionArgs = arrayOf(userName)
        return db.query(TB_name, null, selection, selectionArgs, null, null, null)
    }
}