package com.example.practice.domain.db

import com.example.practice.domain.Gender

final case class DBPerson(id: String, name: String, age: Int, gender: Gender, address: String, create_time: Long)
