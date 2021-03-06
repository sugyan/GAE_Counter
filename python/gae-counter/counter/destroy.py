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
from models.image import NumberImage
from models.record import AccessRecord


class DestroyHandler(webapp.RequestHandler):
    """
    カウンターを削除する
    """

    def get(self):
        """
        GETは404とする
        """
        responses.display_error(self, 404)

    def post(self):
        """
        requestからkeyを受け取り、対応するCounterのentityを削除する
        """
        try:
            # 削除する対象を取得
            counter = Counter.get(db.Key(encoded = self.request.get('key')))
            # 現在のユーザーと関連づけられていなければ403エラー
            if counter == None or counter.user != users.get_current_user():
                responses.display_error(self, 403)
                return
            # 関連画像、アクセス履歴ともに一括削除
            for image in counter.image:
                NumberImage.get(image).delete()
            for record in AccessRecord.all().ancestor(counter):
                record.delete()
            counter.delete()
            self.redirect('/')
        # requestのkeyが正しくない場合
        except BadKeyError, error:
            logging.error(str(error))
            responses.display_error(self, 400)
