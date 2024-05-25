import './App.css';
import { useEffect, useState } from "react";
import React from "react";

function App() {
  const [data, setData] = useState([]);

  useEffect(() => {
    fetch("/chat")
      .then((res) => {
        if (res.redirected) {
          window.location.href = res.url; // 리다이렉트된 URL로 이동
        } else {
          return res.json();
        }
      })
      .then(function (result) {
        if (result) {
          setData(result);
        }
      })
      .catch((error) => {
        console.error('Error:', error);
      });
  }, []);

  return (
    <div className="App">
      <header className="App-header">
        <img src="/logo.png" className="App-logo" alt="logo" />
        <p>
          Edit <code>src/App.js</code> and save to reload.
        </p>
        <a
          className="App-link"
          href="https://reactjs.org"
          target="_blank"
          rel="noopener noreferrer"
        >
          Learn React
        </a>
        <ul>
          {data.map((v, idx) => <li key={`${idx}-${v}`}>{v}</li>)}
        </ul>
      </header>
    </div>
  );
}

export default App;
