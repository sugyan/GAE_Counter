#!/usr/bin/env python
# -*- coding: utf-8 -*-

import logging
import os
from google.appengine.api import users
from google.appengine.api.datastore_errors import BadKeyError
from google.appengine.ext import db
from google.appengine.ext import webapp
from google.appengine.ext.webapp import template
from google.appengine.ext.webapp.util import login_required
from models.counter import Counter


class DestroyHandler(webapp.RequestHandler):
    def get(self):
        self.display_error(404)

    def post(self):
        try:
            user = users.get_current_user()
            counter = Counter.get(db.Key(encoded = self.request.get('key')))
            if counter == None or user != counter.user:
                self.display_error(403)
                return
            counter.delete()
            self.redirect('/')
        except BadKeyError, error:
            logging.error(str(error))
            self.display_error(400)

    def display_error(self, code):
        self.error(code)
        template_values = {
            'status_code' : code,
            'message'     : webapp.Response.http_status_message(code)
            }
        path = os.path.join(os.path.dirname(__file__), os.pardir, 'templates', 'error.html')
        self.response.out.write(template.render(path, template_values))
