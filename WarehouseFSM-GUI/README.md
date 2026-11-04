# Warehouse FSM

## Requirements
Use Java 16 or newer. The backend uses Java records.
## Compile and run
From this folder, run:
```sh
javac -d out *.java
```
`WarehouseContext` is the entry point. Compiled `.class` files go into `out/`
and are excluded from Git.
## Clerk and Manager passwords
```sh
read -s "WAREHOUSE_CLERK_PASSWORD?Clerk password: "
export WAREHOUSE_CLERK_PASSWORD
read -s "WAREHOUSE_MANAGER_PASSWORD?Manager password: "
export WAREHOUSE_MANAGER_PASSWORD
echo
java -cp out WarehouseContext
```
In Codespaces or another Bash terminal:
```sh
read -rsp 'Clerk password: ' WAREHOUSE_CLERK_PASSWORD; echo
export WAREHOUSE_CLERK_PASSWORD
read -rsp 'Manager password: ' WAREHOUSE_MANAGER_PASSWORD; echo
export WAREHOUSE_MANAGER_PASSWORD
java -cp out WarehouseContext
```
I