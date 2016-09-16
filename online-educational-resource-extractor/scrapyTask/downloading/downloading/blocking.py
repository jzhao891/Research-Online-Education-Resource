import urllib2
from BeautifulSoup import BeautifulSoup 
import os
import re

activeAddress=['http://ocw.mit.edu/courses/electrical-engineering-and-computer-science/6-830-database-systems-fall-2010/readings/','https://usfca.instructure.com/courses/1215994/assignments/syllabus']
inactAddress=['http://www.sqlcourse.com/','https://www.class-central.com/mooc/1580/stanford-openedx-db-introduction-to-databases']
#xpath_syllabus=[]
keywords_table=['date','week','chap','schedule','reading','topic','lecture']
#negtivewords=['homework','quiz']
positivewords=['introduction']
class Block():
    def __init__(self):
        self.significant=0
        self.document_html=[]
    def camparator(self,block):
        if self.significant>=block.significant:
            return 1
        else:
            return 0
    def printer_toFile(self,filepath):
        if os.path.isfile(filepath):
            with open(filepath,'a') as f:
                f.write('\nDoc Start.... the significant is '+str(self.significant)+'\n')
                for doc in self.document_html:
                    f.write(doc+'\n')
        else:
            with open(filepath,'w') as f:
                f.write('\nDoc Start.... the significant is '+str(self.significant)+'\n')
                for doc in self.document_html:
                    f.write(doc+'\n')
                    
class Blocking():
    def __init__(self):
        self.tablelis=[]
        self.ullis=[]
        self.ollis=[]


    def analyze_page(self):
        for add in activeAddress:
            #rq=urllib2.Request(add)
            rq = urllib2.Request('https://www.cs.utexas.edu/~mooney/ir-course/')
            rq.add_header("User-Agent", "Mozilla/5.001 (windows; U; NT4.0; en-US; rv:1.0) Gecko/25250101")
            html = urllib2.urlopen(rq)
            soup = BeautifulSoup(html)
            body=soup.find('body')#use when need to process other type of block
            tablelis = soup.findAll("table")
            ullis = soup.findAll("ul")
            ollis = soup.findAll("ol")
            
            ####################
            ### Processing table
            ####################
            blocklist = []
            print 'processing tables....\n'
            if not tablelis:
                print 'no table\n'
            else:
                cont=0
                
                for table in tablelis:
                    
                    cont=cont+1
                    print '\ntable '+str(cont)+' is being analyzing....\n'
                    block=Block()
                    block.document_html=table
                    #cont=cont+1
                    
                    #############################################
                    ###calculate how many rows the table contains
                    #############################################
                    list_tr=table.findAll('tr')
                    if len(list_tr)>=8:
                        print 'the row number of this table is larger than 8...\n'
                        block.significant=block.significant+1
                    
                    #############################################################
                    ### detect if the table title contains keywords of a syllabus
                    #############################################################
                    first_tr = table.find('tr').findAll(text=True)
                    flag=False
                    for tx in first_tr:
                        #print tx
                        tx_lower=tx.lower()
                        for word in keywords_table:
                            if word in tx_lower:
                                flag=True
                    if flag:
                        print 'this table contains keywords...\n'
                        block.significant=block.significant+1
                    
                    #################################################    
                    ### detect if all contents contain positive words
                    #################################################
                    contentlist=table.findAll('td',text=True)
                    flag=False
                    for content in contentlist:
                        for word in positivewords:
                            if word in content.lower():
                                flag=True
                    if flag:
                       print 'this table contains positive words...\n' 
                       block.significant=block.significant+1
                            
                    blocklist.append(block)
                    
            ######################################################
            ### sort block list according to the significant value
            ######################################################
            blocklist=sorted(blocklist,key=lambda block:block.significant,reverse=True)
            print blocklist[0].document_html
            with open('/Users/jessicazhao/Documents/workspace/scrapyTask/downloading/data/temporarydata.html','a') as f:
                #f.write('\n<h2>the syllabus in test </h2>\n')
                f.write('\n<h2>the syllabus in '+add+'</h2>\n')
            if blocklist[0].significant!=0:
                with open('/Users/jessicazhao/Documents/workspace/scrapyTask/downloading/data/temporarydata.html',
                          'a') as f:
                    f.write('\n<h3> table</h3>\n')
                    f.write(str(blocklist[0].document_html))

            ###################
            ###processing ul/ol
            ###################
            blocklist=[]
            self.processing_ulol(ullis,blocklist)
            self.processing_ulol(ollis,blocklist)
            ######################################################
            ### sort block list according to the significant value
            ######################################################
            blocklist = sorted(blocklist, key=lambda block: block.significant, reverse=True)
            print blocklist[0].document_html
            if blocklist[0].significant!=0:
                with open('/Users/jessicazhao/Documents/workspace/scrapyTask/downloading/data/temporarydata.html',
                          'a') as f:
                    f.write('\n<h3> ul/ol</h3>\n')
                    f.write(str(blocklist[0].document_html))

        with open('/Users/jessicazhao/Documents/workspace/scrapyTask/downloading/data/temporarydata.html','a') as f:
            f.write('\n</body>\n</html>\n')
  
    def processing_ulol(self,lis,blocklist):
        print 'Processing ul......\n'
        
        if not lis:
            print 'no ul/ol!\n'
        else:
            cont=0
            for ulol in lis:
                cont=cont+1
                print '\nul/ol ' + str(cont) + ' is being analyzing....\n'
                block = Block()
                block.document_html = ulol

                #############################################
                ###calculate how many rows the table contains
                #############################################
                list_tr = ulol.findAll('li')
                if len(list_tr) >= 8:
                    print 'the row number of this ul/ol is larger than 8...\n'
                    block.significant = block.significant + 1

                #################################################
                ### detect if all contents contain positive words
                #################################################
                contentlist = ulol.findAll('li', text=True)
                flag = False
                for content in contentlist:
                    for word in positivewords:
                        if word in content.lower():
                            flag = True
                if flag:
                    print 'this ul/ol contains positive words...\n'
                    block.significant = block.significant + 1

                blocklist.append(block)