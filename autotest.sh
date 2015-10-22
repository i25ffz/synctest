#!/bin/sh

# count default to 10
c=10

if [[ $# -ge 1 ]]; then
  c=$1
else
  echo "usage: ./autotest.sh 10"
  exit 1
fi

# javac
mkdir bin
cd src
javac -d ../bin *.java
cd ..
echo "build java ok!"

./build.sh
echo "build native ok!"

echo "type,value"

# run loop
i=0
while [ $i -le $c ]
do
  # echo "index:"$i
  
  java -cp bin TestAtomic 3000000
  echo "-------------------------"
  java -cp bin TestVolatile 3000000
  echo "-------------------------"
  java -cp bin TestLock 3000000
  java -cp bin TestLock 3000000 -f
  echo "-------------------------"
  java -cp bin TestSync 3000000
  echo "-------------------------"
  java -cp bin -Djava.library.path=. TestMutexSingle 3000000
  java -cp bin -Djava.library.path=. TestMutexMulti 3000000
  echo "-------------------------"
  
  sleep 1
  i=$(($i+1 ))
done
