docker exec --interactive --tty broker kafka-console-consumer --bootstrap-server broker:9092 --topic users --from-beginning --property print.headers=true
