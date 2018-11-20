import React, {Component} from 'react';
import { Form } from 'semantic-ui-react'

const recurringFrequencies = [
    {value: 'none', text: 'Never'},
    {value: 'weekly', text: 'Weekly'}
];

class ReservationForm extends Component {
    state = {
        roomId: 0,
        subject: null,
        description: null,
        startDatetime: null,
        endDatetime: null,
        recurringFrequency: 'none',
        recurringCount: null
    };

    constructor(props) {
        super();
        this.props = props;
    }

    componentDidMount() {
        this.setState(this.props);
    }

    handleClose(event) {
        event.preventDefault();
        if (this.props.onClose)
            this.props.onClose();
    }

    handleSubmit(event) {
        event.preventDefault();
        const data = new FormData(event.target);
        const parent = this;

        // FIXME: Code refactoring needed
        data.set('start_datetime', data.get('startDatetime'));
        data.set('end_datetime', data.get('endDatetime'));
        data.set('recurring_frequency', data.get('recurringFrequency'));
        data.set('recurring_count', data.get('recurringCount'));

        fetch('http://localhost:8087/reservations', {method: 'POST', body: data})
            .then(resp => {
                if (resp.status == 200) {
                    resp.json().then(data => {
                        parent.props.onRoomReserved(data);

                        // FIXME: Not sure if this is a good practice...
                        parent.handleClose(event);
                    });
                }
                else {
                    resp.json().then(data => {
                        console.log(resp, data);
                    });
                }
            })
            .catch(resp => {});
    }

    handleChange = (e, { name, value }) => {
        this.setState({ [name]: value });
    }

    render() {
        return <form className="ui form" onSubmit={this.handleSubmit.bind(this)}>
            <input type="hidden" name="room_id" value={this.state.roomId}/>
            <div className="field">
                <label>Schedule</label>
                <div className="fields">
                    <Form.Input type="datetime-local" width={8}
                        name="startDatetime" value={this.state.startDatetime} onChange={this.handleChange} />
                    <Form.Input type="datetime-local" width={8}
                        name="endDatetime" value={this.state.endDatetime} onChange={this.handleChange} />
                </div>
            </div>
            <div className="field">
                <label>Recurring Event</label>
                <div className="fields">
                <Form.Select placeholder='How often?' width={4} options={recurringFrequencies}
                    name="recurringFrequency" value={this.state.recurringFrequency} onChange={this.handleChange} />
                <Form.Input placeholder='How many times?' width={4}
                    name="recurringCount" value={this.state.recurringCount} onChange={this.handleChange} />
                </div>
            </div>
            <Form.Group>
                <Form.Input placeholder="What is this meeting about?" width={16}
                    name="subject" value={this.state.subject} onChange={this.handleChange} />
            </Form.Group>
            <Form.Group>
                <Form.TextArea placeholder="More detailed description" width={16}
                    name="description" value={this.state.description} onChange={this.handleChange} />
            </Form.Group>
            <div className="field">
                <button className="ui primary button">
                    Save
                </button>
                <button className="ui button" onClick={this.handleClose.bind(this)}>
                    Discard
                </button>
            </div>
        </form>
    }
}

export default ReservationForm;