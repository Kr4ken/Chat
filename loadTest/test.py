from subprocess import Popen, PIPE, STDOUT
import os
import time
from threading import Thread
import _thread


clientPath = 'C:\\Chat\\client\\target\\ChatClient-jar-with-dependencies.jar'
javaPath = 'C:\\Program Files\\Java\\jre1.8.0_60\\bin\\java.exe'
threadCount = 30
spamcount = 100
delay = 1

def client(name):
	p = Popen([javaPath,"-jar",clientPath, name],  stdin=PIPE, stderr=STDOUT , encoding='utf8')
	for i in range(1,spamcount):
		p.stdin.write('Hello from' + name + ' with ' + str(i) +' iteration\n')
		p.stdin.flush()
		time.sleep(delay)
	p.stdin.write('/close\n')
	p.stdin.flush()

for i in range(1,threadCount):
	_thread.start_new_thread( client, (str("Client_" + str(i)),))

while 1:
	pass