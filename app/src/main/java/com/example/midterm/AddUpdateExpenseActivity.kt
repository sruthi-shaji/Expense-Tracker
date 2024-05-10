package com.example.midterm

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import com.example.midterm.Data.DBHelper
import com.example.midterm.Model.Expense

class AddUpdateExpenseActivity : AppCompatActivity() {
    var isIncome : Boolean = true
    var isEditmode : Boolean = false
    private lateinit var dbHelper: DBHelper

    private lateinit var title : EditText
    private lateinit var amt : EditText
    private lateinit var description : EditText
    private lateinit var incometab : TextView
    private lateinit var expensetab : TextView

    /*
     * Initialized dbHelper, username, and all the fields inside View
     * check if expenseId is present -> if present , isEditMode = true and Loads the expense details
     * handles income and expense toggling action
     * set up done and delete button click actions after validation
     */
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_update_expense)
        dbHelper = DBHelper(this)
        this.title = findViewById(R.id.expensetitle)
        this.amt = findViewById(R.id.amt)
        this.description = findViewById(R.id.description)
        this.incometab = findViewById(R.id.income)
        this.expensetab = findViewById(R.id.expense)

        val username = getIntent().getStringExtra("username") ?: ""
        val expenseId = getIntent().getIntExtra("expenseId", -1)
        if(expenseId != -1) {
            isEditmode = true
            loadExpense(expenseId)
        }

        incometab.setOnClickListener {
            incometab.setBackgroundColor(resources.getColor(R.color.white))
            expensetab.setBackgroundColor(resources.getColor(android.R.color.transparent))
            isIncome = true
        }
        expensetab.setOnClickListener {
            expensetab.setBackgroundColor(resources.getColor(R.color.white))
            incometab.setBackgroundColor(resources.getColor(android.R.color.transparent))
            isIncome = false
        }

        /*
         * handles both Edit mode and Add new mode cases.
         * Validation performed
         */
        val done = findViewById<Button>(R.id.done)
        done.setOnClickListener {
            val expType = if (isIncome) "income" else "expense"
            var amount: Float = if(amt.text.isNullOrBlank()) 0F else amt.text.toString().toFloat()
            val exp = Expense(expenseId,username,expType,title.text.toString(), amount, description.text.toString())
            if(isEditmode) {
                if(exp.isValid()) {
                    dbHelper.updateExpense(expenseId, exp)
                    Toast.makeText(this, "Expense Updated!", Toast.LENGTH_SHORT).show()
                    finish()
                }
                else {
                    Toast.makeText(this, "Invalid Values Added!", Toast.LENGTH_SHORT).show()
                }
            }
            else {
                if(exp.isValid()) {
                    dbHelper.addExpense(exp)
                    Toast.makeText(this, "Expense Added!", Toast.LENGTH_SHORT).show()
                    finish()
                }
                else {
                    Toast.makeText(this, "Invalid Values Added!", Toast.LENGTH_SHORT).show()
                }
            }
        }
        val delete = findViewById<Button>(R.id.delete)
        delete.setOnClickListener{
            if(isEditmode)
            {
                dbHelper.deleteExpense(expenseId)
                Toast.makeText(this, "Expense Deleted!", Toast.LENGTH_SHORT).show()
            }
            finish()
        }
    }

    /*
     * Loads the current expense inside the page.
     * used in Edit mode
     */
    private fun loadExpense(id: Int) {
        var expense : Expense? = dbHelper.getExpense(id)
        if(expense != null) {
            val type: String
            if(expense.type.equals("expense")) {
                expensetab.setBackgroundColor(resources.getColor(R.color.white))
                incometab.setBackgroundColor(resources.getColor(android.R.color.transparent))
            }
            else if(expense.type.equals("expense")) {
                incometab.setBackgroundColor(resources.getColor(R.color.white))
                expensetab.setBackgroundColor(resources.getColor(android.R.color.transparent))
            }
            title.setText(expense.name)
            amt.setText(expense.amount.toString())
            description.setText(expense.description)
        }
    }
}