import React, {Component} from 'react';

const timeslots = [...Array(48).keys()];

class TimeTable extends Component {
    state = {
        rooms: [],
        reservations: [],
        reservationsDict: {}
    };

    // key = roomId
    rowSpans = {};

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

    generateRow(rooms, slot, reservationDict) {
        const hour = Math.floor(slot / 2);
        const minute = (slot % 2) * 30;
        const roomColumns = rooms.map(room => {
            const key = [slot, room.id].toString();
            var reservation = reservationDict[key];

            if (reservation) {
                // Store the remaining number of row spans
                this.rowSpans[room.id] = reservation.spans - 1;

                return <td rowSpan={reservation.spans}>*</td>
            }
            else if (this.rowSpans[room.id]) {
                this.rowSpans[room.id] -= 1;
            }
            else {
                return <td></td>
            }
        });

        return <tr>
            <td>{hour}:{minute}</td>
            {roomColumns}
        </tr>;
    }

    render() {
        const { rooms, reservationsDict } = this.state;

        const theadColumns = rooms.map(r => {
            return <th key={r.id}>{r.name} ({r.capacity})</th>;
        });

        const tbody = timeslots.map(slot => {
            return this.generateRow(rooms, slot, reservationsDict);
        })

        return <div>
            <table className="ui table">
                <thead>
                    <tr>
                        <th></th>
                        {theadColumns}
                    </tr>
                </thead>
                <tbody>
                    {tbody}
                </tbody>
            </table>
        </div>;
    }
}

export default TimeTable;