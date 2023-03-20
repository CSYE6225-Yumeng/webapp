# update and upgrade
sudo yum -y update
sudo yum -y upgrade
# java
sudo amazon-linux-extras install java-openjdk11
sudo wget https://download.java.net/java/GA/jdk19.0.1/afdd2e245b014143b62ccb916125e3ce/10/GPL/openjdk-19.0.1_linux-x64_bin.tar.gz
sudo tar -xvzf openjdk-19.0.1_linux-x64_bin.tar.gz
path=$(find `pwd`/jdk-19.0.1/bin/java -type f); sudo alternatives --install /usr/bin/java java $path 20000;
yes 2 | sudo update-alternatives --config java
## postgresql14
#sudo amazon-linux-extras enable postgresql14
#sudo yum install postgresql-server -y
#sudo postgresql-setup initdb
#sudo systemctl start postgresql
#sudo systemctl enable postgresql
#sudo systemctl status postgresql
#sudo chmod -R 777 /home/ec2-user
#sudo chmod -R 750 /var/lib/pgsql/data
#sudo sed -i 's/peer/trust/g' /var/lib/pgsql/data/pg_hba.conf
#sudo sed -i 's/ident/trust/g' /var/lib/pgsql/data/pg_hba.conf
#sudo service postgresql restart
# cloudwatch
sudo yum install amazon-cloudwatch-agent
# mkdir & policy
sudo mkdir /opt/deployment
sudo mkdir /var/log/apps

sudo chmod -R 777 /opt/deployment
sudo chmod -R 777 /var/log/apps
sudo chmod -R 777 /etc/systemd/system
sudo chmod -R 777 /var/log
