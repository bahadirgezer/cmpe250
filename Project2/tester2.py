import os.path, subprocess
from subprocess import STDOUT, PIPE

def compile_java (java_file):
    subprocess.check_call(['javac', java_file])

def execute_java ( java_file, inputPath, myOutputPath, logPath ):
    cmd=['java', java_file, inputPath, myOutputPath, logPath]
    proc=subprocess.Popen(cmd, stdout = PIPE, stderr = STDOUT)
    input = subprocess.Popen(cmd, stdin = PIPE)
    print(proc.stdout.read())
i = 1

filePath = "C:\\Users\\bahad\\Desktop\\bounDers\\3.Donem\\Cmpe250\\projects\\Project2\\bin\\project2main"
inputPath = "C:\\Users\\bahad\\Desktop\\bounDers\\3.Donem\\Cmpe250\\projects\\Project2\\testcases2\\input_{}.txt".format(i)
myOutputPath = "C:\\Users\\bahad\\Desktop\\bounDers\\3.Donem\\Cmpe250\\projects\\Project2\\testcases2\\myoutput_{}.txt".format(i)
outputPath = "C:\\Users\\bahad\\Desktop\\bounDers\\3.Donem\\Cmpe250\\projects\\Project2\\testcases2\\output_{}.txt".format(i)
logPath = "C:\\Users\\bahad\\Desktop\\bounDers\\3.Donem\\Cmpe250\\projects\\Project2\\testcases2\\log_{}.txt".format(i)

compile_java("C:\\Users\\bahad\\Desktop\\bounDers\\3.Donem\\Cmpe250\\projects\\Project2\\src\\*.java")
execute_java(filePath, inputPath, myOutputPath, logPath)