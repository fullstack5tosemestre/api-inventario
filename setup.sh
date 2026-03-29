docker network create mi_red

docker run -d --name inventario-db --network mi_red -p 3306:3306 -e MYSQL_ALLOW_EMPTY_PASSWORD=yes -e MYSQL_DATABASE=db_inventario mysql:latest 

docker run -d --name inventario --network mi_red -p 9090:9090 --restart on-failure xdainz/api-inventario:latest