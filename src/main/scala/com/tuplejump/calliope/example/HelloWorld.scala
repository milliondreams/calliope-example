package com.tuplejump.calliope.example

import java.nio.ByteBuffer

import com.datastax.driver.core.Row
import com.tuplejump.calliope.CasBuilder
import com.tuplejump.calliope.Implicits._
import com.tuplejump.calliope.Types._
import com.tuplejump.calliope.utils.RichByteBuffer._
import org.apache.spark.{SparkConf, SparkContext}

object HelloWorld extends Serializable {

  private val keyspace = "cql3_test"

  private val readTable = "emp_read_test"

  private val writeTable = "emp_write_test"

  /** Used with nativeCassandra **/
  implicit val row2employee: Row => Employee = {
    row => Employee(row.getInt("deptid"), row.getInt("empid"), row.getString("first_name"), row.getString("last_name"))
  }

  /** Used with cql3SaveToCassandra **/
  implicit val employee2RowKeyMap: Employee => CQLRowKeyMap = {
    emp =>
      Map[ByteBuffer, ByteBuffer]("deptid" -> emp.deptid, "empid" -> emp.empid)
  }

  implicit val employee2RowValues: Employee => CQLRowValues = {
    emp =>
      List(emp.first_name, emp.last_name)
  }

  /** Used by cql3Cassandra **/
  implicit val cas2Employee: (CQLRowKeyMap, CQLRowMap) => Employee = {
    case (k, v) => Employee(k("deptid"), k("empid"), v("first_name"), v("last_name"))
  }


  def main(args: Array[String]) {

    require(args.length == 2, "Syntax: run <sparkMasterIP> <sparkHome>")

    val host = if (args.length <= 0) "127.0.0.1" else args(0)

    val conf = new SparkConf().setAppName("calliope-demo").setMaster(s"spark://${host}:7077")

    //TODO: Add the jar to the conf add jar

    val jar = getClass.getProtectionDomain.getCodeSource.getLocation.getPath

    println("Will add this to class path: " + jar)

    conf.setJars(Seq(jar))

    val sc = new SparkContext(conf)

    /** NATIVE CQL DEMO **/
    /* val inputCql: String = s"SELECT * FROM ${keyspace}.${readTable} WHERE token(deptid) > ? AND token(deptid) <= ? AND empid = 105 ALLOW FILTERING"

    val casRead = CasBuilder.native.withColumnFamilyAndQuery(keyspace, readTable, inputCql).onHost(host).mergeRangesInMultiRangeSplit(256)

    println("Reading Employees Using Native CQL: ")

    val elist = sc.nativeCassandra[Employee](casRead).collect()

    println(elist)

    elist.map(println)

    Thread.sleep(10000) */

    /** CQL3 on Thrift **/
    val casReadc = CasBuilder.cql3.withColumnFamily(keyspace, readTable).onHost(host).where("empid = 105")

    println("Reading Employees Using CQL on Thrift: ")

    val elistc = sc.cql3Cassandra[Employee](casReadc).collect()

    println(elistc)

    elistc.map(println)

    Thread.sleep(10000)

    println("Writing TO Cassandra: ")

    /* val elist2 = List(Employee(1001, 10011, "Arthur", "Bentley"), Employee(1001, 10012, "Andrew", "Reeds"), Employee(1001, 10013, "James", "Courtney"), Employee(1001, 10014, "Richard", "Haley"), Employee(1002, 10021, "Clarence", "Brown"))

    val empRdd = sc.parallelize(elist2)

    val casWrite = CasBuilder.cql3.withColumnFamily(keyspace, writeTable).onHost(host).saveWithQuery(s"UPDATE ${keyspace}.${writeTable} set first_name = ?, last_name = ?")

    empRdd.cql3SaveToCassandra(casWrite)

    Thread.sleep(10000) */


  }
}

case class Employee(deptid: Int, empid: Int, first_name: String, last_name: String)
