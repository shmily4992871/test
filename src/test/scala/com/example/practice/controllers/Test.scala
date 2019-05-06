package com.htc.vr.heyreporterservice.controllers

object Test {
  def main(args: Array[String]): Unit = {
    val s = Iterable(("1", Array("a")), ("2", Array("b")))
    for (r <- s) {
      val key = r._1
      val value = r._2.mkString
      println(s"key: $key value: $value")
    }
  }
}
