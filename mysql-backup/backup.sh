
# 1.Backup the current sql data.
mysqldump -uroot -p mvc-user > /opt/mvc-user.sql

# 2.Edit mysql config.
vi /etc/mysql/mysql.conf.d/mysqld.cnf

server-id               = 8
log_bin                 = /var/lib/mysql/mysql-bin
binlog_do_db            = mvc-user

service mysql restart

# 3.SSH bypass password login.
# on backup server allow app server's access.
# on app server
ssh-keygen -t rsa
# input
/root/.ssh/id_rsa
# ignore password, just enter.
# on backup server
vi /root/.ssh/authorized_keys
# on app server paste vi /root/.ssh/id_rsa.pub

# 4.Send initial backup file to backup server.
# on backup server
mkdir -p /opt/mysql-backup/lock-process

# 5.Java development to make app server to send binlog files to backup server daily.