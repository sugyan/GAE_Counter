#!/usr/bin/env python
# -*- coding: utf-8 -*-

import cgi
import logging
import os
from google.appengine.api import users
from google.appengine.ext import db
from google.appengine.ext import webapp
from google.appengine.ext.webapp import template
from common import responses
from models.counter import Counter
from models.image import NumberImage


class CreateHandler(webapp.RequestHandler):
    """
    カウンターを作成する
    """

    def get(self):
        """
        GETは404とする
        """
        responses.display_error(self, 404)

    def post(self):
        # ログインしているユーザーのみ作成可能
        if not users.get_current_user():
            logging.error('not signed in user')
            self.redirect('/')
            return
        # カウンター名は表示に使うのでエスケープする
        # 長さも勝手に100文字程度に制限。あとでformに明記する
        name = cgi.escape(self.request.get('name')[:100])
        if name == '':
            name = 'No name'
        # transactionを使うべきなのか？
        counter = Counter(name = name)
        counter_key = counter.put()
        # デフォルトの画像を用意する
        for i in range(10):
            path = os.path.join(os.path.dirname(__file__), os.pardir, 'images', str(i) + '.png')
            number = NumberImage(
                parent = counter_key,
                number = i,
                data   = open(path).read(),
                )
            key = number.put()
            # 削除するときに参照できるようListで保持させる
            counter.image.append(key)
        counter.put()
        self.redirect('/')
