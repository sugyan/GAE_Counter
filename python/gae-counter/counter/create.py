#!/usr/bin/env python
# -*- coding: utf-8 -*-

from google.appengine.ext import webapp


class CreateHandler(webapp.RequestHandler):
    def get(self):
        pass

    def post(self):
        self.response.out.write("create")
