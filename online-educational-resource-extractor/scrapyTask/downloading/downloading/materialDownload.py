import xml.etree.ElementTree as ET
import os
import urllib
from sys import argv,exit,stdout
import time



def reporthook(count,block_size,total_size):
    """

    :param count: nums of downloaded data block
    :param block_size: size of data block
    :param total_size: total size of a file
    :return: standard output shows the download process
    """
    if total_size!=0:
        per = (100.0 * count * block_size) / total_size
        if per > 30.0 and per < 31.0:
            print "Download Percent: %.2f %%\n" % per,
        elif per > 50.0 and per < 51.0:
            print "Download Percent: %.2f %%\n" % per,
        elif per > 100.0:
            print "Download Percent: %.2f %%\n" % per,



    #stdout.write("\r")


def urldownload(url,path,category):
    """

    :param url: download url
    :param path: storage path
    :param category: slide/reading/video
    :param cont: maybe not useful
    :return: standard output shows if the file is downloaded successfully
    """
    temp = url.split("/")
    filename = temp[-1]
    isDone=False
    for f in formatlist:
        if f in filename:
            while not isDone:
                try:
                    stdout.write(category+'('+url+') is downloading....\n')
                    if os.path.exists(path + '/' + category + '_' + filename):
                        break
                    down_log = urllib.urlretrieve(url, path + '/' + category + '_' + filename, reporthook)
                    isDone=True
                except urllib.ContentTooShortError, e:
                    isDone = False
                if not isDone:
                    time.sleep(10)  # sleep 10 seconds
            return path + '/' + category + '_' + filename
    stdout.write(url)
    stdout.write("this resource can't be downloaded! keep the url\n")
    return ''


pathroot="/Users/jessicazhao/Documents/workspace/scrapyTask/downloading/data/material/"
tree=ET.parse("/Users/jessicazhao/Desktop/IR-2.xml")
root=tree.getroot()
formatlist=['.ppt','.pptx','.pdf','.html','.htm']
for chapter in root.iter('chapter'):
    print chapter.text
for course in root:
    print 'course No. '+str(course.get("no"))#get course ID
    #####create course file folder
    coursepath=pathroot+str(course.get("no"))+''
    #print coursepath
    #if not os.path.exists(coursepath):
    #    os.makedirs(coursepath)
    for unit in course.find('units').iter('unit'):
        print 'unit No. '+str(unit[0].text)#get unitNo
        #####create units file folder
        unitpath=coursepath+'/unit'+str(unit[0].text)
        if not os.path.exists(unitpath):
            os.makedirs(unitpath)
        slidepath=unitpath+'/slide'
        for slide in unit.findall('slide_url'):
            slide_url = str(slide.text)
            if slide_url is not None:
                if not os.path.exists(slidepath):
                    os.makedirs(slidepath)
                path=urldownload(slide_url,slidepath,'slide')
                #modify the url to a local file path
                if path!='':
                    #slide.text = path
                    slide.set('updated', 'y')
                    slide.set('localpath',path)
                    #tree.write('/Users/jessicazhao/Desktop/output.xml')
        readingpath=unitpath+'/reading'
        for reading in unit.findall('reading'):
            reading[0].text#reading title
            reading_url=str(reading[1].text)
            if reading_url is not None:
                if not os.path.exists(readingpath):
                    os.makedirs(readingpath)
                path=urldownload(reading_url,readingpath,'reading')
                if path!='':
                    #reading[1].text=path
                    reading.set('updated','y')
                    reading[1].set('localpath',path)
        videopath = unitpath + '/video'
        for video in unit.findall('video'):
            video[0].text  # video title
            video_url = str(video[1].text)
            if video_url is not None:
                if not os.path.exists(videopath):
                    os.makedirs(videopath)
                path = urldownload(video_url, videopath, 'video')
                if path != '':
                    #video[1].text = path
                    video.set('updated', 'y')
                    video[1].set('localpath',path)
        tree.write('/Users/jessicazhao/Desktop/output2.xml')
