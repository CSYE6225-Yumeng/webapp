# webapp

## Prerequisites 
1. programming language: Java JDK 18
2. frameworks: maven, Spring-boot, Spring-security
3. database: docker, postgres
4. third-party libraries: see dependency in pom.xml

## how to build
in terminal, "mvn --batch-mode --update-snapshots package"

## how to test
run src -> test -> java -> com.yumeng.webapp.controller -> UserControllerTest.java, and you can see there should be 5 test passed.

## how to deploy:
1. create a docker containers with a latest postgres image
2. run docker `docker run --name [container_name] -e POSTGRES_PASSWORD=[password] -e POSTGRES_USER=[username] -d [database_name]`
3. enter container`docker exec -it [container_name] bash`
4. enter database `psql postgres://[username]:[password]@localhost:5432/[database_name]`
5. create table in database:
   `CREATE TABLE users (
   id BIGSERIAL PRIMARY KEY,
   first_name TEXT NOT NULL,    
   last_name TEXT NOT NULL,
   password TEXT NOT NULL,
   username TEXT NOT NULL UNIQUE,
   account_created TIMESTAMP DEFAULT CURRENT_TIMESTAMP NOT NULL,
   account_updated TIMESTAMP DEFAULT CURRENT_TIMESTAMP NOT NULL,
   enabled BOOLEAN
   );`
6. `CREATE FUNCTION update_updated_on_user_task()
   RETURNS TRIGGER AS $$
   BEGIN
   NEW.account_updated = now();
   RETURN NEW;
   END;
   $$ language 'plpgsql';`
7. `CREATE TRIGGER update_user_task_updated_on
   BEFORE UPDATE
   ON
   users
   FOR EACH ROW
   EXECUTE PROCEDURE update_updated_on_user_task();`
8. `create table authorities (
   username varchar(50) not null,
   authority varchar(50) not null,
   constraint fk_authorities_users foreign key(username) references users(username)
   );`
9. `create unique index ix_auth_username on authorities (username,authority);`
10. run src -> main -> java -> com.yumeng.webapp -> WebappApplication.java

## The command to import the certificate：
aws acm import-certificate --certificate fileb://demo_yumenghuang_me.crt \
--certificate-chain fileb://demo_yumenghuang_me.ca-bundle \
--private-key fileb://private.key \
--region us-west-1 \
--profile demo
