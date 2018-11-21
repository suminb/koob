import React, {Component} from 'react';
import { Form, Label } from 'semantic-ui-react'

const recurringFrequencies = [
    {value: 0, text: 'Never'},
    {value: 7, text: 'Weekly'}
];

class ReservationForm extends Component {
    state = {
        resourceId: 0,
        reserved_by: '',
        title: '',
        description: '',
        starts_at: null,
        ends_at: null,
        recurring_frequency: 0,
        recurring_count: 0,
        errors: {}
    };

    constructor(props) {
        super();
        this.props = props;
    }

    componentDidMount() {
        const props = this.props;
        //this.setState(this.props);
        this.setState({
            starts_at: props.startsAt,
            ends_at: props.endsAt
        });
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
        // FIXME: Not sure why `data.get('recurring_frequency')` returns null
        data.set('recurring_frequency', this.state.recurring_frequency);
        data.set('recurring_interval', 1);
        data.set('recurring_count', this.state.recurring_count);

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
                        parent.setState({errors: data});
                    });
                }
            })
            .catch(resp => {});
    }

    handleChange = (e, { name, value }) => {
        this.setState({ [name]: value });
        console.log(this.state);
    }

    errorLabelClassName(key) {
        var messages = this.state.errors[key];
        return (messages && messages.length > 0) ? '' : 'hidden';
    }

    render() {
        console.log('render', this.state.errors);
        return <Form onSubmit={this.handleSubmit}>
            <input type="hidden" name="resource_id" value={this.state.resourceId}/>
            <div className="field">
                <label>Your Name</label>
                <div className="fields">
                    <Form.Input placeholder="Your name" width={6}
                        name="reserved_by" value={this.state.reserved_by} onChange={this.handleChange} />
                    <Label basic color='red' pointing='left' width={4} className={this.errorLabelClassName('reserved_by')}>
                        {this.state.errors.reserved_by}
                    </Label>
                </div>
            </div>
            <div className="field">
                <label>Schedule</label>
                <div className="fields">
                    <Form.Input type="datetime-local" width={6}
                        name="starts_at" value={this.state.starts_at} onChange={this.handleChange} />
                    <Form.Input type="datetime-local" width={6}
                        name="ends_at" value={this.state.ends_at} onChange={this.handleChange} />
                    <Label basic color='red' pointing='left' width={4}
                        className={this.errorLabelClassName('starts_at') && this.errorLabelClassName('ends_at')}>
                        <div>Starts at: {this.state.errors.starts_at}</div>
                        <div>Ends at: {this.state.errors.ends_at}</div>
                    </Label>
                </div>
            </div>
            <div className="field">
                <label>Recurring Event</label>
                <div className="fields">
                <Form.Select placeholder='How often?' width={4} options={recurringFrequencies}
                    name="recurring_frequency" value={this.state.recurring_frequency} onChange={this.handleChange} />
                <Form.Input placeholder='How many times?' width={4}
                    name="recurring_count" value={this.state.recurring_count} onChange={this.handleChange} />
                </div>
            </div>
            <div className="field">
                <div className="fields">
                    <Form.Input placeholder="What is this meeting about?" width={12}
                        name="title" value={this.state.subject} onChange={this.handleChange} />
                    <Label basic color='red' pointing='left' width={4} className={this.errorLabelClassName('title')}>
                        {this.state.errors.title}
                    </Label>
                </div>
            </div>
            <div className="field">
                <Form.TextArea placeholder="More detailed description" width={12}
                    name="description" value={this.state.description} onChange={this.handleChange} />
            </div>
            <div className="field">
                <button className="ui primary button">Reserve</button>
                <button className="ui button" onClick={this.handleClose.bind(this)}>Discard</button>
            </div>
        </Form>
    }
}

export default ReservationForm;