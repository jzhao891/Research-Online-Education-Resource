"""
Model of a wikipedia dump
"""

import bz2
import logging
import os
import tempfile
from common import Timer
from category import Category as Cate
import xml.etree.ElementTree as etree
import re
import urllib2
from io import BytesIO


class Dump:
    logger=logging.getLogger('wikiExtracter.model.Dump')
    articles=dict()
    def __init__(self,dump_path,unpack=False):
        #zipFile=urllib2.urlopen(dump_path)

        self.dump_path=os.path.abspath(dump_path)
        #print self.dump_path
        base, ext=map(str.lower,os.path.splitext(dump_path))
        if ext=='.bz2':
            #Need to compress the dump file
            if unpack:
                #steam=zipFile.read()
                # unpack the .bz2 file to a tempfile beforehand.
                #with bz2.decompress(BytesIO(steam).read()) as dump_bz2:
                with bz2.BZ2File(self.dump_path) as dump_bz2:
                    dump_txt = tempfile.TemporaryFile();
                    self.logger.info("unpacking %s to a temparary file", self.dump_path)
                    self.logger.debug("Tempdir is %s",tempfile.tempdir)
                    while True:
                        chunk=dump_bz2.read(1024*1024*10) # read in 10MB chunks
                        if chunk:
                            dump_txt.write(chunk)
                        else:
                            break
               # self.logger.info("Done unpacking (took %.1fs)",t.elapsed)
                self.dump_file=dump_txt
                dump_txt.seek(0)
            else:
                self.dump_file=open(self.dump_path)


            self.logger.info("============================================")
            self.logger.info("Loading data for %s", self.dump_path)

    def get_raw(self):
#
#
#
#
#

        self.logger.info("extract content from wiki dump....")
        self.dump_file.seek(0)
        xmlstring=self.dump_file.read()
        self.doms=etree.fromstring(xmlstring)
        allpages=self.doms.findall('{http://www.mediawiki.org/xml/export-0.10/}page')
        index=0;
        self.selectedpages=[]
        for page in allpages:
            index+=1;
            text=page.find('{http://www.mediawiki.org/xml/export-0.10/}revision').find('{http://www.mediawiki.org/xml/export-0.10/}text').text
            title=page.find('{http://www.mediawiki.org/xml/export-0.10/}title').text.encode('utf8')
            categories=self.get_category(text)
            if categories.__len__()!=0:
                for cate in categories:
                    if cate in Cate.category:
                        self.articles[title]=text


    def get_category(self,content):
        list=[]
        category_link = re.compile(r"\[\[Category:.+?\]\]")
        catelist = category_link.findall(content)
        for item in catelist:
            len = item.__len__()
            list.append(item[11:len - 2])
        return list
    def close(self):
        self.dump_file.close()