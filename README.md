

Uses sbt 0.13

sbt eclipse

sbt console
runMain net.hotelling.harold.KafkaTool zookeeperHost:port zookeeperPath

See the events sent via:

    kafka-console-consumer.sh --zookeeper <host:port/path> --topic test123 [--from-beginning]

