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
from models.image import NumberImage


class DestroyHandler(webapp.RequestHandler):
    """
    カウンターを削除する
    """

    def get(self):
        """
        GETは404とする
        """
        self.display_error(404)

    def post(self):
        """
        requestからkeyを受け取り、対応するCounterのentityを削除する
        """
        try:
            # 削除する対象を取得
            counter = Counter.get(db.Key(encoded = self.request.get('key')))
            # 現在のユーザーと関連づけられていなければ403エラー
            if counter == None or counter.user != users.get_current_user():
                self.display_error(403)
                return
            # transactionで関連画像とともに一括削除
            # transaction使う必要ない？
            db.run_in_transaction(self.destroy_counter, counter.key())
            self.redirect('/')
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

    def destroy_counter(self, key):
        """
        指定されたkeyのentityを削除する
        """
        counter = Counter.get(key)
        # entityが持つ画像データもすべて削除する
        for image in counter.image:
            NumberImage.get(image).delete()
        counter.delete()
