from wtforms import DateTimeField, Form, IntegerField, StringField
from wtforms.validators import DataRequired


class ReservationForm(Form):
    resource_id = IntegerField(validators=[DataRequired()])
    starts_at = DateTimeField(
        format='%Y-%m-%dT%H:%M', validators=[DataRequired()])
    ends_at = DateTimeField(
        format='%Y-%m-%dT%H:%M', validators=[DataRequired()])
    reserved_by = StringField(validators=[DataRequired()])
    title = StringField(validators=[DataRequired()])
    description = StringField()
