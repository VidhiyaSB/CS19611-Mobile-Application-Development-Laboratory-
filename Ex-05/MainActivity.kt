package com.example.lab5sqlite

import android.content.ContentValues
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast

class MainActivity : AppCompatActivity() {

    private lateinit var dbHelper: SQLiteOpenHelper

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val e1 = findViewById<EditText>(R.id.rno)
        val e2 = findViewById<EditText>(R.id.name)
        val e3 = findViewById<EditText>(R.id.cgpa)
        val ins = findViewById<Button>(R.id.add)
        val disp=findViewById<Button>(R.id.view)
        val t=findViewById<TextView>(R.id.viewdb)
        val modify=findViewById<Button>(R.id.modify)
        val del = findViewById<Button>(R.id.del)
        val clr= findViewById<Button>(R.id.clr)

        dbHelper = object : SQLiteOpenHelper(this, "db1", null, 1) {
            override fun onCreate(db: SQLiteDatabase?) {
                db?.execSQL("CREATE TABLE IF NOT EXISTS stud(Rno INTEGER, name VARCHAR(20), cgpa INTEGER)")
            }

            override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
            }
        }

        ins.setOnClickListener {
            val db = dbHelper.writableDatabase
            val rno = e1.text.toString().toIntOrNull()
            val name = e2.text.toString()
            val cgpa = e3.text.toString().toIntOrNull()

            if (rno != null && cgpa != null) {
                insertData(db, rno, name, cgpa)
                Toast.makeText(this, "$name added to database", Toast.LENGTH_LONG).show()
                e1.text.clear()
                e2.text.clear()
                e3.text.clear()
            } else {
                Toast.makeText(this, "Invalid input", Toast.LENGTH_SHORT).show()
            }
        }

        disp.setOnClickListener{
            val db = dbHelper.readableDatabase
            val c: Cursor = db.rawQuery("SELECT * FROM stud", null)

            val stringBuilder = StringBuilder()
            while (c.moveToNext()) {
                val rno = c.getInt(c.getColumnIndex("Rno"))
                val name = c.getString(c.getColumnIndex("name"))
                val cgpa = c.getInt(c.getColumnIndex("cgpa"))

                stringBuilder.append("Rno: $rno, Name: $name, CGPA: $cgpa\n")
            }
            c.close()

            t.text = stringBuilder.toString()
        }

        modify.setOnClickListener{
            val db = dbHelper.writableDatabase
            val rno = Integer.parseInt(e1.text.toString())
            val name = e2.text.toString()
            val cgpa = Integer.parseInt(e3.text.toString())

            if (rno != null || cgpa!=null) {
                if (name.isNotEmpty()) {
                    updateData(db, rno, name,cgpa)
                    Toast.makeText(this, "Record with Rno $rno updated", Toast.LENGTH_LONG).show()
                    e1.text.clear()
                    e2.text.clear()
                    e3.text.clear()
                } else {
                    Toast.makeText(this, "Name cannot be empty", Toast.LENGTH_SHORT).show()
                }
            } else {
                Toast.makeText(this, "Invalid Rno", Toast.LENGTH_SHORT).show()
            }
        }

        del.setOnClickListener{
            val db = dbHelper.writableDatabase
            val rno = e1.text.toString().toIntOrNull()

            if (rno != null) {
                deleteData(db, rno)
                Toast.makeText(this, "Record with Rno $rno deleted", Toast.LENGTH_LONG).show()
                e1.text.clear()
                e2.text.clear()
                e3.text.clear()
            } else {
                Toast.makeText(this, "Invalid Rno", Toast.LENGTH_SHORT).show()
            }
        }
        clr.setOnClickListener{
            e1.text.clear()
            e2.text.clear()
            e3.text.clear()
            t.text=""
        }
    }

    private fun insertData(db: SQLiteDatabase, rno: Int, name: String, cgpa: Int) {
        db.execSQL("Insert into stud values ($rno,'$name',$cgpa)")
    }
    private  fun updateData(db: SQLiteDatabase, rno: Int, name: String, cgpa: Int){
        db.execSQL("Update stud set name='$name', cgpa=$cgpa where Rno=$rno")
    }
    private fun deleteData(db: SQLiteDatabase, rno: Int){
        db.execSQL("delete from stud where Rno=$rno")
    }
}
