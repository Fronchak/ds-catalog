import Navbar from './components/Navbar';
import React from 'react';
import ReactDOM from 'react-dom/client';
import App from './App';
import { BrowserRouter , Route, Switch } from 'react-router-dom';
import Home from './pages/Home';

const root = ReactDOM.createRoot(
  document.getElementById('root') as HTMLElement
);
root.render(
  <React.StrictMode>
      <BrowserRouter>
        <Navbar />
        <Switch >
          <Route path="/">
            <Home />
          </Route>
        </Switch>
      </BrowserRouter>
  </React.StrictMode>
);

