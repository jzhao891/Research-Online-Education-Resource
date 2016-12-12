
import xml.etree.ElementTree as etree
from category import Category
import re
file=open('/Users/jessicazhao/Downloads/enwiki-20161120-pages-meta-current1.xml').read()
et=etree.fromstring(file)
pages=et.findall('{http://www.mediawiki.org/xml/export-0.10/}page')
for page in pages:
    text = page.find('{http://www.mediawiki.org/xml/export-0.10/}revision').find(
        '{http://www.mediawiki.org/xml/export-0.10/}text').text
    #print text
    category_link = re.compile(r"\[\[Category:.+?\]\]")
    list = category_link.findall(text)
    cate=[]
    print list.__len__()
    for item in list:
        len = item.__len__()
        cate.append(item[11:len - 2])
    for c in cate:
        #print c
        if c in Category.category:
            print c


