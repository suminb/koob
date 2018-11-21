import React, { Component } from 'react';

import BigCalendar from 'react-big-calendar'
import moment from 'moment'
import { Button, Header, Image, Modal } from 'semantic-ui-react'

import ReservationForm from './ReservationForm.js';
import 'react-big-calendar/lib/css/react-big-calendar.css'

const urlPrefix = 'http://localhost:8080/api/v1';

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

        fetch(urlPrefix + '/resources')
            .then(resp => resp.json())
            .then(data => {
                this.setState({
                    resources: data.resources.map(r => {return {resourceId: r.id, resourceTitle: r.title}})
                })
            });
    }

    /**
     * Loads all reservations within a single day
     * 
     * @param {*} date 
     */
    loadReservations(date) {
        var url = new URL(urlPrefix + '/reservations');
        url.searchParams.append('date', this.formatDate(date));

        fetch(url)
            .then(resp => resp.json())
            .then(data => {
                this.setState({ reservations: data.reservations.map(r => this.processReservation(r)) });
                console.log(this.state.reservations);
            });
    }

    processReservation(raw) {
        return {
            id: raw.id,
            resourceId: raw['resource_id'],
            start: new Date(Date.parse(raw['starts_at'])),
            end: new Date(Date.parse(raw['ends_at'])),
            title: raw.title,
            description: raw.description
        };
    }

    formatDate(date) {
        return this.formatDatetime(date).slice(0, 10);
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
        this.loadReservations(date[0]);
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
                    resourceIdAccessor='resourceId'
                    resourceTitleAccessor='resourceTitle'
                    startAccessor='start'
                    endAccessor='end'
                    onSelectEvent={event => alert('TODO: Edit/delete this event')}
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
                            startsAt={this.state.startDatetime}
                            endsAt={this.state.endDatetime}
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