# To run and test

1. `docker-compose up`
2. Compile and launch `account-service` and `user-service`
3. Sent a create account command to `account-service`: `POST http://localhost:8080/account/testAccount`
4. Verify that `account-service` received and consumed the Kafka message: `GET http://localhost:8080/account/all`
5. Verify that `user-service` received and consumed the Kafka message: `GET http://localhost:8888/account/all`