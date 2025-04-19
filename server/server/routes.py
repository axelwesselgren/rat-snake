from flask import render_template, request, session, redirect, url_for, jsonify
from flask_login import current_user, login_required
from flask_socketio import emit
from server import app
from server.sockets import clients

@app.route("/logged_in")
def check_login_status():
    return jsonify(is_authenticated=session.get('logged_in', False))

@app.route("/")
def index():
    if is_logged_in():
        emit('hearbeat', broadcast=True)
        return render_template('controlpanel.html')
    return render_template('index.html')

@app.route("/login", methods=['POST'])
def login():
    username = request.form['username']
    password = request.form['password']

    if username == 'admin' and password == '123':
        session['logged_in'] = True
        return redirect(url_for('control_panel'))
    return redirect(url_for('index'))    
    
@app.route("/control-panel")
def control_panel():
    if is_logged_in():
        return render_template('controlpanel.html', snakes=clients.values())
    return redirect(url_for('index'))

@app.route("/logout")
def logout():
    if is_logged_in():
        session['logged_in'] = False
    return redirect(url_for('index'))

def is_logged_in():
    return session.get('logged_in', False)