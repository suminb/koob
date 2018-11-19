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
    resourceId: 3,
  },
]

class Calendar extends Component {
    state = {
        modalFormOpen: false,
        startDatetime: null,
        endDatetime: null,

        resourceMap: [],
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
                    resourceMap: data.map(r => {return {resourceId: r.id, resourceTitle: r.name}})
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

    formatDatetime(date) {
        return new Date(date - (date.getTimezoneOffset() * 60000)).toISOString().slice(0, 16);
    }

    handleSelect(event) {
        var { start, end } = event;
        this.setState({
            modalFormOpen: true,
            startDatetime: this.formatDatetime(start),
            endDatetime: this.formatDatetime(end)
        });
    }

    render() {
        if (this.state.resourceMap.length == 0) {
            return <div>There is no meeting room available</div>;
        }
        else {
            return <div>
                <BigCalendar
                    selectable
                    localizer={BigCalendar.momentLocalizer(moment)}
                    events={events}
                    defaultView={BigCalendar.Views.DAY}
                    views={['day']}
                    step={30}
                    resources={this.state.resourceMap}
                    resourceIdAccessor="resourceId"
                    resourceTitleAccessor="resourceTitle"
                    startAccessor="start"
                    endAccessor="end"
                    onSelectEvent={event => alert(event.title)}
                    onSelectSlot={event => this.handleSelect(event)}
                ></BigCalendar>
                <Modal
                    open={this.state.modalFormOpen}
                    closeOnDocumentClick={true}>
                    <Modal.Header>Schedule a meeting</Modal.Header>
                    <Modal.Content>
                        <ReservationForm
                            startDatetime={this.state.startDatetime}
                            endDatetime={this.state.endDatetime}
                            onClose={_ => this.setState({ modalFormOpen: false })}
                            ></ReservationForm>
                    </Modal.Content>
                </Modal>
            </div>
        }
    }
}

export default Calendar;