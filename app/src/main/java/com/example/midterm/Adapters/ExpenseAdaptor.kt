package com.example.midterm.Adapters

import android.app.Activity
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.midterm.AddUpdateExpenseActivity
import com.example.midterm.Model.Expense
import com.example.midterm.R

class ExpenseAdaptor(val expenseList : MutableList<Expense>, val username : String, val activity : Activity) : RecyclerView.Adapter<ExpenseAdaptor.ExpenseViewHolder>() {

    inner class ExpenseViewHolder(itemView: View): RecyclerView.ViewHolder(itemView){
        var expensename : TextView = itemView.findViewById(R.id.expensename)
        var type: TextView = itemView.findViewById(R.id.type)
        var amount : TextView = itemView.findViewById(R.id.amount)
        var view : TextView = itemView.findViewById(R.id.view)

        init {
            //sets up the edit button on the individual expense
            view.setOnClickListener(){
                val intent = Intent(activity, AddUpdateExpenseActivity::class.java)
                intent.putExtra("username", username)
                intent.putExtra("expenseId", expenseList[adapterPosition].ID)
                activity.startActivity(intent)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ExpenseViewHolder {
        val v = LayoutInflater.from(parent.context).inflate(R.layout.individual_expense_layout,parent, false)
        return ExpenseViewHolder(v)
    }

    override fun getItemCount(): Int {
        return expenseList.size
    }

    override fun onBindViewHolder(holder: ExpenseViewHolder, position: Int) {
        holder.expensename.setText(expenseList[position].name)
        holder.type.text = if(expenseList[position].type.equals("income")) "+" else "-"
        holder.amount.setText(expenseList[position].amount.toString())
    }
}