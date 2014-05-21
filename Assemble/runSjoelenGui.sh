export LD_LIBRARY_PATH=./opencvlibraries/linux/x64:$LD_LIBRARY_PATH

java -Djava.library.path=opencvlibraries/linux/x64 -jar ../Modules/sjoelengui/target/sjoelengui-1.0-SNAPSHOT.jar
