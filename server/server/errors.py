from flask import render_template, url_for, redirect
from server import app

@app.errorhandler(405)
def method_not_allowed(error):
        return redirect(url_for("index"))

@app.errorhandler(404)
def method_not_allowed(error):
        return redirect(url_for("index"))

@app.errorhandler(401)
def method_not_allowed(error):
        return redirect(url_for("index"))