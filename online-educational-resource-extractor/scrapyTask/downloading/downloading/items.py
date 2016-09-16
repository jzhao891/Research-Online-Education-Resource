# -*- coding: utf-8 -*-

# Define here the models for your scraped items
#
# See documentation in:
# http://doc.scrapy.org/en/latest/topics/items.html

import scrapy


class DownloadingItem(scrapy.Item):
    # define the fields for your item here like:
    # name = scrapy.Field()
    coursetitle=scrapy.Field()
    description=scrapy.Field()
    preRequest=scrapy.Field()
    chapter=scrapy.Field()
    father=scrapy.Field()
    textbook=scrapy.Field()
    material=scrapy.Field()

    pass
