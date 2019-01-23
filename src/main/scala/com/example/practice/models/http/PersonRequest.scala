package com.example.practice.models.http

case class PersonRequest(name: String, age: Int, gender: String, address: String)
case class UpdatePersonRequest(id: String, name: String, age: Int, gender: String, address: String)
