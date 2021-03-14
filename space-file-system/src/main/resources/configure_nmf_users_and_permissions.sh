#!/bin/sh
cd ${0%/*}

# Variables:
user_nmf_admin="nmf-admin"
user_nmf_admin_password="password"
group_nmf_apps="nmf-apps"
supervisor_mainclass="esa.mo.nmf.provider.NanoSatMOSupervisorRaspberryPiImpl"
start_script_name="start_supervisor.sh"

# The script must be run as root
if [ $(whoami) != 'root' ]; then
	echo "The current user is: $(whoami)"
	echo "Please run this script as root or with sudo!"
	exit 1
fi

# Add NMF Admin user and set password:
useradd $user_nmf_admin -m -s /bin/bash --user-group
echo $user_nmf_admin:$user_nmf_admin_password | chpasswd

# Allow the nmf-admin user to run the useradd command without password: 
#$user_nmf_admin ALL= NOPASSWD: /sbin/halt
#echo 'foobar ALL=(ALL:ALL) ALL' | sudo EDITOR='tee -a' visudo

# Add NMF App Group:
groupadd $group_nmf_apps

# Create Directories
mkdir apps
mkdir libs
mkdir packages
mkdir public_square
mkdir nmf_updates

# Create the start script for the nmf: start_supervisor.sh
cat > $start_script_name <<EOF
#!/bin/sh
cd \${0%/*}
java -classpath "libs/*" $supervisor_mainclass
EOF

# Set Owner and Group + Permissions to the folders and files
chown root:$user_nmf_admin .
chmod 775 .
chown $user_nmf_admin:$user_nmf_admin apps
chmod 775 apps
chown $user_nmf_admin:$user_nmf_admin libs
chmod 775 libs
chown $user_nmf_admin:$user_nmf_admin packages
chmod 700 packages
chown $user_nmf_admin:$user_nmf_apps public_square
chmod 770 public_square
chown $user_nmf_admin:$user_nmf_admin nmf_updates
chmod 700 nmf_updates
chown $user_nmf_admin:$user_nmf_admin $start_script_name
chmod 700 $start_script_name


