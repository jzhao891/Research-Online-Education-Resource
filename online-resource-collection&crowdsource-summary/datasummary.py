import os
import simplejson
import urllib2
import json


Title="Information_Retrieval"#Information_Retrieval/Java_Programming/Database/Human_Computer_Interaction
root="/Users/jessicazhao/Documents/workspace-java/Linkmark/data/"#summary(noHTML)
originalfilepath='/Users/jessicazhao/Documents/workspace/workspace-python/data/Information_Retrieval_course.json'
summeryfilepath='/Users/jessicazhao/Documents/workspace/workspace-python/data/summary(noHTML)/Information_Retrieval_summary.json'
remarkfilepath='/Users/jessicazhao/Documents/workspace/workspace-python/data/Information_Retrieval_course_remark.json'
items=[]
linkmark=dict()
remark=dict()
list=os.listdir(root)
for filename in list:
    if Title in filename:
        filepath=os.path.join(root,filename)
        #print filename
        with open(filepath,'r') as f:
            data=simplejson.load(f)
            lis= data['list']
            #print lis
            if len(lis)>0:
                for li in lis:
                #if not(linkmark.has_key(item['id']))and not(remark.has_key(item['id'])):
                    content=''
                    if '@id' in li['xpath']:
                        remark[li['id']]=simplejson.dumps(item)
                    else:
                        li['IsCourseweb']=True
                        li['IfContainSyllabus']=True #only IR data need
                        items.append(li)
                        #linkmark[item['id']]=simplejson.dumps(item)
print items
####remark table processing####
string=''
with open(remarkfilepath,'r') as f:
    data=simplejson.load(f)
    listsRemark=data['lists']
    for li in listsRemark:
        li['IsCourseweb']=True
        li['IfContainSyllabus']=True #only IR data need
        if li['revised']==True:
            li["original"]=original[li['id']]
            print li
             
    string=simplejson.dumps(data)
with open(summeryfilepath,'w') as f:
    f.write(string)
### combine non-remark list and remark list
items.extend(listsRemark)    

##generate summary list with non-remark and remark list                                                                                                    
summary=[]
#origninal=dict()
#with open(originalfilepath,'r') as f:
#    links=simplejson.load(f)['links']
#    for link in links:
#        original[link['ID']]=link['link']
#    print original
with open(originalfilepath,'r') as f:
    links=simplejson.load(f)['links']
    for link in links:
        temp=dict()
        for li in items:
            #print li
            if link['ID']==li['id']:
                temp['id']=link['ID']
                temp['IsCourseweb']=li['IsCourseweb']
                temp['revised']=li['revised']
                temp['IfContainSyllabus']=li['IfContainSyllabus']
                temp['link']=li['link']
                if li['revised']==True:
                    temp['original']=link['link']
                temp['xpath']=li['xpath']
        #print len(temp)
        if len(temp)==0:
            temp['id']=link['ID']
            temp['IsCourseweb']=False
            temp['revised']=False
            temp['IfContainSyllabus']=False
            temp['link']=link['link']
            temp['xpath']=''    
        summary.append(temp)
#print len(summary)
string='{"list":'+simplejson.dumps(summary)+'}'
with open(summeryfilepath,'w') as f:
    f.write(string)
with open(summeryfilepath,'r') as f:#test
    simplejson.load(f)
    
    
###add original link(if revised) on ono-remark link###
for id in linkmark:
    data=simplejson.loads(linkmark[id])
    #data['id']
    #lists= linkmark[link].split(',')
    if data['revised']==True:
        str=linkmark[id][0:len(linkmark[id])-1]+',"original":"'+original[data['id']]+'"}'
        linkmark[id]=str

######extract HTML###########
for id in linkmark:
    data=simplejson.loads(linkmark[id])
    linkResp=urllib2.urlopen(data['link'])
    s=linkResp.read().replace('\n','\\n')
    s=s.replace('\r','\\r')
    s=s.replace('\r\n','\\r\\n')
    str=linkmark[id][0:len(linkmark[id])-1]+',"linkHTML":"'+s+'"}'
    linkmark[id]=str
    
    if data['revised']==True:
        oriResp=urllib2.urlopen(data['original'])
        s=oriResp.read().replace('\n','\\n')
        s=s.replace('\r','\\r')
        s=s.replace('\r\n','\\r\\n')
        str=linkmark[id][0:len(linkmark[id])-1]+',"origianlHTML":"'+s+'"}'
        linkmark[id]=str
        
######save as files#####
with open(summeryfilepath,'w') as f:
    f.write('{"links":\n[\n')
    for id in linkmark:
        f.write(linkmark[id]+',\n')
    f.write(']}')
#with open(remarkfilepath,'w') as f:
#    f.write('{"links":\n[\n')
#    for id in remark:
#        f.write(remark[id]+',\n')
#    f.write(']}')
with open(summeryfilepath,'r') as f:
    simplejson.load(f)
    
    
with open(summeryfilepath,'r') as f:
    data=simplejson.loads(f.read(),strict=False)
                           
        
    filepath=os.path.join(root,filename)
    #filepath="/Users/jessicazhao/Documents/workspace-java/Linkmark/data/submit_Information_Retrieval_course-2ba6-4adb-855e-7d7942e46af0.json"
    str=''
    with open(filepath,'r') as f:
        for line in f:
            if '"revised":true"' in line:
                sp=line.split(",")
                if len(sp)>4:
                    line=sp[0]+","+sp[1]+','+sp[2][0:len(sp[2])-1]+','+sp[3]+','+sp[4]
            str=str+line
    with open(filepath,'w') as f:
        f.write(str)
        

filepath="/Users/jessicazhao/Documents/workspace-java/Linkmark/data/submit_Java_Programming_course-6871-4a07-9d91-299e8165f902.json"

str=''
with open(filepath,'r') as f:
    data=simplejson.load(f)
    print data['userid']
    for line in f:
        if '"revised":false"' in line:
                sp=line.split(",")
                if len(sp)>4:
                    line=sp[0]+","+sp[1]+','+sp[2][0:len(sp[2])-1]+','+sp[3]+','+sp[4]
        str=str+line
with open(filepath,'w') as f:
    f.write(str)        
                
        if "link_count" in line:
            sp=line.split(":")
            if int(sp[1])==0:
                lis=str.split("\n")
                s=''
                for li in lis:
                    if "list" in li:
                        li=li+"],"
                    s=s+li+'\n'
                str=s
        str=str+line
with open(filepath,'w') as f:
    f.write(str)
                
            sp[1]='"'+sp[1].split(",")[0]+'",\n'
            line=sp[0]+':'+sp[1]
        str=str+line
        

    

        


