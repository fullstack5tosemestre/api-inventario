docker run -d \
    --rm \
    -p 9090:9090 \
    --env-file .env \
    xdainz/api-inventario:latest
