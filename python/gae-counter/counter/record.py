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
from common import responses
from models.counter import Counter
from models.record import AccessRecord


class RecordHandler(webapp.RequestHandler):
    """
    カウンターのアクセス記録を表示する
    """
    
    @login_required
    def get(self):
        try:
            user = users.get_current_user()
            # requestのkeyに対応するCounterを取得
            counter = Counter.get(db.Key(encoded = self.request.get('key')))
            # 現在のユーザーと関連づけられていなければ403エラー
            if counter == None or user != counter.user:
                responses.display_error(self, 403)
                return
            template_values = {
                'user'     : user,
                'user_url' : users.create_logout_url('/'),
                'counter'  : counter,
                'records'  : AccessRecord.all().ancestor(counter),
                }
            path = os.path.join(os.path.dirname(__file__), os.pardir, 'templates', 'record.html')
            self.response.out.write(template.render(path, template_values))
        # requestのkeyが正しくない場合
        except BadKeyError, error:
            logging.error(str(error))
            responses.display_error(self, 400)
