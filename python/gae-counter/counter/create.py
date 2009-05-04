#!/usr/bin/env python
# -*- coding: utf-8 -*-

import cgi
import logging
from google.appengine.api import users
from google.appengine.ext import webapp
from models.counter import Counter


class CreateHandler(webapp.RequestHandler):
    def get(self):
        pass

    def post(self):
        if not users.get_current_user():
            logging.error('not signed in user')
            self.redirect('/')
            return
        name = cgi.escape(self.request.get('name')[:100])
        if name == '':
            name = 'No name'
        counter = Counter(name = name)
        key = counter.put()
        logging.debug("key: " + str(key))
        self.redirect('/')
