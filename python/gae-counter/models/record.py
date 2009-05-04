#!/usr/bin/env python
# -*- coding: utf-8 -*-

from google.appengine.ext import db


class AccessRecord(db.Model):
    count       = db.IntegerProperty()
    datetime    = db.DateTimeProperty(auto_now_add = True)
    referer     = db.LinkProperty()
    user_agent  = db.StringProperty()
    remote_addr = db.StringProperty()
    
