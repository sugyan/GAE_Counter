#!/usr/bin/env python
# -*- coding: utf-8 -*-

import logging
from google.appengine.api import images
from google.appengine.api.datastore_errors import BadKeyError
from google.appengine.ext import db
from google.appengine.ext import webapp
from common import responses
from models.counter import Counter
from models.image import NumberImage
from models.record import AccessRecord


class CounterHandler(webapp.RequestHandler):
    """
    カウンターの画像を表示する
    """
    def get(self):
        try:
            # パスの解析
            name =  self.request.path.split('/')[2]
            (key, type) = name.split('.')
            key = db.Key(key)
            output_encoding = [images.PNG, images.JPEG][['png', 'jpg'].index(type)]
            # カウンターの取得
            counter = Counter.get(key)
            if not counter:
                responses.display_error(self, 404)
                return
            logging.debug(counter.count)
            # アクセス履歴情報の取得
            record = {
                'referer'     : self.request.headers.get('Referer'),
                'user_agent'  : self.request.headers.get('User-Agent'),
                'remote_addr' : self.request.remote_addr,
                }
            # カウントのインクリメント
            result = db.run_in_transaction(self.increment_count, counter.key(), record)
            count = Counter.get(result).count
            # 新しいカウントを桁毎に区切る
            digits = []
            while count / 10 != 0:
                digits.append(count % 10)
                count /= 10
            digits.append(count)
            # 使用する画像データの読み込み
            image_data = {}
            for number_image in NumberImage.all().ancestor(key).filter('number in', digits):
                logging.debug(number_image.number)
                image_data[number_image.number] = number_image.data
            # 合成するデータの決定
            image_list = []
            offset = 0
            for i in reversed(digits):
                image_list.append((image_data[i], offset, 0, 1.0, images.TOP_LEFT))
                offset += 64
            # 合成して出力
            image = images.composite(image_list, offset, 128, output_encoding=output_encoding)
            if output_encoding == images.PNG:
                self.response.headers['Content-Type'] = 'image/png'
            elif output_encoding == images.JPEG:
                self.response.headers['Content-Type'] = 'image/jpeg'
            self.response.out.write(image)
        except ValueError, error:
            logging.error('invalid path')
            responses.display_error(self, 404)
        except BadKeyError, error:
            logging.error(str(error))
            responses.display_error(self, 404)

    def increment_count(self, key, record):
        """
        transactionでアクセスカウントを増やす
        """
        counter = Counter.get(key)
        counter.count += 1
        access_record = AccessRecord(
            parent      = counter,
            count       = counter.count,
            referer     = record['referer'],
            user_agent  = record['user_agent'],
            remote_addr = record['remote_addr'],
            )
        access_record.put()
        return counter.put()
