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

variable "share_account_id" {
  type    = string
  default = "133078896069"
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

  ami_users = ["${var.share_account_id}"]

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
    #    environment_vars = [
    #      "DEBIAN_FRONTEND=noninteractive",
    #      "CHECKPOINT_DISABLE=1"
    #    ]

    scripts = [
      "packer/build_image.sh"
    ]
  }

  provisioner "file" {
    source      = "./target/webapp-0.0.1-SNAPSHOT.jar"
    destination = "/opt/deployment/webapp-0.0.1-SNAPSHOT.jar"
  }

  provisioner "file" {
    source      = "./packer/webapp.service"
    destination = "/etc/systemd/system/webapp.service"
  }

  provisioner "shell" {
    scripts = [
      "packer/launch_app.sh"
    ]
  }


}
