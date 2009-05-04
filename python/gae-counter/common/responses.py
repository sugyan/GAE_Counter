#!/usr/bin/env python
# -*- coding: utf-8 -*-

import os
from google.appengine.ext import webapp
from google.appengine.ext.webapp import template


def display_error(request_handler, code):
    """
    エラーコードに対応したエラー画面を出力
    """
    request_handler.error(code)
    template_values = {
        'status_code' : code,
        'message'     : webapp.Response.http_status_message(code)
        }
    path = os.path.join(os.path.dirname(__file__), os.pardir, 'templates', 'error.html')
    request_handler.response.out.write(template.render(path, template_values))
