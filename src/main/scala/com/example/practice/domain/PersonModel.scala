package com.example.practice.domain

object PersonModel {
  sealed trait Gender extends Product with Serializable {
    def asString: String
  }
  case object Male extends Gender {
    def asString: String = "male"
  }
  case object Female extends Gender {
    def asString: String = "female"
  }

  final case class Name(d: String)     extends AnyVal
  final case class Age(d: Int)         extends AnyVal
  final case class Address(d: String)  extends AnyVal
  final case class CreateTime(d: Long) extends AnyVal

  final case class Person(id: ID, name: Name, age: Age, gender: Gender, address: Address, createTime: CreateTime)
}
