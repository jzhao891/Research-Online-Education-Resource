import scrapy
from downloading.items import DownloadingItem
import simplejson
import os

class gettructure(scrapy.Spider):
    name = "downloading"
    root="/Users/jessicazhao/Documents/workspace/workspace-python/data/summary(noHTML)"
    filenamelist=os.listdir(root)
    keywords=['textbook','prerequistes','book','Overview','introduction']
    #allowed_domains = []
    #start_url = []
    #xpath_Map=dict()
    #for filename in filenamelist:
    #    filepath=root+filename;
    #    with open(filepath,'r') as f:
    #        data=simplejson.load(f)
    #        list=data['list']
    #        for link in list:
                # processing address
    #            address=link['link']
    #            domain=address.split('://')[1].split('/')[0]
    #            allowed_domains.append(domain)
    #            start_url.append(address)
    #            xpath[address]=link['xpath']
    allowed_domains = ["courses.ischool.berkeley.edu"]
    start_urls = ['http://courses.ischool.berkeley.edu/i240/s13/schedule.php']

    def parse(self, response):  # //*[@id="selectedcontent"]/div[2]/ul/li[1]
        item = DownloadingItem()
        rootpath = "//*[@id=\"content\"]/ol"
        # if "ol" in rootpath:
        rootnode = response.xpath(rootpath)
        #lis = rootnode.xpath('li')
        #
        item['coursetitle'] = "IR"
        fatherlist=[]
        chapterlist=[]
        with open('/Users/jessicazhao/Documents/workspace/workspace-python/data/summary(noHTML)/intermediaResult.txt',
                  'w') as f:
            #chapterlist.append(rootnode.xpath('li//text()').extract())
            lis=rootnode.xpath('li')
            for li in lis:
                chapterlist.append(li.xpath('.//text()').extract()[0])
                #chapterlist.append(li.xpath('.//text()').extract())
                fatherlist.append(li.extract())

            #fatherlist.append(rootnode.xpath('li').extract())
            item['father']=fatherlist
            item['chapter']=chapterlist
            #for li in lis:
            #    item['father']=li.extract()
             #   item['chapter']=li.xpath('//text()').extract()#f.write(li.xpath('//text()').extract() + '\n\n')
        yield item







