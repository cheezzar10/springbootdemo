Ordering service MVP

1. Building

$ mvn package

2. Running

$ java -jar target/springbootdemo-0.0.1-SNAPSHOT.jar

3. Testing using curl

instant order checkout 

$ curl -X POST -H 'Content-Type: application/json' -d '{"customerName":"Billy Bones","customerPhone":"111","customerEmail":"bb@hispaniola.uk","customerAddress":"Treasure Island","productSku":"P-1"}' http://localhost:8080/ordering/instant-checkout

NOTE: order records retrieval methods access protected by Basic auth ( OK for MVP, just to check user roles set )

retrieving existing order by id

$ curl -v -u billy:password http://localhost:8080/ordering/order/1

retrieving all orders

$ curl -v -u billy:password http://localhost:8080/ordering/orders