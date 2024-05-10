package com.example.midterm

import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.midterm.Adapters.ExpenseAdaptor
import com.example.midterm.Data.DBHelper
import com.example.midterm.Model.Expense
import com.example.midterm.Model.User
import com.google.gson.Gson

class ExpenseViewActivity : AppCompatActivity() {

    private lateinit var dbHelper: DBHelper
    private lateinit var username: String

    /*
     * initializes dbHelper and username
     * modifies the welcome text on the top tab
     * sets up the logout button and add new expense button
     * loads users' expenses inside the recyclerview
     */
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_expense_view)

        this.dbHelper = DBHelper(this)
        this.username = getIntent().getStringExtra("username") ?: ""

        //modifies the welcome text on the top tab
        val sharedPreferences : SharedPreferences = getSharedPreferences("Users", MODE_PRIVATE)
        val user : User = Gson().fromJson(sharedPreferences.getString(username, null), User::class.java)
        findViewById<TextView>(R.id.welcometext).text = "Welcome ${user.profileName.toString()} !"

        //sets up the logout button
        val logout = findViewById<TextView>(R.id.logout)
        logout.setOnClickListener {
            finish()
        }

        //sets up add new expense button
        val newExpense = findViewById<TextView>(R.id.addexpense)
        newExpense.setOnClickListener{
            val newintent = Intent(this, AddUpdateExpenseActivity::class.java)
            newintent.putExtra("username", username)
            startActivity(newintent)
        }

        //loads users' expenses inside the recyclerview
        val recyclerView = findViewById<RecyclerView>(R.id.recyclerview)
        recyclerView.layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL,false)
        //create expenselist of user using SQLLite
        val expenseList : MutableList<Expense> = loadData(username)
        val adapter = ExpenseAdaptor(expenseList,username, this)
        recyclerView.adapter = adapter
    }

    /*
     * loads users' expenses inside the recyclerview for each page load
     */
    override fun onResume() {
        super.onResume()

        val recyclerView = findViewById<RecyclerView>(R.id.recyclerview)
        recyclerView.layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL,false)
        //create expenselist of user using SQLLite
        val expenseList : MutableList<Expense> = loadData(this.username)
        val adapter = ExpenseAdaptor(expenseList,this.username, this)
        recyclerView.adapter = adapter
    }

   /*
    * loads expense data from SQLLite using dbHelper class
    * returns list of expenses.
    */
    private fun loadData(username : String) : MutableList<Expense> {
        val cursor = dbHelper.getExpenses(username)
        val expenseList = mutableListOf<Expense>()

        while (cursor.moveToNext()) {
            val id = cursor.getInt(cursor.getColumnIndexOrThrow(DBHelper.ID))
            val type = cursor.getString(cursor.getColumnIndexOrThrow(DBHelper.TYPE))
            val name = cursor.getString(cursor.getColumnIndexOrThrow(DBHelper.NAME))
            val amount = cursor.getFloat(cursor.getColumnIndexOrThrow(DBHelper.AMOUNT))
            val description = cursor.getString(cursor.getColumnIndexOrThrow(DBHelper.DESCRIPTION))

            expenseList.add(Expense(id, username, type, name, amount, description))
        }
        cursor.close()
        return expenseList
    }

}