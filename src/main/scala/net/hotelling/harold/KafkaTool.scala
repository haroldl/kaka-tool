package net.hotelling.harold

import scala.collection.JavaConverters._
import java.util.Properties
import kafka.producer.ProducerConfig
import kafka.producer.Producer
import kafka.producer.KeyedMessage
import org.I0Itec.zkclient.ZkClient
import org.I0Itec.zkclient.serialize.BytesPushThroughSerializer
import scala.util.parsing.json.JSON

object KafkaTool {

  def main(args: Array[String]) {
    val (zookeeper, zookeeperPath) = (args(0), args(1))
    println(s"Using zookeeper at $zookeeper with path $zookeeperPath")
    val brokers = findKafkaBrokers(zookeeper, zookeeperPath)
    val producer = connectToKafka(brokers)

    val data = new KeyedMessage("test123", "hello", "world at " + System.currentTimeMillis())
    producer.send(data)
  }

  def findKafkaBrokers(zookeeper: String, zookeeperPath: String): String = {
    val zkClient = new ZkClient(zookeeper, 4000, 6000, new BytesPushThroughSerializer())
    zkClient.getChildren(zookeeperPath + "/brokers/ids").asScala.toList.map({
      child =>
        val bytes = zkClient.readData[Array[Byte]](zookeeperPath + "/brokers/ids/" + child)
        val str = new String(bytes)
        val json = JSON.parseFull(str).get.asInstanceOf[Map[String,AnyRef]]
        val port = json("port").asInstanceOf[Double].toInt
        json("host") + ":" + port
    }).mkString(",")
  }

  def connectToKafka(brokers: String): Producer[String,String] = {
    val props = new Properties()
    props.put("metadata.broker.list", brokers)
    props.put("serializer.class", "kafka.serializer.StringEncoder")
    val config = new ProducerConfig(props)
    new Producer[String,String](config)
  }

}

