import React, { Component } from 'react';

import BigCalendar from 'react-big-calendar'
import moment from 'moment'
import { Button, Header, Image, Modal } from 'semantic-ui-react'

import ReservationForm from './ReservationForm.js';

import 'react-big-calendar/lib/css/react-big-calendar.css'

class Calendar extends Component {
    state = {
        modalFormOpen: false,
        startDatetime: null,
        endDatetime: null,

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
                    rooms: data.map(r => {return {roomId: r.id, roomName: r.name}})
                })
            });

        var url = new URL(urlPrefix + '/reservations');
        url.searchParams.append('start_datetime', '2018-11-17');
        url.searchParams.append('end_datetime', '2018-11-17');

        fetch(url)
            .then(resp => resp.json())
            .then(data => {
                this.setState({ reservations: data.map(r => this.processReservation(r)) });
            });
    }

    processReservation(raw) {
        return {
            id: raw.id,
            resourceId: raw['room_id'],
            title: raw.subject,
            start: new Date(Date.parse(raw['start_datetime'])),
            end: new Date(Date.parse(raw['end_datetime']))
        };
    }

    formatDatetime(date) {
        return new Date(date - (date.getTimezoneOffset() * 60000)).toISOString().slice(0, 16);
    }

    handleSelect(event) {
        var { start, end, resourceId } = event;
        this.setState({
            modalFormOpen: true,
            selectedResourceId: resourceId,
            startDatetime: this.formatDatetime(start),
            endDatetime: this.formatDatetime(end)
        });
    }

    handleRoomReserved(reservation) {
        this.setState({
            reservations: this.state.reservations.concat(this.processReservation(reservation))
        });
    }

    render() {
        if (this.state.rooms.length == 0) {
            return <div>There is no meeting room available</div>;
        }
        else {
            return <div>
                <BigCalendar
                    selectable
                    localizer={BigCalendar.momentLocalizer(moment)}
                    events={this.state.reservations}
                    defaultView={BigCalendar.Views.DAY}
                    views={['day']}
                    step={30}
                    resources={this.state.rooms}
                    resourceIdAccessor="roomId"
                    resourceTitleAccessor="roomName"
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
                            roomId={this.state.selectedResourceId}
                            startDatetime={this.state.startDatetime}
                            endDatetime={this.state.endDatetime}
                            onRoomReserved={event => this.handleRoomReserved(event)}
                            onClose={_ => this.setState({ modalFormOpen: false })}
                            ></ReservationForm>
                    </Modal.Content>
                </Modal>
            </div>
        }
    }
}

export default Calendar;