# usermanagement-boot

## Basic Configurations
tomcat-users.xml 

```
<role rolename="tomcat"/>
  <role rolename="manager-gui"/>
  <role rolename="manager-script"/>
  <role rolename="admin-gui"/>
  <user username="admin" password="admin" roles="manager-gui,manager-script,admin-gui,tomcat"/>
</tomcat-users>
```

maven> conf > settings.xml

```
<server>
      <id>TomcatServer</id>
      <username>admin</username>
      <password>admin</password>
</server>
```

## Maven Commands to run

```
mvn spring-boot:run
```


##### DataBase name
```
ums-with-spring
```
