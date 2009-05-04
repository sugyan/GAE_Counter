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


class ConfigHandler(webapp.RequestHandler):
    """
    カウンターの各種設定を行う
    """

    @login_required
    def get(self):
        try:
            user = users.get_current_user()
            # requestのkeyに対応するCounterを取得
            counter = Counter.get(db.Key(encoded = self.request.get('key')))
            # 現在のユーザーと関連づけられていなければ403エラー
            if counter == None or user != counter.user:
                self.display_error(403)
                return
            template_values = {
                'user'     : user,
                'user_url' : users.create_logout_url('/'),
                'counter'  : counter,
                }
            path = os.path.join(os.path.dirname(__file__), os.pardir, 'templates', 'config.html')
            self.response.out.write(template.render(path, template_values))
        # requestのkeyが正しくない場合
        except BadKeyError, error:
            logging.error(str(error))
            self.display_error(400)

    def display_error(self, code):
        """
        エラーコードに対応したエラー画面を出力
        """
        self.error(code)
        template_values = {
            'status_code' : code,
            'message'     : webapp.Response.http_status_message(code)
            }
        path = os.path.join(os.path.dirname(__file__), os.pardir, 'templates', 'error.html')
        self.response.out.write(template.render(path, template_values))
