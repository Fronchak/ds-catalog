import Home from './pages/Home';
import React from 'react';
import { BrowserRouter, Route, Switch } from "react-router-dom";
import Navbar from './components/Navbar';


const Routes = () => {

  return (
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
}

export default Routes;
