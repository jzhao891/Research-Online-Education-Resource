from google import Google
import types
import json
import os


Dir='/Users/jessicazhao/Documents/workspace/workspace-python/data/'
if not os.path.exists(Dir):
    os.makedirs(Dir)

api=Google()
query='Human Computer Interactive Syllabus'
page_num=21
searchresult=api.search(query,page_num)

#write links into files
filename=query.replace(" ","_")
with open(Dir+filename+'.csv','w') as f:
    f.write('ID,link\n')
    if(type(searchresult)!=types.NoneType):
        len=searchresult.__len__()
        cont=0
        for result in searchresult:
            cont=cont+1
            f.write(str(cont)+','+result.link+'\n')
#with open(Dir+filename+'.json','w') as jsonFile:
#    jsonFile.write('{\n"links":[\n')
#    if(type(searchresult)!=types.NoneType):
#        len=searchresult.__len__()
#        cont=0
#        for result in searchresult:
#            cont=cont+1
#            link={
#                "ID":cont,
#                "link":result.link,
#                "name":result.name
#            }
#            json.dump(link,jsonFile)
#            if(cont!=len):
#                jsonFile.write(',\n')
#    jsonFile.write(']}')



