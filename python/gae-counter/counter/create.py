#!/usr/bin/env python
# -*- coding: utf-8 -*-

import cgi
import logging
import os
from google.appengine.api import users
from google.appengine.ext import webapp
from google.appengine.ext.webapp import template
from models.counter import Counter


class CreateHandler(webapp.RequestHandler):
    def get(self):
        code = 404
        self.error(code)
        template_values = {
            'status_code' : code,
            'message'     : webapp.Response.http_status_message(code)
            }
        path = os.path.join(os.path.dirname(__file__), os.pardir, 'templates', 'error.html')
        self.response.out.write(template.render(path, template_values))

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
