import React, { Component } from 'react';

import BigCalendar from 'react-big-calendar'
import moment from 'moment'
import { Button, Header, Image, Modal } from 'semantic-ui-react'

import ReservationForm from './ReservationForm.js';

import 'react-big-calendar/lib/css/react-big-calendar.css'

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
    state = {
        rooms: [],
        reservations: [],
        reservationsDict: {}
    };

    // Code is invoked after the component is mounted/inserted into the DOM tree.
    componentDidMount() {
        const urlPrefix = 'http://localhost:8087';

        fetch(urlPrefix + '/rooms')
            .then(resp => resp.json())
            .then(data => {
                this.setState({
                    rooms: data
                })
            });

        var url = new URL(urlPrefix + '/reservations');
        url.searchParams.append('start_datetime', '2018-11-17');
        url.searchParams.append('end_datetime', '2018-11-17');

        fetch(url)
            .then(resp => resp.json())
            .then(data => {
                var reservations = this.processReservations(data);
                this.setState({
                    reservations: reservations,
                    reservationsDict: this.makeReservationsDict(reservations)
                })
            });
    }

    processReservations(reservations) {
        return reservations.map(r => {
            var startDatetime = new Date(Date.parse(r['start_datetime']));
            var endDatetime = new Date(Date.parse(r['end_datetime']));

            r.slot = startDatetime.getHours() * 2 + startDatetime.getMinutes() / 30;
            r.spans = r.duration / 30;
            return r;
        });
    }

    /**
     * Converts a list of reservations into a dictionary (with [slot, room_id] as the key)
     * 
     * @param {*} reservations 
     */
    makeReservationsDict(reservations) {
        var dict = {};
        reservations.map(r => {
            const key = [r.slot, r.room_id];

            if (!(key in dict)) {
                dict[key] = r;
            }
            else {
                // This should not happen
            }
        });
        return dict;
    }

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
        return <div>
            <BigCalendar
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
            <Modal trigger={<Button>Schedule a meeting</Button>}>
                <Modal.Header>Schedule a meeting</Modal.Header>
                <Modal.Content>
                    <ReservationForm></ReservationForm>
                </Modal.Content>
            </Modal>
        </div>
    }
}

export default Calendar;