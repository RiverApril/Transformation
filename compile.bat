
echo off
del binvs /S /Q
mkdir binvs
javac -cp lib\lwjgl.jar src\main\test.java -d binvs
cd binvs
start java MyApplication