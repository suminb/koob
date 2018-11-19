import React, { Component } from 'react';
import Calendar from './Calendar';
import './App.css';

import 'semantic-ui-css/semantic.min.css';


class App extends Component {
  render() {
    return (
      <div className="App">
        <Calendar></Calendar>
      </div>
    );
  }
}

export default App;
