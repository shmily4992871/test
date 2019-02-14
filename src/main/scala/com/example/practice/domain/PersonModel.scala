package com.example.practice.domain

import enumeratum.EnumEntry.Lowercase
import enumeratum.{Enum, EnumEntry, ReactiveMongoBsonEnum}

object PersonModel {
  final case class Name(d: String)     extends AnyVal
  final case class Age(d: Int)         extends AnyVal
  final case class Address(d: String)  extends AnyVal
  final case class CreateTime(d: Long) extends AnyVal

  final case class Person(id: ID, name: Name, age: Age, gender: Gender, address: Address, createTime: CreateTime)
  final case class UpdatePerson(id: ID, name: Name, age: Age, gender: Gender, address: Address)
}

sealed trait Gender extends EnumEntry with Product with Serializable

object Gender extends Enum[Gender] with ReactiveMongoBsonEnum[Gender] {
  val values = findValues

  case object Male   extends Gender with Lowercase
  case object Female extends Gender with Lowercase
}
