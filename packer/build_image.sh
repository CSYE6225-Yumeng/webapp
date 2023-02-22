# update and upgrade
sudo yum -y update
sudo yum -y upgrade
# java
sudo amazon-linux-extras install java-openjdk11
sudo wget https://download.java.net/java/GA/jdk19.0.1/afdd2e245b014143b62ccb916125e3ce/10/GPL/openjdk-19.0.1_linux-x64_bin.tar.gz
sudo tar -xvzf openjdk-19.0.1_linux-x64_bin.tar.gz
path=$(find `pwd`/jdk-19.0.1/bin/java -type f); sudo alternatives --install /usr/bin/java java $path 20000;
yes 2 | sudo update-alternatives --config java
# postgresql14
sudo amazon-linux-extras install postgresql14-server
sudo /usr/pgsql-14/bin/postgresql-14-setup initdb
sudo systemctl start postgresql-14
sudo systemctl enable postgresql-14
sudo systemctl status postgresql-14
sudo passwd postgres
su - postgres
psql -c "ALTER USER postgres WITH PASSWORD 'postgrespass';"
# mkdir & policy
sudo mkdir /opt/deployment
sudo mkdir /var/log/apps
sudo chmod -R 777 /opt/deployment
sudo chmod -R 777 /var/log/apps
