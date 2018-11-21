from wtforms import DateTimeField, Form, StringField
from wtforms.validators import DataRequired


class ReservationForm(Form):
    starts_at = DateTimeField(
        format='%Y-%m-%dT%H:%M', validators=[DataRequired()])
    ends_at = DateTimeField(
        format='%Y-%m-%dT%H:%M', validators=[DataRequired()])
    reserved_by = StringField(validators=[DataRequired()])
    title = StringField(validators=[DataRequired()])
    description = StringField()
