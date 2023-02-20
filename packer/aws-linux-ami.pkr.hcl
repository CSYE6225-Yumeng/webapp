// packer {
//   required_plugins {
//     amazon = {
//       source = "github.com/hashicorp/amazon"
//     }
//   }
// }


variable "aws_region" {
  type    = string
  default = "us-west-1"
}

variable "source_ami" {
  type    = string
  default = "ami-00569e54da628d17c"
}

variable "ssh_username" {
  type    = string
  default = "ec2-user"
}

variable "subnet_id" {
  type    = string
  default = "subnet-07cf96be39beac432"
}

source "amazon-ebs" "amazon_linux_2" {
  // ami_name        = "yumeng"
  ami_name        = "yumeng_${formatdate("YYYY_MM_DD_hh_mm_ss", timestamp())}"
  instance_type   = "t2.micro"
  region          = "${var.aws_region}"
  ami_description = "AMI for CSYE 6225"
  source_ami      = "${var.source_ami}"
  ssh_username    = "${var.ssh_username}"
  subnet_id       = "${var.subnet_id}"

  ami_regions = [
    "${var.aws_region}"
  ]

  aws_polling {
    delay_seconds = 120
    max_attempts  = 50
  }

  launch_block_device_mappings {
    delete_on_termination = true
    device_name           = "/dev/xvda"
    volume_size           = 8
    volume_type           = "gp2"
  }

}

build {
  name = "learn-packer"
  sources = [
    "source.amazon-ebs.amazon_linux_2"
  ]

  provisioner "shell" {
    environment_vars = [
      "DEBIAN_FRONTEND=noninteractive",
      "CHECKPOINT_DISABLE=1"
    ]
    inline = [
      "sudo yum -y update",
      "sudo yum -y upgrade",
      "sudo amazon-linux-extras install java-openjdk11", //ds sudo amazon-linux-extras install java-openjdk11
      // "sudo amazon-linux-extras install tomcat9",
      // "sudo chkconfig tomcat on",
      // "sudo service tomcat start",
      // "sudo amazon-linux-extras install postgresql14",
      // "sudo amazon-linux-extras enable postgresql15",
      // "sudo yum install postgresql-server -y",
      // "sudo postgresql-setup initdb",
      // "sudo systemctl start postgresql",
      // "sudo systemctl enable postgresql",
      // "sudo systemctl status postgresql", 

      // "sudo yum clean"
    ]
  }
}
