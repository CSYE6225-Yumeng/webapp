sudo chmod +x /opt/deployment/webapp-0.0.1-SNAPSHOT.jar
#sudo java -jar /opt/deployment/webapp-0.0.1-SNAPSHOT.jar



#systemd
sudo chmod -R 777 /etc/init.d
sudo ln -s /opt/deployment/webapp-0.0.1-SNAPSHOT.jar /etc/init.d/webapp
sudo chown ec2-user:ec2-user /opt/deployment/webapp-0.0.1-SNAPSHOT.jar
sudo systemctl enable /etc/systemd/system/webapp.service

#cloudWatch
sudo touch /var/log/webapp.log
sudo chmod o+x /var/log
sudo chmod -R 777 /var/log/webapp.log

sudo /opt/aws/amazon-cloudwatch-agent/bin/amazon-cloudwatch-agent-ctl \
    -a fetch-config \
    -m ec2 \
    -c file:/opt/deployment/cloudwatch_config.json \
    -s

# AWS CLI - Create a new Launch Template for the autoscaling group

aws configure set aws_access_key_id ${{ secrets.DEMO_AWS_ACCESS_KEY_ID }} --profile demo
aws configure set aws_secret_access_key ${{ secrets.DEMO_AWS_SECRET_ACCESS_KEY }} --profile demo
aws configure set region $REGION --profile demo
aws configure set aws_access_key_id ${{ secrets.DEV_AWS_ACCESS_KEY_ID }} --profile dev
aws configure set aws_secret_access_key ${{ secrets.DEV_AWS_SECRET_ACCESS_KEY }} --profile dev
aws configure set region $REGION --profile dev

export LAUNCH_TEMPLATE_ID=`aws ec2 describe-launch-templates  \
--region $REGION \
--profile $PROFILE_NAME \
--query 'LaunchTemplates[0].LaunchTemplateId' \
--output text`

export IMAGE_ID=`aws ec2 describe-images \
--region $REGION \
--profile $PROFILE_NAME \
--filters "Name=name,Values=yumeng_*" \
--query 'sort_by(Images, &CreationDate)[-1].ImageId' \
--output text`

aws ec2 create-launch-template-version \
  --launch-template-id $LAUNCH_TEMPLATE_ID \
  --version-description new_version \
  --source-version 1 \
  --launch-template-data "{\"ImageId\":\"${IMAGE_ID}\"}" \
  --region $REGION \
  --profile $PROFILE_NAME

aws autoscaling start-instance-refresh \
    --auto-scaling-group-name $AUTO_SCALING_GROUP_NAME \
    --region $REGION \
    --profile $PROFILE_NAME