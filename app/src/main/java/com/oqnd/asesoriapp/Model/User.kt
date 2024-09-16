package com.oqnd.asesoriapp.Model

class User {
    private var uid : String = ""
    private var userName : String = ""
    private var email : String = ""
    private var image : String = ""
    private var search : String = ""
    private var name : String = ""
    private var lastName : String = ""
    private var age : String = ""
    private var type : String = ""
    private var phone : String = ""

    constructor()

    constructor(
        uid: String,
        userName: String,
        email: String,
        image: String,
        search: String,
        name: String,
        lastName: String,
        age: String,
        type: String,
        phone: String
    ) {
        this.uid = uid
        this.userName = userName
        this.email = email
        this.image = image
        this.search = search
        this.name = name
        this.lastName = lastName
        this.age = age
        this.type = type
        this.phone = phone
    }

    //getters y setters
    fun getUid() : String?{
        return uid
    }

    fun setUid(uid : String){
        this.uid = uid
    }

    fun getUsername() : String?{
        return userName
    }

    fun setN_Usuario(n_usuario : String){
        this.userName = n_usuario
    }

    fun getEmail() : String?{
        return email
    }

    fun setEmail(email : String){
        this.email = email
    }

    fun getImage() : String?{
        return image
    }

    fun setImagen(imagen : String){
        this.image = imagen
    }

    fun getSearch() : String?{
        return search
    }

    fun setBuscar(buscar : String){
        this.search = buscar
    }

    fun getName() : String?{
        return name
    }

    fun setNombres(nombres : String){
        this.name = nombres
    }

    fun getLastname() : String?{
        return lastName
    }

    fun setApellidos(apellidos : String){
        this.lastName = apellidos
    }

    fun getAge() : String?{
        return age
    }

    fun setEdad(edad : String){
        this.age = edad
    }

    fun getType() : String?{
        return type
    }

    fun setProfesion(profesion : String){
        this.type = profesion
    }

    fun getPhone() : String?{
        return phone
    }
}