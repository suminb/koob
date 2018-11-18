import React, {Component} from 'react';

const timeslotKeys = [...Array(48).keys()];
const timeslots = timeslotKeys.map(k => {
    var hour = Math.floor(k / 2);
    var minute = (k % 2) * 30;

    return [hour, minute];
});

class TimeTable extends Component {
    state = {
        rooms: [],
        reservations: {}
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

        var url = new URL(urlPrefix + '/reservations_by_room');
        url.searchParams.append('start_datetime', '2018-11-17');
        url.searchParams.append('end_datetime', '2018-11-17');

        fetch(url)
            .then(resp => resp.json())
            .then(data => {
                this.setState({
                    reservations: data
                })
            });
    }

    generateSlots(rooms) {
        return timeslots.map(ts => {
            const hour = ts[0], minute = ts[1];
            const columns = rooms.map(r => {
                return <td>{r.name}</td>
            });
            return <tr>
                <td>{hour}:{minute}</td>
                {columns}
            </tr>;
        });
    }

    render() {
        const { rooms, reservations } = this.state;

        const theads = rooms.map(r => {
            return <th key={r.id}>{r.name} ({r.capacity})</th>;
        });

        const reservationList = Object.keys(reservations).map(k => {
            var rs = reservations[k];
            return rs.map(r => {
                return <li>{r.room_id} {r.start_datetime} {r.end_datetime}</li>;
            });
        });

        return <div>
            <table className="ui table">
                <thead>
                    <tr>
                        <th></th>
                        {theads}
                    </tr>
                </thead>
                <tbody>
                    {this.generateSlots(rooms)}
                </tbody>
            </table>
            <ul>{reservationList}</ul>
        </div>;
    }
}

export default Table;