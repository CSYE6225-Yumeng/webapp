sudo chmod +x /opt/deployment/webapp-0.0.1-SNAPSHOT.jar

#sudo java -jar /opt/deployment/webapp-0.0.1-SNAPSHOT.jar

#systemd

sudo chmod -R 777 /etc/init.d
sudo ln -s /opt/deployment/webapp-0.0.1-SNAPSHOT.jar /etc/init.d/webapp
sudo chown ec2-user:ec2-user /opt/deployment/webapp-0.0.1-SNAPSHOT.jar
sudo systemctl enable /etc/systemd/system/webapp.service
