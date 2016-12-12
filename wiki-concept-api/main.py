from category import Category as Cate
from wikiExtracter import Dump
import os
import urllib2
from concept import ConceptPicker
import sys
reload(sys)
sys.setdefaultencoding('utf8')#change the default encoding of the whole script to be 'UTF-8', avoiding encoding problem
#print Cate.category[1]
concepts=dict()






dump_root='http://dumps.wikimedia.your.org/'
filename='enwiki/20161120/enwiki-20161120-pages-meta-current1.xml-p000000010p000030303.bz2'
dump_path=os.path.join(dump_root, filename)

zipFile=urllib2.urlopen(dump_path)
name=os.path.split(dump_path)[1]
program_root=os.path.abspath('')
dump_path=os.path.join(program_root,'tmp/'+name)
print dump_path
tempzip = open(dump_path, "wb")
tempzip.write(zipFile.read())
tempzip.close()
#print dump_path
download=Dump(dump_path,True)
download.get_raw()
pathConcept=os.path.join(program_root,'concept/allconcepts.json')
conceptpicker=ConceptPicker()
with open(pathConcept,'a') as f:
    f.write('[\n')
    for title in download.articles:
        concepts[title] = conceptpicker.getConcept(download.articles[title]);
        f.write('{\"title\":\"'+title+'\",\"concepts\":\"'+','.join(concepts[title])+'\"},\n')

    f.write('{}]')

download.close()

