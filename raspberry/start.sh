killall python
clear
python ./main.py&
while true #run indefinitely
do 
echo "###START"
inotifywait -r -e modify,move,create,delete --exclude 'workspace' . 
killall python
clear
python ./main.py&
done
