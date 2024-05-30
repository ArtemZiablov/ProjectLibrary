FROM postgres:16
ENV POSTGRES_USER=postgres
ENV POSTGRES_PASSWORD=postgres
ENV POSTGRES_DB=lms_db
COPY ./sql /docker-entrypoint-initdb.d
EXPOSE 5432
CMD ["postgres"]
