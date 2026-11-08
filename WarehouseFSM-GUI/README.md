# Warehouse FSM

```sh
javac -d out *.java
```
In Codespaces or another Bash terminal:
```sh
read -rsp 'Clerk password: ' WAREHOUSE_CLERK_PASSWORD; echo
export WAREHOUSE_CLERK_PASSWORD //Enter password you want to use
read -rsp 'Manager password: ' WAREHOUSE_MANAGER_PASSWORD; echo
export WAREHOUSE_MANAGER_PASSWORD //Enter password you want to use (make it different from the other)

Then

websockify --web=/usr/share/novnc/ 6080 localhost:5901
This is then opened in the new browser tab; navigate to that tab, click on vnc.html, and connect with your password. Since there is no GUI program running it will say there is nothing to display. That is okay.
To run a GUI program we go gback to the codespace tab, start a new bash shell, and navigate to the program we want to run, compile etc. Then we set up the display:
export DISPLAY=:1
Then we can run the java program and see the GUI in the other tab.
java -cp out WarehouseGui.java

Password for noVNC: Warehouse1
```
I