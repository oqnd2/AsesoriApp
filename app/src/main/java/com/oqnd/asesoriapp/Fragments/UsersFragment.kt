package com.oqnd.asesoriapp.Fragments

import android.os.Bundle
import android.security.keystore.UserPresenceUnavailableException
import android.text.Editable
import android.text.TextWatcher
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.oqnd.asesoriapp.Adapter.UserAdapter
import com.oqnd.asesoriapp.Model.User
import com.oqnd.asesoriapp.R

class UsersFragment : Fragment() {

    private var userAdapter : UserAdapter?=null
    private var userList : List<User>?=null
    private var rvUser : RecyclerView?=null

    private lateinit var txtSearchUser : EditText

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view:View = inflater.inflate(R.layout.fragment_users, container, false)

        rvUser=view.findViewById(R.id.rvUser)
        rvUser!!.setHasFixedSize(true)
        rvUser!!.layoutManager = LinearLayoutManager(context)
        txtSearchUser = view.findViewById(R.id.txtSearchUser)

        userList = ArrayList()
        obtUsers()

        txtSearchUser.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                searchUser(s.toString().lowercase())
            }

            override fun afterTextChanged(s: Editable?) {

            }

        })

        return view
    }

    private fun obtUsers() {
        val firebaseUser = FirebaseAuth.getInstance().currentUser!!.uid
        val reference = FirebaseDatabase.getInstance().reference.child("Users").orderByChild("userName")
        reference.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                (userList as ArrayList<User>).clear()
                if (txtSearchUser.text.toString().isEmpty()){
                    for (sh in snapshot.children){
                        val user:User?=sh.getValue(User::class.java)
                        if (!(user!!.getUid()).equals(firebaseUser)){
                            (userList as ArrayList<User>).add(user)
                        }
                    }
                    userAdapter= UserAdapter(context!!, userList!!)
                    rvUser!!.adapter=userAdapter
                }
            }

            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }

        })
    }

    private fun searchUser(searchUser : String){
        val firebaseUser = FirebaseAuth.getInstance().currentUser!!.uid
        val query = FirebaseDatabase.getInstance().reference.child("Users").orderByChild("search")
            .startAt(searchUser).endAt(searchUser + "\uf8ff")
        query.addValueEventListener(object : ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                (userList as ArrayList<User>).clear()

                for (sh in snapshot.children){
                    val user:User?=sh.getValue(User::class.java)
                    if (!(user!!.getUid()).equals(firebaseUser)){
                        (userList as ArrayList<User>).add(user)
                    }
                }
                userAdapter= UserAdapter(context!!, userList!!)
                rvUser!!.adapter=userAdapter
            }

            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }

        })
    }

}