package com.example.midmorningsqliteapp

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Message
import android.widget.Button
import android.widget.EditText
import androidx.appcompat.app.AlertDialog

class MainActivity : AppCompatActivity() {
    lateinit var mEdtName : EditText
    lateinit var mEdtEmail : EditText
    lateinit var mEdtId :EditText
    lateinit var btnSave : Button
    lateinit var btnDelete :Button
    lateinit var btnView : Button
    lateinit var db:SQLiteDatabase
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        mEdtName = findViewById(R.id.mEdtName)
        mEdtEmail = findViewById(R.id.mEdtEmail)
        mEdtId = findViewById(R.id.mEdtId)
        btnSave = findViewById(R.id.mBtnSave)
        btnView = findViewById(R.id.mBtnView)
        btnDelete = findViewById(R.id.mBtnDelete)
        //create a database called emobilisDB
        db = openOrCreateDatabase("emobilisDB", Context.MODE_PRIVATE, null)
        //Create a table called users in the database
        db.execSQL("CREATE TABLE IF NOT EXISTS users(jina VARCHAR, arafa VARCHAR, kitambulisho VARCHAR)")

        btnSave.setOnClickListener{
            //Recieve data from the user
            var name = mEdtName.text.toString().trim()
            var email = mEdtEmail.text.toString().trim()
            var idnumber = mEdtId.text.toString().trim()
            //check if the user is submitting empty folders
            if(name.isEmpty() || email.isEmpty() || idnumber.isEmpty()){
                //Display an empty message using defined message function
                message("EMPTY FIELDS!!", "Please fill out all inputs")
            }else{
                //Proceed to save
                db.execSQL("INSERT INTO users VALUES('"+name+"','"+idnumber+"', '"+email+"')")
                clear()
                message("SUCCESS!!!", "User saved successfully")

            }
        }
        btnView.setOnClickListener{
            //Use cursor top select all the records
            var cursor = db.rawQuery("SELECT * FROM users", null)
            //check if there is record found
            if (cursor.count == 0){
                message("NO RECORDS!!!", "Sorry there are no users found")
            }else{
                //use the string buffer to append all records to be displayed using a loop
                var buffer = StringBuffer()
                while (cursor.moveToNext()){
                    var retrievedName = cursor.getString(0)
                    var retrievedEmail = cursor.getString(1)
                    var retrievedIdNumber = cursor.getString(2)
                    buffer.append(retrievedName+"\n")
                    buffer.append(retrievedEmail+"\n")
                    buffer.append(retrievedIdNumber+"\n")
                }
                message("USERS", buffer.toString())

            }
         }
        btnDelete.setOnClickListener{
            val idNumber = mEdtId.text.toString().trim()
            if (idNumber.isEmpty()){
                message("EMPTY FIELD!!!", "Please fill in the id field")

            }else{
                var cursor = db.rawQuery("SELECT * FROM users WHERE kitambulisho='"+idNumber+"'", null)
                if (cursor.count == 0){
                    message("NO RECORD FOUND!!", "Sorry there is no user with provided id")

                }else{
                    db.execSQL("DELETE FROM users WHERE kitambulisho='"+idNumber+"'")
                }
            }
        }
    }
    fun message(title:String, message:String){
        var alertDialog = AlertDialog.Builder(this)
        alertDialog.setTitle(title)
        alertDialog.setMessage(message)
        alertDialog.setPositiveButton("Cancel", null)
        alertDialog.create().show()
    }
    fun clear(){
        mEdtName.setText("")
        mEdtEmail.setText("")
        mEdtId.setText("")

    }
}