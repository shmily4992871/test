package com.example.practice.models.http

final case class PersonRequest(name: String, age: Int, gender: String, address: String)
final case class UpdatePersonRequest(id: String, name: String, age: Int, gender: String, address: String)
