#!/usr/bin/env python
# -*- coding: utf-8 -*-

from google.appengine.ext import db


class Counter(db.Model):
    name  = db.StringProperty()
    user  = db.UserProperty(auto_current_user_add = True)
    date  = db.DateTimeProperty(auto_now_add = True)
    count = db.IntegerProperty(default = 0)
    image = db.ListProperty(db.Key)
