import random
import os
from os import path

INPPATH = "ex_inputs"
if not os.path.exists(INPPATH):
    os.makedirs(INPPATH)
os.chdir(INPPATH)

def createTestCase1(path , sc , sd , MAXW):
        
    file = open(path , "w")
    t = random.randint(1,sc * MAXW)
    file.write(str(t) + "\n")
    n = sc + sd
    file.write(str(n) + "\n")
    file.write("c1 c" + str(sc))

    for i in range(1,sc+1):
        file.write("\nc"+str(i) + " ")
        for j in range(1,sc+1):
            if i != j and random.randint(0,5 + (i == sc) * 10) <= 3:
                file.write("c"+str(j) + " " + str(random.randint(1,MAXW)) + " ")
        if (i == sc):
            for j in range(1, sc+1):
                if random.randint(0,5) > 1:
                    file.write("d"+str(j) + " " + str(random.randint(1,MAXW)) + " ")

    for i in range(1,sd+1):
        file.write("\nd"+str(i) + " ")
        for j in range(1,sd+1):
            if i != j and random.randint(0,1) == 0:
                file.write("d"+str(j) + " " + str(random.randint(1,MAXW)) + " ")


    file.close()


officalTest = 13

for test in range(officalTest,1001):
    createTestCase1("input" + str(test) + ".txt" , 5, 5 , 10)

for test in range(1001,1051):
    createTestCase1("input" + str(test) + ".txt" ,200,200 , 1000)
