from server.config import Config
from flask import Flask, app
from flask_socketio import SocketIO

app = Flask(__name__)
app.config.from_object(Config)

socketio = SocketIO(app)

from server import routes, errors