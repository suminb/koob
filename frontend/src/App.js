import React, { Component } from 'react';
import ReservationForm from './ReservationForm.js';
import TimeTable from './TimeTable.js';
import './App.css';

class App extends Component {
  render() {
    return (
      <div className="App">
        <ReservationForm></ReservationForm>
        <TimeTable></TimeTable>
      </div>
    );
  }
}

export default App;
