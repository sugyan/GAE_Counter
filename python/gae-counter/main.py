#!/usr/bin/env python
# -*- coding: utf-8 -*-
#
# Copyright 2007 Google Inc.
#
# Licensed under the Apache License, Version 2.0 (the "License");
# you may not use this file except in compliance with the License.
# You may obtain a copy of the License at
#
#     http://www.apache.org/licenses/LICENSE-2.0
#
# Unless required by applicable law or agreed to in writing, software
# distributed under the License is distributed on an "AS IS" BASIS,
# WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
# See the License for the specific language governing permissions and
# limitations under the License.
#

import logging
import os
import wsgiref.handlers
from google.appengine.api import users
from google.appengine.ext import webapp
from google.appengine.ext.webapp import template
from common import responses
from common import templatefilters
from counter.config import ConfigHandler
from counter.create import CreateHandler
from counter.destroy import DestroyHandler
from counter.viewer import ViewHandler
from models.counter import Counter


class MainHandler(webapp.RequestHandler):
    """
    ホーム画面の処理
    """
    def get(self):
        """
        ログインしていればユーザーの管理画面へ、
        ログインしていなければトップ画面へ
        """
        user = users.get_current_user()
        if user:
            user_url = users.create_logout_url(self.request.uri)
            counters = Counter.all().filter('user = ', user)
            counter  = {
                'counters': counters,
                'can_create': True if counters.count() < 3 else False,
                }
        else:
            user_url = users.create_login_url(self.request.uri)
            counter  = None
        template_values = {
            'counter'  : counter,
            'user'     : user,
            'user_url' : user_url,
            }
        path = os.path.join(os.path.dirname(__file__), 'templates', 'index.html')
        self.response.out.write(template.render(path, template_values))

        
class NotFoundHandler(webapp.RequestHandler):
    """
    処理すべきURL以外のものは404とする
    """
    def get(self):
        responses.display_error(self, 404)
        

def main():
    logging.getLogger().setLevel(logging.DEBUG)
    # カスタムフィルタの追加
    webapp.template.register_template_library('common.templatefilters')
    application = webapp.WSGIApplication([
            ('/',        MainHandler),
            ('/create',  CreateHandler),
            ('/destroy', DestroyHandler),
            ('/config',  ConfigHandler),
            ('/view',    ViewHandler),
            ('/.*',      NotFoundHandler)
            ], debug=True)
    wsgiref.handlers.CGIHandler().run(application)


if __name__ == '__main__':
    main()
