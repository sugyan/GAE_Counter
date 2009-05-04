#!/usr/bin/env python
# -*- coding: utf-8 -*-

import datetime
from google.appengine.ext import webapp
from google.appengine.ext.webapp import template


class UtcTzinfo(datetime.tzinfo):
    def utcoffset(self, dt):
        return datetime.timedelta(0)


class JstTzinfo(datetime.tzinfo):
    def utcoffset(self, dt):
        return datetime.timedelta(hours = 9)
    def dst(self, dt):
        return datetime.timedelta(0)


def timeJST(value):
    value = value.replace(tzinfo = UtcTzinfo()).astimezone(JstTzinfo()) 
    return value


register = webapp.template.create_template_register()
register.filter(timeJST)
