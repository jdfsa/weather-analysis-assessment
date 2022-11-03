#!/bin/bash

# configure mongo yum repository
tee -a ./mongodb-org.repo << END
[mongodb-org-6.0]
name=MongoDB Repository
baseurl=https://repo.mongodb.org/yum/redhat/8/mongodb-org/6.0/x86_64/
gpgcheck=1
enabled=1
gpgkey=https://www.mongodb.org/static/pgp/server-6.0.asc
END

sudo mv ./mongodb-org.repo /etc/yum.repos.d/mongodb-org.repo

# install mongo and other tools
sudo yum install -y mongodb-org wget nano

# install certificates to log into mongodb
wget https://s3.amazonaws.com/rds-downloads/rds-ca-2019-root.pem

# --------------------------------------
# log into mongodb cluster
mongosh --ssl \
--host weather-analysis-db.cluster-c4se9etyjkgh.us-east-1.docdb.amazonaws.com:27017 \
--sslCAFile rds-ca-2019-root.pem \
--username root --password root2022

