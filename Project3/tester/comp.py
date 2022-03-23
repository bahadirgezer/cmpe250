import os
import sys

TEST_CNT = 1051
MAX_PARTITION = 50

bin_path = sys.argv[1]
# print(bin_path)

if not os.path.exists("myOutput"):
    os.makedirs("myOutput")

# cwd = os.getcwd()
# print(f"for i in {{1..{TEST_CNT}}};do " 
# + "java -cp " + bin_path + " project3main ./ex_inputs/input$i.txt " + "./myOutput/output$i.txt;"
# + "done;")

def inpData(inp):
	inpLines = []
	with open(inp) as f_in:
		inpLines = list(line for line in (l.strip() for l in f_in) if line)

	sid,lid = inpLines[2].split();

	pdt = []
	c = 3
	while c < len(inpLines):
		nRow = inpLines[c].split()
		qid = nRow[0]
		#print(f"qid : {qid}")
		if (qid[0] != 'c'):
			break
		qN = dict()
		i = 1
		while i < len(nRow):
			nodeN = nRow[i]
			wN = nRow[i+1]
			qN[nodeN] = wN
			i+=2
		pdt.append(qN)
		c+=1

	return sid, lid , pdt	

def path_calc(inpData , pathStr):

	path = pathStr.split()
	#print(f"path --> {path}")
	
	if inpData[0] != path[0]:
		return -2;
	if inpData[1] != path[-1]:
		return -3

	graph = inpData[2]

	curr = 1
	cn = inpData[0]
	cost = 0
	while (curr < len(path)):
		node = path[curr]
		#print(node)
		if not 'c' in node:
			return -1
		cnId = int(cn.split('c')[1])

		if not node in graph[cnId-1].keys():
			return -1
		else:
			cost += int(graph[cnId-1][node])

		cn = node
		curr += 1
	#print(cost)	
	return cost

def check(inp_file , ans_file , out_file):

	ansLines = []
	with open(ans_file) as f_in:
		ansLines = list(line for line in (l.strip() for l in f_in) if line)
	
	outLines = []
	with open(out_file) as f_in:
		outLines = list(line for line in (l.strip() for l in f_in) if line)

	#print(ansLines , outLines)

	if (len(outLines)!=2):
		return "WRONG (expected 2 line of output)"

	if (ansLines == outLines):
		return "OK (identical)"

	if ansLines[1] != outLines[1]:
		return "WRONG (incorrect mst cost)"

	if (ansLines[0] != outLines[0] and outLines[0] != "-1"):
		inp_data = inpData(inp_file)
		ansCost = path_calc(inp_data , ansLines[0])
		outCost = path_calc(inp_data , outLines[0])
		if outCost == -3:
			return f"WRONG cannot reach leyla with this path"
		elif outCost == -2:
			return f"WRONG path should start from mecnun"
		elif outCost == -1:
			return "WRONG this path does not exist"
		elif ansCost != outCost :
			return f"WRONG (min path costs {ansCost} but yours {outCost})"
		else:
			return f"OK (different but valid paths with cost {ansCost})"

	return "WRONG"

def extract():
	#os.system("echo != extracting outputs\n")
	print("!= extracting outputs")
	part = 0
	while part*MAX_PARTITION < TEST_CNT:
		print(f"processing {MAX_PARTITION * part} to {min(MAX_PARTITION * (part+1)-1, TEST_CNT)}")
		for i in range(MAX_PARTITION * part , 1 + min(MAX_PARTITION * (part+1)-1, TEST_CNT)):
			os.system(f"java -cp " + bin_path + f" project3main ./ex_inputs/input{i}.txt " + f"./myOutput/output{i}.txt");	
		part += 1
	print("!= output extraction done!")
		#os.system("echo " + f"processing {MAX_PARTITION * part} to {min(MAX_PARTITION * (part+1)-1, TEST_CNT)}")
		#os.system(f"for i in {{{MAX_PARTITION * part}..{min(MAX_PARTITION * (part+1)-1, TEST_CNT) }}};do " 
		#+ "java -cp " + bin_path + " project3main ./ex_inputs/input$i.txt " + "./myOutput/output$i.txt;"
		#+ "done;");
		#os.system("echo != output extraction done!")

def judge():
	#os.system("echo != system is judging your output")
	print("!= system is judging your output")
	correct = 0
	for i in range(TEST_CNT):
		response = check(f"./ex_inputs/input{i}.txt" , f"./ex_outputs/output{i}.txt" , f"./myOutput/output{i}.txt")
		if "OK" in response:
			correct += 1
		print(f"Test {i}: " + response)
	print(f"CORRECT <-! {correct} / {TEST_CNT} !-> TOTAL")


extract()
judge()

