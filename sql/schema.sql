
## MySQL 5.1+

GRANT ALL ON essc.* TO essc@localhost IDENTIFIED BY 'essc';
FLUSH PRIVILEGES;

/*
CREATE TABLE users (
    id INT UNSIGNED NOT NULL PRIMARY KEY,
  user VARCHAR(255) NOT NULL,
  pass CHAR(32) NOT NULL,
  mail VARCHAR(255) NOT NULL
);

CREATE TABLE products;

CREATE TABLE releases;

*/

/*
JBoss AS 7 datasource:

                <datasource jndi-name="java:jboss/datasources/MysqlDS" pool-name="MysqlDS" enabled="true" use-java-context="true">
                    <connection-url>jdbc:mysql://localhost:3306/essc</connection-url>
                    <driver>mysql</driver>
                    <security>
                        <user-name>essc</user-name>
                        <password>essc</password>
                    </security>
                </datasource>
                <drivers>
                    <driver name="mysql" module="com.mysql.jdbc">
                        <datasource-class>com.mysql.jdbc.Driver</datasource-class>
                    </driver>
                </drivers>
*/