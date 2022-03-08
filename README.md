# Termoidraulica FS
This is a software for Termoidraulica f.lli Sassoli company. 

It can manage three main aspects of the company, the customer, the estimate and the receipt manging.

### Requirements
- PostgreSQL (version 14 or higher)
- JRE (version 16 or higher)

### Install instructions
Download the latest release from the GitHub repository, so put the `.jar` file in a folder.
Then from the shell run the following command:
```
java -jar termoidraulica_fs.jar
```
Otherwise you can create a script for your OS system. Under [Script](https://github.com/tommasosassoli/termoidraulica-fs/script)
folder you can find some script that able to run the application with and without the shell. 
Copy them into the same folder and run.

### Connection to database instructions
You need to open the `termoidraulica_fs.jar` file and looking for `config/database.properties` file.
Open this and change the field that you need.
This is an example:
```
host=localhost
port=5432
db_name=termoidraulica_fs
user=tfs_user
password=1234
```

### Update instructions
Simply, you need to replace the file `termoidraulica_fs.jar` with the more recent one. 
Maybe you need to change the database connection property again.
