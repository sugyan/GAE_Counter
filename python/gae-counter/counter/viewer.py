#!/usr/bin/env python
# -*- coding: utf-8 -*-

import logging
from google.appengine.ext import db
from google.appengine.ext import webapp
from common import responses
from models.image import NumberImage


class ViewHandler(webapp.RequestHandler):
    def get(self):
        try:
            num = int(self.request.get('num'))
            key = db.Key(self.request.get('key'))
            results = NumberImage.all().ancestor(key).filter('number = ', num)
            if results.count() != 1:
                responses.display_error(self, 404)
                return
            self.response.headers['Content-Type'] = "image/png"
            self.response.out.write(results[0].data)
        except Exception, exception:
            logging.error(str(exception))
            responses.display_error(self, 404)
