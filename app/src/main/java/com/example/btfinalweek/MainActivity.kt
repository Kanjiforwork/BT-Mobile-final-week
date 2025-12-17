package com.example.btfinalweek

import android.os.Bundle
import android.util.Log
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.WindowInsetsCompat
import com.example.btfinalweek.databinding.ActivityMainBinding
import com.example.btfinalweek.databinding.DialogAddExpenseBinding
import com.example.btfinalweek.model.ExpenseModel
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private lateinit var dbRef: DatabaseReference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        initFirebaseDatabase()

        findViewById<Button>(R.id.btnLoad).setOnClickListener {
            loadFirebase()
        }

        findViewById<Button>(R.id.btnInsert).setOnClickListener {
            showDialogAddExpense()
        }
    }

    private fun initFirebaseDatabase() {
        //Tạo table expenses trong Firebase
        dbRef = FirebaseDatabase.getInstance().getReference("expenses")
        //dbRef.keepSynced(true) //Lưu dữ liệu ngay khi có thay đổi
    }

    private fun loadFirebase() {
        Toast.makeText(this, "Loading from Firebase...", Toast.LENGTH_SHORT).show()
        
        val expenses = mutableListOf<ExpenseModel>()
        val listExpense = mutableListOf<String>()
        val lvExpenses = binding.lvExpenses

        dbRef.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                expenses.clear()
                listExpense.clear()
                
                if (dataSnapshot.exists()) {
                    var dem = 0
                    for (expenseSnapshot in dataSnapshot.children) {
                        dem++
                        expenseSnapshot.getValue(ExpenseModel::class.java)?.let {expense ->
                            expenses.add(expense)
                            listExpense.add("${expense.category}: ${expense.title} - ${expense.amount} on ${expense.date}")
                        }
                    }
                    
                    val adapter = ArrayAdapter(this@MainActivity, android.R.layout.simple_list_item_1, listExpense)
                    lvExpenses.adapter = adapter
                    
                    Toast.makeText(this@MainActivity, "✅ Loaded $dem expenses", Toast.LENGTH_LONG).show()
                } else {
                    // Không có dữ liệu
                    val adapter = ArrayAdapter(this@MainActivity, android.R.layout.simple_list_item_1, listOf("No expenses found. Click 'Add Expense' to start!"))
                    lvExpenses.adapter = adapter
                    
                    Toast.makeText(this@MainActivity, "⚠️ No data in Firebase. Add some expenses first!", Toast.LENGTH_LONG).show()
                }
            }

            override fun onCancelled(error: DatabaseError) {
                Toast.makeText(this@MainActivity, "❌ Error: ${error.message}", Toast.LENGTH_LONG).show()
            }
        })
    }

    private fun showDialogAddExpense() {
        val dialogBinding = DialogAddExpenseBinding.inflate(layoutInflater)
        val dialog = AlertDialog.Builder(this)
            .setTitle("Add Expense")
            .setView(dialogBinding.root)
            .setPositiveButton("Add") { _, _ ->
                val idItem = dbRef.push().key
                val title = dialogBinding.etTitle.text.toString()
                val amount = dialogBinding.etAmount.text.toString().toDoubleOrNull() ?: 0.0
                val category = dialogBinding.etCategory.text.toString()
                val date = SimpleDateFormat("dd MMM yyyy", Locale.getDefault()).format(Date())

                val expense = ExpenseModel(id = idItem, title = title, amount = amount, category = category, date = date.toString())

                saveExpenseToFirebase(expense)
            }
            .setNegativeButton("Cancel", null)
            .create()

        dialog.show()
    }

    private fun saveExpenseToFirebase(expense: ExpenseModel) {
        dbRef.child(expense.id?: "").setValue(expense)
            .addOnSuccessListener {
                Toast.makeText(this, "Expense added successfully", Toast.LENGTH_SHORT).show()
            }
            .addOnFailureListener {
                Toast.makeText(this, "Failed to add expense", Toast.LENGTH_SHORT).show()
            }
    }
}