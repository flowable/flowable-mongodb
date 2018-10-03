# Flowable MongoDB

More information about Flowable: see http://www.flowable.org/ and https://github.com/flowable/flowable-engine

This repository contains the source code for using MongoDB as a persistent data store for the Flowable process engine (support for the other engines (CMMN/DMN) are not yet implemented).

It is currently considered in **alpha** state as not all features have been implemented. Once the default test suite runs fully against this implementation, it will be deemed stable.  

## Requirements

* MongoDB 4.0+
* Flowable 6.4.0+

## Usage

Add the _flowable-process-engine-mongodb_ and _flowable-engine_ dependency to your Maven pom.xml (or a similar operation when not using Maven).

```xml
<dependency>
    <groupId>org.flowable</groupId>
    <artifactId>flowable-engine</artifactId>
    <version>6.4.0</version>
</dependency>
<dependency>
    <groupId>org.flowable</groupId>
    <artifactId>flowable-process-engine-mongodb</artifactId>
    <version>6.4.0.alpha1</version>
</dependency>
```

Create a ProcessEngine instance as follows (changing the server url as needed):

```java
ProcessEngine processEngine = new MongoDbProcessEngineConfiguration()
    .setServerAddresses(Arrays.asList(new ServerAddress("localhost", 27017)))
    .setDisableIdmEngine(true)
    .buildProcessEngine();
```

## Development

### Single server

To set up a local MongoDB server that can be used to execute the unit tests against:

(Following instructions have been tested on OS X only. PR's for other systems appreciated!)

* Download the latest MongoDB community server from https://www.mongodb.com/download-center#community
* Unzip and create a folder _data_ in the unzipped folder
* Start MongoDB as follows through the terminal

```
ulimit -n 4096
./mongod --port 27017 --bind_ip_all --dbpath ../data/ --replSet rs0
```

* Now execute _./mongo_ from the _bin_ folder
* In this shell, execute

```
rs.initiate()
```

Notes:
* The _ulimit_ is needed or otherwise executing the unit tests quickly leads to the 'too many open files' exception.
* Transactions are currently supported on replicasets only, hence the use of the _replSet_ parameter.

### Multiple servers

To set up a cluster of MongoDB servers that share the same replicaset and that can be used to execute the unit tests against:

* Download the latest MongoDB community server from https://www.mongodb.com/download-center#community
* Unzip and create _data1, data2 and data3_ folders in the unzipped folder

* Start the MongoDB instances as follows through separate terminal windows

```
ulimit -n 4096
./mongod --port 27017 --bind_ip_all --dbpath ../data1/ --replSet rs0
```

```
ulimit -n 4096
./mongod --port 27018 --bind_ip_all --dbpath ../data2/ --replSet rs0 
```

```
ulimit -n 4096
./mongod --port 27019 --bind_ip_all --dbpath ../data3/ --replSet rs0
```

* Now execute _./mongo_ from the _bin_ folder
* In this shell, execute

```
rsconf = {_id: "rs0", members: [ {_id: 0, host: "localhost:27017"}, {_id: 1,host: "localhost:27018"}, {_id: 2,host: "localhost:27019"}]}

rs.initiate(rsconf)
```