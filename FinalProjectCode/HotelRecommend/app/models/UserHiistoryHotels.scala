package models

import models.DecisionTreeClassifierModel.spark
import org.apache.spark.SparkConf
import org.apache.spark.sql.SparkSession
case class DetailUser(userId:String,dateTime:String,country:String)

object UserHiistoryHotels {


  // 1. Create Spark configuration
  val conf = new SparkConf()
    .setAppName("SparkMe Application")
    .setMaster("local[*]")  // local mode

  // 2. Create Spark context
  //val sc = new SparkContext(conf)

  val spark = SparkSession.builder().appName("My Application").master("local").getOrCreate()

  def parseUserData(str: String): DetailUser= {
    val fields = str.split(",")
    print(fields(0))
    assert(fields.size == 24)
    DetailUser(fields(7), fields(0),fields(15))
  }

  import spark.implicits._
  def getHistoricalBookingsOfUser(userId:String):List[String]={
val dataPath="C:\\Users\\sweta\\Desktop\\export.csv"
    val file=spark.read.textFile(dataPath)

    val trainingheader = file.first()

    val datasetOfUsers=file.filter(row => row != trainingheader).filter(row=>row(18)!=0).map(parseUserData)

    val allUsers=datasetOfUsers.toDF("UserId","DateTime","CountryId")

    allUsers.show(10)

   val userHistory= allUsers.select($"UserId",$"DateTime",$"CountryId").where($"UserId"===userId)

    userHistory.rdd.map(x =>{x.get(0).toString+" "+x.get(1).toString+" "+x.get(2).toString}).collect().toList


  }

  def main(args: Array[String]): Unit = {

    print(getHistoricalBookingsOfUser("427859"))

  }

}
