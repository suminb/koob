import React, {Component} from 'react';

class ReservationForm extends Component {
    state = {
        room_id: 1,
        subject: null,
        description: null,
        start_datetime: '2018-11-18T12:00',
        end_datetime: '2018-11-18T13:30'
    };

    constructor() {
        super();    
    }

    handleSubmit(event) {
        event.preventDefault();
        const data = new FormData(event.target);
        
        fetch('http://localhost:8087/reservations', {method: 'POST', body: data})
            .then(resp => console.log(resp))
    }

    handleChange(e) {
        console.log(e.target.name, e.target.value);
        const key = e.target.name;
        this.setState({key: e.target.value});
    }

    render() {
        return <form className="ui form" onSubmit={this.handleSubmit}>
            <input type="hidden" name="room_id" value={this.state.room_id}/>
            <div className="field">
                <label>Schedule</label>
                <div className="two fields">
                    <div className="field">
                        <input type="datetime-local" name="start_datetime" value={this.state.start_datetime} onChange={this.handleChange.bind(this)} />
                    </div>
                    <div className="field">
                        <input type="datetime-local" name="end_datetime" value={this.state.end_datetime} onChange={this.handleChange.bind(this)} />
                    </div>
                </div>
            </div>
            <div className="field">
                <label>Subject</label>
                <input type="text" name="subject" placeholder="What is this meeting about?" value={this.state.subject} onChange={this.handleChange.bind(this)} />
            </div>
            <div className="field">
                <label>Description (optional)</label>
                <textarea rows="3" name="description"></textarea>
            </div>
            <div className="field">
                <button className="ui primary button">
                    Save
                </button>
                <button className="ui button">
                    Discard
                </button>
            </div>
        </form>
    }
}

export default ReservationForm;