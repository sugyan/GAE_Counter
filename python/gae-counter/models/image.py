#!/usr/bin/env python
# -*- coding: utf-8 -*-

from google.appengine.ext import db


class NumberImage(db.Model):
    user   = db.UserProperty(auto_current_user_add = True)
    number = db.IntegerProperty()
    data   = db.BlobProperty()
