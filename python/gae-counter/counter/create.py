#!/usr/bin/env python
# -*- coding: utf-8 -*-

import cgi
import logging
import os
from google.appengine.api import users
from google.appengine.ext import db
from google.appengine.ext import webapp
from google.appengine.ext.webapp import template
from models.counter import Counter
from models.image import NumberImage


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
        # transactionを使うべきなのか？
        counter = Counter(name = name)
        counter_key = counter.put()
        for i in range(10):
            number = NumberImage(
                parent = counter_key,
                number = i,
                )
            key = number.put()
            counter.image.append(key)
        counter.put()
        
        self.redirect('/')
