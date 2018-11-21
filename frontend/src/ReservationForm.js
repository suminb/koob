import React, {Component} from 'react';
import { Form } from 'semantic-ui-react'

const recurringFrequencies = [
    {value: 'None', text: 'Never'},
    {value: 'Weekly', text: 'Weekly'}
];

class ReservationForm extends Component {
    state = {
        resourceId: 0,
        reserved_by: '',
        title: '',
        description: '',
        start: null,
        end: null,
        recurringFrequency: 'None',
        recurringCount: 0
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

    handleSubmit = (event) => {
        event.preventDefault();
        const data = new FormData(event.target);
        const parent = this;

        // FIXME: Code refactoring needed
        // FIXME: Not sure why `data.get('recurringFrequency')` returns null
        data.set('recurring_frequency', this.state.recurringFrequency);
        data.set('recurring_interval', 1);
        data.set('recurring_count', this.state.recurringCount);

        fetch('http://localhost:8080/api/v1/reservations', {method: 'POST', body: data})
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
        console.log(this.state);
    }

    render() {
        return <Form onSubmit={this.handleSubmit}>
            <input type="hidden" name="resource_id" value={this.state.resourceId}/>
            <div className="field">
                <label>Your Name</label>
                <Form.Input placeholder="Your name" width={8}
                    name="reserved_by" value={this.state.reserved_by} onChange={this.handleChange} />
            </div>
            <div className="field">
                <label>Schedule</label>
                <div className="fields">
                    <Form.Input type="datetime-local" width={8}
                        name="starts_at" value={this.state.startDatetime} onChange={this.handleChange} />
                    <Form.Input type="datetime-local" width={8}
                        name="ends_at" value={this.state.endDatetime} onChange={this.handleChange} />
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
                    name="title" value={this.state.subject} onChange={this.handleChange} />
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
        </Form>
    }
}

export default ReservationForm;