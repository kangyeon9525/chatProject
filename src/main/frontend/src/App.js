import React, { useState, useEffect, useRef } from 'react';
import 'bootstrap/dist/css/bootstrap.min.css';
import './App.css';

const App = () => {
  const [username, setUsername] = useState('');
  const [message, setMessage] = useState('');
  const [chatMessages, setChatMessages] = useState([]);
  const [connected, setConnected] = useState(false);
  const websocket = useRef(null);

  const handleMessageReceive = (event) => {
    const newMessage = event.data;
    // 시스템 메시지는 별도로 처리하지 않음
    if (!newMessage.startsWith('(시스템)')) {
      setChatMessages((prevMessages) => [...prevMessages, newMessage]);
    }
  };

  const handleOpen = () => {
    const msg = `(시스템) *${username}가 채팅방에 입장하셨습니다*`;
    setChatMessages((prevMessages) => [...prevMessages, msg]);
    setConnected(true);
  };

  const handleClose = () => {
    setChatMessages((prevMessages) => [...prevMessages, '(시스템) 연결이 종료되었습니다']);
    setConnected(false);
  };

  const sendMessage = () => {
    if (message) {
      const msg = `${username}: ${message}`;
      websocket.current.send(msg);
      setMessage('');
    }
  };

  const leaveChat = () => {
    if (websocket.current) {
      const msg = `(시스템) *${username}가 채팅방에서 나갔습니다*`;
      websocket.current.send(msg);
      websocket.current.close();
      setChatMessages((prevMessages) => [...prevMessages, msg]);
    }
  };

  const handleSubmit = (e) => {
    e.preventDefault();
    websocket.current = new WebSocket('ws://localhost:8080/ws/chat');
    websocket.current.onmessage = handleMessageReceive;
    websocket.current.onopen = handleOpen;
    websocket.current.onclose = handleClose;
  };

  useEffect(() => {
    return () => {
      if (websocket.current) {
        websocket.current.close();
      }
    };
  }, []);

  return (
      <div className="container mt-5">
        {!connected ? (
            <form onSubmit={handleSubmit} className="card p-4 shadow">
              <div className="mb-3">
                <label htmlFor="username" className="form-label">닉네임 입력:</label>
                <input
                    type="text"
                    className="form-control"
                    id="username"
                    value={username}
                    onChange={(e) => setUsername(e.target.value)}
                    required
                />
              </div>
              <button type="submit" className="btn btn-primary">채팅방 입장</button>
            </form>
        ) : (
            <div className="card p-4 shadow">
              <button className="btn btn-secondary mb-3" onClick={leaveChat}>채팅방 나가기</button>
              <div className="chat-box col-12">
                <div id="msgArea" className="chat-area">
                  {chatMessages.map((msg, index) => (
                      <div key={index} className="chat-message">
                        {!msg.startsWith('(시스템)') && <img src="/maha.jpg" alt="User" />}
                        <span>{msg}</span>
                      </div>
                  ))}
                </div>
                <div className="input-group mb-3 mt-3">
                  <input
                      type="text"
                      id="msg"
                      className="form-control"
                      placeholder="메세지 입력하세요"
                      value={message}
                      onChange={(e) => setMessage(e.target.value)}
                      aria-label="Message"
                  />
                  <button className="btn btn-outline-secondary" type="button" onClick={sendMessage}>전송</button>
                </div>
              </div>
            </div>
        )}
      </div>
  );
};

export default App;















