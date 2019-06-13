
import os

instances = ['100','200','300','400','500','600','700','800',]
variacao = ['TS_DIVERSIFICATION_INFLUENCE','TS_INFLUENCE','TS_SURROGATE_INFLUENCE']
seed = ['10','15','20','25',]


for v in variacao:			
	for x in instances:
		for s in seed:
			print "Iniciou a instacia " + x + " com a variacao "+v + "com o k = " +s
			os.system("java -jar "+v+".jar  " +x +" "+ s + " > Resultados/"+v+"/instance"+x+"-"+s+".txt")
			print "Terminou a instancia " + x +"com o k = " +s
		
print("FIM")
