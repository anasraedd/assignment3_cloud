package com.example.mylibrary

import android.annotation.SuppressLint
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.mylibrary.MyNotification.Companion.TAG
import com.example.mylibrary.MyNotification.Companion.userToken
import com.example.mylibrary.adapter.AdapterBook
import com.example.mylibrary.model.Book
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.obtions_more_to_book.*
import java.text.SimpleDateFormat

class MainActivity : AppCompatActivity(), AdapterBook.onClick, AdapterBook.onClickOnMore {
    var data = ArrayList<Book>()
    val db = Firebase.database
    val mRef = db.reference
    var firestore: FirebaseFirestore? = null
    lateinit var myNotification: MyNotification
     var isFavorite = false
    private val sdf = SimpleDateFormat("yyyy.MM.dd.HH.mm.ss")
    var bookAdapter = AdapterBook(this,data, this, this)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        firestore = Firebase.firestore
        myNotification = MyNotification(this)
//        Firebase.messaging.unsubscribeFromTopic("book_library")
//
//        Firebase.messaging.subscribeToTopic("book_library")
//            .addOnCompleteListener {
//                if (it.isSuccessful) {
//                    Log.d(TAG, "user now subscribe the topic")
//                } else {
//                    Log.d(TAG, "not subscribe")
//                }
//            }
        rv_books.layoutManager = LinearLayoutManager(this)
        rv_books.adapter = bookAdapter
//        tv_title.setText("المكتبة")
//
//        data.add(Book("00", "anas", "sport", 4.2, "anas raed", "2022", false))
//
//        data.add(Book("11", "anas11", "sport", 4.2, "anas raed11", "2022", true))
//        data.add(Book("22", "anas22", "sport", 4.2, "anas raed22", "2022", true))
//        data.add(Book("33", "anas33", "sport", 4.2, "anas raed33", "2022", false))
//        data.add(Book("44", "anas44", "sport", 4.2, "anas raed44", "2022",true))
//        data.add(Book("55", "anas555", "sport", 4.2, "anas raed55", "2022", false))

        rv_books.layoutManager = LinearLayoutManager(this)
        rv_books.adapter = bookAdapter

       // getBooks()

        val readDate = Read()
        val myRef = mRef.child("Books")
        myRef.addValueEventListener(readDate)

        add_book.setOnClickListener {
var i = Intent(this, AddBookActivity::class.java)
            startActivity(i)
        }

//        if (checkGooglePlayServices()) {
//            //Get Token
//            FirebaseMessaging.getInstance().token
//                .addOnCompleteListener { task ->
//                    if (!task.isSuccessful) {
//                        Log.w(TAG, "Fetching FCM registration token failed", task.exception)
//                        return@addOnCompleteListener
//                    }
//
//                    val token = task.result
//                    Log.d(TAG, "Token $token")
//
//
//                }
//
//        } else {
//            Toast.makeText(this, "can't used notification from firebase", Toast.LENGTH_SHORT).show()
//        }



    }

    inner class Read : ValueEventListener {
        @SuppressLint("NotifyDataSetChanged")

        override fun onDataChange(snapshot: DataSnapshot) {
            //data.clear()

            var data2 = ArrayList<Book>()

            for (book in snapshot.children) {
                val myBook = Book(
                    book.key.toString(),
                    book.child("name").value.toString().trim(),
                    book.child("desciption").value.toString().trim(),
                    book.child("nameCategory").value.toString(),
                    book.child("rate").value.toString().toFloat(),
                    book.child("author").value.toString(),
                    book.child("yearRealese").value.toString(),
                    book.child("isFavorite").value as Boolean,
                    book.child("image").value.toString()

                )
                data2.add(myBook)
            }


            bookAdapter.book = data2
            bookAdapter.notifyDataSetChanged()
         //   binding.rcyBooks.adapter!!.notifyDataSetChanged()
        }

        override fun onCancelled(error: DatabaseError) {
            Log.d(TAG, "error with Exception : ${error.message}")
        }

    }

    /*
    fun getBooks(){

        db!!.collection("books").addSnapshotListener { value, error ->
            //Toast.makeText(this, "db:  ${value!!.documents.size}", Toast.LENGTH_LONG).show()
            //sizeDocument =  value!!.documents.size

            var data2 = ArrayList<Book>()

            for (document in value!!) {
                var id = document.id
               var name= document["name"].toString()
                var desciption= document["desciption"].toString()
               var categoryName= document["nameCategory"].toString()
                var rating= document["rate"]
               var  authorName= document["author"].toString()
               var yearRelease= document["yearRealese"]
                var image= document["image"].toString()

                if (document["isFavorite"] != null){
                     isFavorite= document["isFavorite"] as Boolean
                }

                val u = Book(id, name, desciption, categoryName,
                    rating, authorName, yearRelease,
                    isFavorite, image
                )


                data2.add(u)

            }

            bookAdapter.data = data2
            bookAdapter.notifyDataSetChanged()
        }

    }

    */


    override fun onclickItem(position: Int, id: String, name: String, isFavorite: Boolean) {
//       db!!.collection("books").whereEqualTo("id", id).get().addOnSuccessListener { snapShot ->
//           snapShot.documents[0][id]
//
//       }.addOnFailureListener { e ->
//       }

        /*
        firestore!!.collection("books").document("$id").update("isFavorite", isFavorite).addOnSuccessListener {
            Log.d("TAG", "DocumentSnapshot successfully updated!")
        }
            .addOnFailureListener { e ->
                Log.w("TAG", "Error updating document", e)
            }

        */



    }

    override fun onclickItemOnClickMore(position: Int, id: String, book:Book ,name: String) {
        val bottomSheetDialog = BottomSheetDialog(this)
        bottomSheetDialog.setContentView(R.layout.obtions_more_to_book)

        bottomSheetDialog.tv_name_book.text = name
        bottomSheetDialog.cancble.setOnClickListener {
            bottomSheetDialog.cancel()
        }

        bottomSheetDialog.btn_edit.setOnClickListener {
            var i = Intent(this, EditBookActivity::class.java)
            i.putExtra("book", book)
             startActivity(i)
            bottomSheetDialog.dismiss()
        }


        bottomSheetDialog.btn_delete.setOnClickListener {
            mRef.child("Books").child(id).removeValue()
                .addOnSuccessListener {
                    myNotification.sendNotification("Delete Book", "Book deleted Successfully", userToken)
                    Toast.makeText(this, "Book deleted Successfully", Toast.LENGTH_SHORT).show()

                }
                .addOnFailureListener { exception ->
                    Toast.makeText(this, exception.message.toString(), Toast.LENGTH_SHORT).show()
                }

            /*
            db!!.collection("books").document("$id")
                .delete()
                .addOnSuccessListener {
                    Log.d("TAG", "DocumentSnapshot successfully deleted!")
                }
                .addOnFailureListener { e ->
                    Log.w("TAG", "Error deleting document", e)
                }

            */
            bottomSheetDialog.dismiss()
        }
        bottomSheetDialog.show()
    }



//    private fun checkGooglePlayServices(): Boolean {
//        val status = GoogleApiAvailability.getInstance().isGooglePlayServicesAvailable(this)
//        return if (status != ConnectionResult.SUCCESS) {
//            Log.e(TAG, "Error")
//            false
//        } else {
//            Log.d(TAG, "Google play services updated")
//            true
//        }
//    }


}