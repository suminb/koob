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

        resources: [],
        reservations: [],
        reservationsDict: {}
    };

    // Code is invoked after the component is mounted/inserted into the DOM tree.
    componentDidMount() {
        // TODO: Figure out how to retrieve the current date from <Calendar> component
        var current = new Date();
        this.loadReservations(current);
    }

    /**
     * Loads all reservations within a single day
     * 
     * @param {*} date 
     */
    loadReservations(date) {
        const urlPrefix = 'http://localhost:8080/api/v1';

        fetch(urlPrefix + '/resources')
            .then(resp => resp.json())
            .then(data => {
                this.setState({
                    resources: data.resources.map(r => {return {resourceId: r.id, resourceTitle: r.title}})
                })
            });

        var url = new URL(urlPrefix + '/reservations');
        var lowerbound = new Date(date);
        lowerbound.setHours(0, 0, 0);
        var upperbound = new Date(date);
        upperbound.setHours(23, 59, 59);
        url.searchParams.append('lowerbound', this.formatDatetime(lowerbound));
        url.searchParams.append('upperbound', this.formatDatetime(upperbound));

        fetch(url)
            .then(resp => resp.json())
            .then(data => {
                this.setState({ reservations: data.map(r => this.processReservation(r)) });
            });
    }

    processReservation(raw) {
        return {
            id: raw.id,
            resourceId: raw['resource_id'],
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

    handleRangeChange(date) {
        this.loadReservations(date);
    }

    handleRoomReserved(reservation) {
        this.setState({
            reservations: this.state.reservations.concat(this.processReservation(reservation))
        });
    }

    render() {
        if (this.state.resources.length == 0) {
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
                    resources={this.state.resources}
                    resourceIdAccessor="resourceId"
                    resourceTitleAccessor="resourceTitle"
                    startAccessor="start"
                    endAccessor="end"
                    onSelectEvent={event => alert(event.title)}
                    onSelectSlot={event => this.handleSelect(event)}
                    onRangeChange={event => this.handleRangeChange(event)}
                ></BigCalendar>
                <Modal
                    open={this.state.modalFormOpen}
                    closeOnDocumentClick={true}>
                    <Modal.Header>Schedule a meeting</Modal.Header>
                    <Modal.Content>
                        <ReservationForm
                            resourceId={this.state.selectedResourceId}
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