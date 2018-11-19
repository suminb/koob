import React, {Component} from 'react';

class ReservationForm extends Component {
    state = {
        roomId: 1,
        subject: null,
        description: null,
        startDatetime: null,
        endDatetime: null
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
        
        fetch('http://localhost:8087/reservations', {method: 'POST', body: data})
            .then(resp => resp.json())
            .then(data => {
                console.log(data);

                // FIXME: Not sure if this is a good practice...
                parent.handleClose(event);
            })
            .catch(resp => {
                console.log(resp);
            });
    }

    handleChange(e) {
        const key = e.target.name;
        this.setState({key: e.target.value});
    }

    render() {
        return <form className="ui form" onSubmit={this.handleSubmit.bind(this)}>
            <input type="hidden" name="room_id" value={this.state.roomId}/>
            <div className="field">
                <label>Schedule</label>
                <div className="two fields">
                    <div className="field">
                        <input type="datetime-local" name="start_datetime" value={this.state.startDatetime} onChange={this.handleChange.bind(this)} />
                    </div>
                    <div className="field">
                        <input type="datetime-local" name="end_datetime" value={this.state.endDatetime} onChange={this.handleChange.bind(this)} />
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
                <button className="ui button" onClick={this.handleClose.bind(this)}>
                    Discard
                </button>
            </div>
        </form>
    }
}

export default ReservationForm;