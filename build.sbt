
libraryDependencies ++= Seq(
  "org.apache.zookeeper" % "zookeeper" % "3.3.4",
  "kafka" % "kafka_2.10" % "0.8.1.151"
)

initialCommands in console := """import scala.collection.JavaConverters._
import net.hotelling.harold._
import KafkaTool._
"""
