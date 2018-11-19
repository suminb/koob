import React, { Component } from 'react';

import BigCalendar from 'react-big-calendar'
import moment from 'moment'
import 'react-big-calendar/lib/css/react-big-calendar.css'

import ReservationForm from './ReservationForm.js';

const events = [
  {
    id: 0,
    title: 'Board meeting',
    start: new Date(2018, 10, 19, 9, 0, 0),
    end: new Date(2018, 10, 19, 13, 0, 0),
    resourceId: 1,
  },
  {
    id: 1,
    title: 'MS training',
    allDay: false,
    start: new Date(2018, 10, 19, 14, 0, 0),
    end: new Date(2018, 10, 19, 16, 30, 0),
    resourceId: 2,
  },
  {
    id: 2,
    title: 'Team lead meeting',
    start: new Date(2018, 10, 19, 8, 30, 0),
    end: new Date(2018, 10, 19, 12, 30, 0),
    resourceId: 3,
  },
  {
    id: 11,
    title: 'Birthday Party',
    start: new Date(2018, 10, 19, 7, 0, 0),
    end: new Date(2018, 10, 19, 10, 30, 0),
    resourceId: 4,
  },
]

const resourceMap = [
  { resourceId: 1, resourceTitle: 'Board room' },
  { resourceId: 2, resourceTitle: 'Training room' },
  { resourceId: 3, resourceTitle: 'Meeting room 1' },
  { resourceId: 4, resourceTitle: 'Meeting room 2' },
]

class Calendar extends Component {
    handleSelect(start, end) {
      const title = window.prompt('New Event name')
      console.log(start, end);
      if (title)
        this.setState({
          events: [
            ...this.state.events,
            {
              start,
              end,
              title,
            },
          ],
        })
    }

    render() {
        return <BigCalendar
            selectable
            localizer={BigCalendar.momentLocalizer(moment)}
            events={events}
            defaultView={BigCalendar.Views.DAY}
            views={['day']}
            step={30}
            resources={resourceMap}
            resourceIdAccessor="resourceId"
            resourceTitleAccessor="resourceTitle"
            startAccessor="start"
            endAccessor="end"
            onSelectEvent={event => alert(event.title)}
            onSelectSlot={this.handleSelect}
        ></BigCalendar>
    }
}

export default Calendar;